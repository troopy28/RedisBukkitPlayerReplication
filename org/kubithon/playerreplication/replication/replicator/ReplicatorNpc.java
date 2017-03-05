package org.kubithon.playerreplication.replication.replicator;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityLook;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.kubithon.playerreplication.Main;
import org.kubithon.playerreplication.replication.ReplicatedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by troopy28 on 26/02/2017.
 * The fake player used to replicate the sponsors.
 */
public class ReplicatorNpc implements Runnable {

    private static Map<Integer, ReplicatorNpc> replicatorNpcMap = new HashMap<>();

    private ReplicatedPlayer associatedReplicatedPlayer;
    private List<Player> observers;
    private BukkitTask updatingTask;
    private EntityPlayer fakePlayer;

    private ReplicatorNpc(ReplicatedPlayer replicatedPlayer, Location location, String playerName) {
        this.associatedReplicatedPlayer = replicatedPlayer;
        this.observers = new ArrayList<>();

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        fakePlayer = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(replicatedPlayer.getUuid(), playerName), new PlayerInteractManager(nmsWorld));
        fakePlayer.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);

        this.updatingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.get(), this, 5, 5);

        Log.info("Created a new ReplicatorNpc. Entity ID : " + fakePlayer.getId() +
                " UUID " + replicatedPlayer.getUuid() +
                " replication ID " + replicatedPlayer.getReplicationId());
    }

    public static ReplicatorNpc createReplicatorNpc(ReplicatedPlayer replicatedPlayer, Location location, String playerName) {
        if (!replicatorNpcMap.containsKey(replicatedPlayer.getReplicationId())) {
            return replicatorNpcMap.put(replicatedPlayer.getReplicationId(), new ReplicatorNpc(replicatedPlayer, location, playerName));
        }
        return null;
    }

    public static ReplicatorNpc getReplicatorNpc(int replicationId) {
        return replicatorNpcMap.get(replicationId);
    }

    private void removeObserver(Player player) { // NOSONAR -> Used using method references. Sonar doesn't see it......
        WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        destroy.setEntityIds(new int[]{fakePlayer.getId()});
        destroy.sendPacket(player);
        observers.remove(player);
    }

    private void addObserver(Player player) { // working
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, fakePlayer));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(fakePlayer));

        observers.add(player);
        Log.info("---------------------------------");
        Log.info("Added the observer " + player.getDisplayName());
        Log.info("NPC Spawned in " + fakePlayer.locX + " " + fakePlayer.locY + " " + fakePlayer.locZ);
        Log.info("---------------------------------");
    }

    public void updateLocation(double x, double y, double z) { // not working
        PacketPlayOutEntity.PacketPlayOutRelEntityMove movePacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                fakePlayer.getId(),
                (long) (x - fakePlayer.locX) * 32,
                (long) (y - fakePlayer.locY) * 32,
                (long) (z - fakePlayer.locZ) * 32,
                false // for testing. Maybe it should get a onGround with the redis pub/sub.
        );

        observers.forEach(player -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(movePacket));
        fakePlayer.locX = x;
        fakePlayer.locY = y;
        fakePlayer.locZ = z;
        Log.info(x + " " + y + " " + z);
    }

    public void updateLocationAndLook(double x, double y, double z, float pitch, float yaw) { // not working
        Log.info("Moved of " + x + " " + y + " " + z); // TODO remove this debug message
        WrapperPlayServerRelEntityMoveLook movementPacket = new WrapperPlayServerRelEntityMoveLook();
        movementPacket.setEntityID(fakePlayer.getId());
        movementPacket.setDx(x - fakePlayer.locX);
        movementPacket.setDx(y - fakePlayer.locY);
        movementPacket.setDx(z - fakePlayer.locZ);
        movementPacket.setPitch(pitch);
        movementPacket.setYaw(yaw);

        observers.forEach(movementPacket::sendPacket);
        fakePlayer.move(x - fakePlayer.locX, y - fakePlayer.locY, z - fakePlayer.locZ);
    }

    /**
     * Called from {@link org.kubithon.playerreplication.redis.replicationpackets.converter.PacketConverter} according
     * to the data it received from Redis pub/sub. It sends a packet to every observer informing them of the current
     * pitch and yaw of the Replicated Player, that is to say the NPC.
     *
     * @param yaw   The current yaw of the player.
     * @param pitch The current pitch of the player.
     */
    public void updateLook(float yaw, float pitch) { // working
        WrapperPlayServerEntityLook lookPacket = new WrapperPlayServerEntityLook();
        lookPacket.setEntityID(fakePlayer.getId());
        lookPacket.setYaw(yaw);
        lookPacket.setPitch(pitch);
        observers.forEach(lookPacket::sendPacket);
    }

    public void destroy() { // working
        Lists.newArrayList(observers).forEach(this::removeObserver);
        replicatorNpcMap.remove(associatedReplicatedPlayer.getReplicationId());
        updatingTask.cancel();
    }

    public void moveArm() { // working
        WrapperPlayServerAnimation animationPacket = new WrapperPlayServerAnimation();
        animationPacket.setAnimation(0);
        animationPacket.setEntityID(fakePlayer.getId());
        observers.forEach(animationPacket::sendPacket);
    }

    @Override
    public void run() {
        // 100^2 = 10 000 (visible at 100 blocks. distance squared will then be 10 000
        for (Player pls : Bukkit.getOnlinePlayers()) {
            double d = distanceSquared(pls.getLocation());
            if (!observers.contains(pls) && d < 10000)
                addObserver(pls);
            else if (observers.contains(pls) && d >= 10000)
                removeObserver(pls);
        }
    }

    /**
     * The distance (non-squared) wasn't useful and more heavy in calculations (square root). The squared distance
     * appears to be a better solution.
     *
     * @param loc Location from which the distance to the NPC will be computed.
     * @return Returns the squared distance between the NPC and the specified location.
     */
    private double distanceSquared(Location loc) {
        return (loc.getX() - fakePlayer.locX) * (loc.getX() - fakePlayer.locX)
                + (loc.getY() - fakePlayer.locY) * (loc.getY() - fakePlayer.locY)
                + (loc.getZ() - fakePlayer.locZ) * (loc.getZ() - fakePlayer.locZ);
    }
}