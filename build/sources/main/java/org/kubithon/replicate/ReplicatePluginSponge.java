package org.kubithon.replicate;

import com.google.gson.Gson;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.PermissionData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.kubithon.replicate.broking.BrokingConstant;
import org.kubithon.replicate.broking.PubSubManager;
import org.kubithon.replicate.broking.impl.redis.RedisCredentials;
import org.kubithon.replicate.listener.SpongeConnectionListener;
import org.kubithon.replicate.listener.SpongeInventoryClickListener;
import org.kubithon.replicate.listener.SpongeItemHeldChangedListener;
import org.kubithon.replicate.replication.ReplicationManager;
import org.kubithon.replicate.util.SpongeScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oscar Davis, troopy28
 * @since 1.0.0
 */
@Plugin(
        id = "sponge-replication",
        name = "Sponsors Replication",
        authors = "troopy28",
        description = "A plugin to replicate players between servers",
        url = "https://kubithon.org",
        version = "1.0.0",
        dependencies = {
                @Dependency(id = "spongeapi", version = "5.2.0-SNAPSHOT-c675e80"),
                @Dependency(id = "luckperms", version = "3.4.18"),
                @Dependency(id = "sponge", version = "1.10.2-2477-5.2.0-BETA-2637")/*,
                @Dependency(id="forge", version = "12.18.3.2477")*/
        }
)
public class ReplicatePluginSponge {

    private static ReplicatePluginSponge instance;

    private final File configFile = new File("replication-config.json");
    private LuckPermsApi permissionApi;

    //private PubSubManager<RedisCredentials> jedisBroker = new JedisPubSubManager();
    private ReplicationManager replicationManager;
    private SpongeConfig config;

    private Logger logger;

    public ReplicatePluginSponge() {
        instance = this;
    }

    /**
     * The sponge way to enable the system.
     */
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger = LoggerFactory.getLogger("Replicate");

        if (!checkConfigFile())
            return;

        replicationManager = new ReplicationManager();

        Gson gson = new Gson();
        try {
            config = gson.fromJson(new String(Files.readAllBytes(configFile.toPath())), SpongeConfig.class);
        } catch (IOException e) {
            logger.error("An error occurred.");
            logger.trace(ExceptionUtils.getStackTrace(e));
            logger.error("Server will stop.");
        }

        logger.info("REDIS CREDENTIALS -------------------------");
        logger.info("HOST IS " + config.getRedisHost());
        logger.info("PORT IS " + config.getRedisPort());
        logger.info("PASSWORD IS " + config.getRedisPassword());
        logger.info("------------------------------------------");

        logger.info("THE SERVER UNIQUE ID IS " + config.getServerUuid());
        logger.info("DEBUG IS " + (config.isDebug() ? "ENABLED" : "DISABLED"));
        logger.info("PERMISSION NAME OF REPLICATION IS " + config.getReplicationPermissionName());


        RedisCredentials redisCredentials = new RedisCredentials(
                config.getRedisHost(),
                config.getRedisPort(),
                config.getRedisPassword());

        try {
            logger.info("Attempting to connect to Redis...");
            connectToRedis(redisCredentials);
            logger.info("Attempting to subscribe to the pattern :..." + BrokingConstant.REPLICATION_PATTERN.concat("*"));
            //jedisBroker.psubscribe(BrokingConstant.REPLICATION_PATTERN.concat("*"), BrokingConstant.REPLICATION_TOPIC, new ReplicationListener());
            logger.info(
                    "Successfully subscribed to the pattern :'" + BrokingConstant.REPLICATION_PATTERN.concat("*") + "'"
                            + " on the topic '" + BrokingConstant.REPLICATION_TOPIC + "'.");
        } catch (Exception ex) {
            logger.trace(ExceptionUtils.getStackTrace(ex));
        }


        Sponge.getEventManager().registerListeners(this, new SpongeConnectionListener());
        Sponge.getEventManager().registerListeners(this, new SpongeInventoryClickListener());
        Sponge.getEventManager().registerListeners(this, new SpongeItemHeldChangedListener());

        SpongeScheduler.scheduleTaskLater(() -> {
            logger.info("Try to load the LuckPerms API...");
            try {
                permissionApi = LuckPerms.getApi();
                logger.info("Success!");
            } catch (Exception ex) {
                logger.info("Error!");
                logger.trace(ExceptionUtils.getStackTrace(ex));
            }
        }, 20);
    }

    @Listener
    public void onServerStop(GameStoppedEvent event) {
        //jedisBroker.disconnect();
    }

    private void connectToRedis(RedisCredentials credentials) {
        boolean connectionSuccess = false;
        try {
            //jedisBroker.connect(credentials);
            connectionSuccess = true;
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
        if (connectionSuccess)
            logger.info("Successfully connected to Redis!");

    }

    /**
     * Check the existence of the JSON configuration file. If it doesn't exist, try to create  the JSON configuration
     * file, with the default password (empty string), the default username (empty string) and the default port (3360).
     * <br/>
     * If the configuration file exists because of the creation of the file by this function during the current
     * execution of the plugin (the function created it), then it will return false and ask the user to fill it
     * correctly before restarting the server.
     *
     * @return Return that the JSON configuration file exists.
     */
    private boolean checkConfigFile() {
        if (!configFile.exists()) {
            logger.error("Could not find \"" + configFile.getName() + "\"");
            try {
                logger.error("Creating the \"" + configFile.getName() + "\" file.");
                if (configFile.createNewFile()) {
                    config = new SpongeConfig();
                    config.setDebug(false);
                    config.setRedisHost("REDIS-HOST");
                    config.setRedisPassword("REDIS-PASSWORD");
                    config.setRedisPort(3360);
                    config.setReplicationPermissionName("REPLICATION-PERMISSION-NAME");
                    config.setServerUuid(0);

                    Gson gson = new Gson();
                    Files.write(configFile.toPath(), gson.toJson(config, config.getClass()).getBytes());

                    logger.info("Credentials file created.");
                    logger.info("-------------------------------");
                    logger.info("PLEASE COMPLETE THE CONFIG FILE");
                    logger.info("-------------------------------");
                } else {
                    logger.error("Unable to create the \"" + configFile.getName() + "\" file.");
                }
            } catch (IOException e) {
                logger.error("Unable to create the \"" + configFile.getName() + "\" config file." + ExceptionUtils.getStackTrace(e));
            }
            logger.error("Shutting down the server.");
            Sponge.getServer().shutdown();
            return false;
        }
        logger.info("Credentials file found!");
        return true;
    }

    /**
     * Returns the message broker implementation.
     *
     * @return the message broker implementation.
     */
    public PubSubManager getMessageBroker() {
        //return jedisBroker;
        return null;
    }

    /**
     * @return Returns the replication manager.
     */
    public ReplicationManager getReplicationManager() {
        return replicationManager;
    }

    /**
     * @return Returns the unique id of this server in the infrastructure. This is used to retrieve the sender of a
     * packet over the Redis pub/sub system.
     */
    public int getServerId() {
        return config.getServerUuid();
    }

    /**
     * @return the single instance of the ReplicatePluginSpigot class.
     */
    public static ReplicatePluginSponge get() {
        return instance;
    }

    /**
     * @return Returns the LuckPerms API, used for managing the permissions.
     */
    public LuckPermsApi getPermissionApi() {
        return permissionApi;
    }

    /**
     * @return Returns that the specified player should be replicated.
     */
    public boolean shouldBeReplicated(Player player) {
        if (config.isDebug()) // To debug, enable the replication using sponge permissions
            return player.hasPermission("kubithon.replicate");
        else {
            Optional<User> user = permissionApi.getUserSafe(player.getUniqueId());
            if (!user.isPresent()) {
                permissionApi.getStorage().loadUser(player.getUniqueId());
                user = permissionApi.getUserSafe(player.getUniqueId());
            }
            // Here the user's value cannot be null
            Contexts contexts = permissionApi.getContextForUser(user.get()).orElse(null);
            if (contexts == null)
                return false;

            PermissionData permissionData = user.get().getCachedData().getPermissionData(contexts);
            Map<String, Boolean> permissionsMap = permissionData.getImmutableBacking();
            if (permissionsMap == null)
                return false;
            return permissionsMap.get(config.getReplicationPermissionName());
        }
    }

    public Logger getLogger() {
        return this.logger;
    }
}