package org.kubithon.playerreplication;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.plugin.java.JavaPlugin;
import org.kubithon.playerreplication.commands.AddReplicablePlayerCommand;
import org.kubithon.playerreplication.listeners.PlayerConnectionListener;
import org.kubithon.playerreplication.redis.RedisBridge;
import org.kubithon.playerreplication.redis.replicationpackets.converter.PacketConverter;
import org.kubithon.playerreplication.replication.ReplicationParameters;
import org.kubithon.playerreplication.replication.ReplicationMaster;

/**
 * Created by troopy28 on 20/02/2017.
 */
public class Main extends JavaPlugin {

    /**
     * Instance of the plugin. Accessible through the {@link #get()} method.
     */
    private static Main instance;
    private static final String LINE = "#############################";

    private ReplicationParameters replicationParameters;
    private ReplicationMaster replicationMaster;
    private RedisBridge redisBridge;
    private PacketConverter packetConverter;

    @Override
    public void onEnable() {
        Log.info(LINE);
        Log.info("##Player replication output##");
        Log.info("#######Read carefully#######");
        Log.info(LINE);
        instance = this;

        CommandExecutor ce = new AddReplicablePlayerCommand();
        getCommand("addReplicable").setExecutor(ce);

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(),this);

        replicationParameters = new ReplicationParameters();
        redisBridge = new RedisBridge();
        Log.info("RedisBridge initialized.");
        replicationMaster = new ReplicationMaster();
        Log.info("ReplicationMaster initialized.");
        packetConverter = new PacketConverter(replicationMaster);
        Log.info("PacketConverter initialized");
        Log.info(LINE);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        replicationParameters.saveParametersFile();
        redisBridge.closeBridge();
    }

    public ReplicationMaster getReplicationMaster() {
        return replicationMaster;
    }

    public ReplicationParameters getReplicationParameters() {
        return replicationParameters;
    }

    public RedisBridge getRedisBridge() {
        return redisBridge;
    }

    public PacketConverter getPacketConverter() {
        return packetConverter;
    }

    /**
     * @return Returns the instance of the plugin.
     */
    public static Main get() {
        return instance;
    }
}
