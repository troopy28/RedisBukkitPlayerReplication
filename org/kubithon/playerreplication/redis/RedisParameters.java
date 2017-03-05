package org.kubithon.playerreplication.redis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.kubithon.playerreplication.Utils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by troopy28 on 25/02/2017.
 * Parameters of the Redis bridge class.
 */
class RedisParameters implements Serializable {

    private static final String REDIS_PARAMETERS_FILE = "RedisParameters.json";

    private String redisHost;
    private String listeningChannel;
    private transient List<byte[]> bytesReplicationPublishingChannels;
    private transient byte[] listeningChannelBytes;
    private List<String> replicationPublishingChannels;

    RedisParameters(boolean readParameters) {
        bytesReplicationPublishingChannels = new ArrayList<>();
        replicationPublishingChannels = new ArrayList<>();
        if (readParameters) {
            checkParamsFileExistence();
            RedisParameters fileParams = readParameters();
            redisHost = fileParams.redisHost;
            Log.info("Redis host : " + redisHost);
            listeningChannel = fileParams.listeningChannel;
            listeningChannelBytes = listeningChannel.getBytes(StandardCharsets.UTF_8);
            Log.info("Listening channel : " + listeningChannel);
            replicationPublishingChannels = fileParams.replicationPublishingChannels;
            for (String replicationPublishingChannel : replicationPublishingChannels) {
                Log.info("String channel is " + replicationPublishingChannel);
                Log.info("Binary channel is " + Arrays.toString(replicationPublishingChannel.getBytes(StandardCharsets.UTF_8)));
                bytesReplicationPublishingChannels.add(replicationPublishingChannel.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    void saveParameters() {
        replicationPublishingChannels.clear();
        for (byte[] bytes : bytesReplicationPublishingChannels) {
            replicationPublishingChannels.add(new String(bytes, StandardCharsets.UTF_8));
        }
        Type type = new TypeToken<RedisParameters>() {
        }.getType();
        Utils.writeFileContent(REDIS_PARAMETERS_FILE, new Gson().toJson(this, type));
    }

    /**
     * Checks if the file containing the redis parameters exists. If not, create it.
     */
    private void checkParamsFileExistence() {
        if (!Utils.fileExists(REDIS_PARAMETERS_FILE)) {
            redisHost = "localhost";
            listeningChannel = "hubName-example-1";
            replicationPublishingChannels.clear();
            replicationPublishingChannels.add("OtherHub2");
            replicationPublishingChannels.add("OtherHub3");
            replicationPublishingChannels.add("OtherHub4");
            saveParameters();
        } else
            Log.info("Redis parameters exist.");
    }

    private RedisParameters readParameters() {
        Type type = new TypeToken<RedisParameters>() {
        }.getType();
        return new Gson().fromJson(Utils.readFileContent(REDIS_PARAMETERS_FILE), type);
    }

    String getRedisHost() {
        return redisHost;
    }

    List<byte[]> getReplicationPublishingChannels() {
        return bytesReplicationPublishingChannels;
    }

    public byte[] getListeningChannel() {
        return listeningChannelBytes;
    }
}
