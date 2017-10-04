package org.kubithon.playerreplication.replication;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.kubithon.playerreplication.Main;
import org.kubithon.playerreplication.redis.replicationpackets.ReplicationPacketAddReplicablePlayer;
import org.kubithon.playerreplication.redis.replicationpackets.ReplicationPacketConnection;

import java.util.UUID;

/**
 * Created by troopy28 on 20/02/2017.
 * The class that manages the replication of players using packets and Redis.
 */
public class ReplicationMaster {

    private ProtocolManager protocolManager;
    private ReplicationParameters replicationParameters;

    public ReplicationMaster() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.replicationParameters = Main.get().getReplicationParameters();
        handleRedisIncomingPacketsReceiving();
    }

    //<editor-fold desc="Sending packets incoming from players of this server to other servers.">

    public void addReplicablePlayer(UUID uuid) {
        ReplicatedPlayer replicatedPlayer = replicationParameters.addReplicablePlayer(uuid);
        replicatedPlayer.setCurrentlyReplicated(true);
        subscribePacketsOf(replicatedPlayer.getReplicationId());
        ReplicationPacketAddReplicablePlayer packet = new ReplicationPacketAddReplicablePlayer(replicatedPlayer.getUuid(), replicatedPlayer.getReplicationId());
        packet.publishPacket();
        Log.info("The player with the UUID" + uuid + " is now replicated.");
    }

    public void removeReplicablePlayer(UUID uuid) {
        if (replicationParameters.isReplicable(uuid))
            replicationParameters.removeReplicablePlayer(uuid);
        Log.info("The player with the UUID " + uuid + " is no more replicated.");
    }

    public void enableReplication(Player player) {
        Log.info("Trying to enable replication");
        ReplicatedPlayer rp = replicationParameters.getReplicatedPlayer(player);
        rp.setCurrentlyReplicated(true);
        subscribePacketsOf(rp.getReplicationId());
        ReplicationPacketConnection packetConnection = new ReplicationPacketConnection(
                rp.getReplicationId(),
                (byte) 0,
                rp.getUuid().toString(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                Bukkit.getWorlds().indexOf(player.getWorld()),
                player.getDisplayName()
        );
        packetConnection.publishPacket();
        Log.info("Enabled replication for " + player.getDisplayName());
    }

    public void disableReplication(Player player) {
        Log.info("Trying to disable replication");
        ReplicatedPlayer rp = replicationParameters.getReplicatedPlayer(player);
        rp.setCurrentlyReplicated(false);
        ReplicationPacketConnection packetConnection = new ReplicationPacketConnection(
                rp.getReplicationId(),
                (byte) 1,
                rp.getUuid().toString(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                Bukkit.getWorlds().indexOf(player.getWorld()),
                player.getDisplayName()
        );
        packetConnection.publishPacket();
        Log.info("Disabled replication for " + player.getDisplayName());
    }

    boolean shouldReplicate(Player player) {
        return replicationParameters.isReplicable(player.getUniqueId()) &&
                replicationParameters.getReplicatedPlayer(player).isCurrentlyReplicated();
    }

    private void subscribePacketsOf(int sponsorReplicationId) {
        PacketType.Play.Client.getInstance().forEach(packetType -> {
            if (packetType.getLegacyId() != 0) {
                protocolManager.addPacketListener(new SponsorPacketsListener(Main.get(), ListenerPriority.NORMAL, packetType, sponsorReplicationId));
                Log.info("Listening for packet " + packetType);
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="Handling packets from redis">

    private void handleRedisIncomingPacketsReceiving() {
        Main.get().getRedisBridge().subscribeMessages(
                (channel, message) -> Main.get().getPacketConverter().interpretPacket(message)
        );
    }

    //</editor-fold>
}
