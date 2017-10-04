package org.kubithon.playerreplication.redis;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.kubithon.playerreplication.Main;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BinaryJedisPubSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by troopy28 on 25/02/2017.
 * The class that helps to communicate with other hubs using Redis pub / sub.
 */
public class RedisBridge implements Runnable {


    private RedisParameters redisParameters;
    // Probably not the better way of doing theses two connections, but... it works.
    private BinaryJedis publisherJedis;
    private BinaryJedis subscriberJedis;
    private BinaryJedisPubSub pubSub;

    private volatile List<MessageReceived> callbacks;

    public RedisBridge() {
        this.redisParameters = new RedisParameters(true);
        this.publisherJedis = new BinaryJedis(redisParameters.getRedisHost());
        this.subscriberJedis = new BinaryJedis(redisParameters.getRedisHost());
        this.pubSub = getPubSub();
        this.callbacks = new ArrayList<>();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.get(), this, 20);
    }

    @Override
    public void run() {
        subscriberJedis.subscribe(pubSub, redisParameters.getListeningChannel());
    }

    private BinaryJedisPubSub getPubSub() {
        return new BinaryJedisPubSub() {
            @Override
            public void onUnsubscribe(byte[] channel, int subscribedChannels) {
                handleUnsubscribing();
            }

            @Override
            public void onSubscribe(byte[] channel, int subscribedChannels) {
                handleSubscribing();
            }

            @Override
            public void onMessage(byte[] channel, byte[] message) {
                handleReceivedMessage(channel, message);
            }
        };
    }

    public void publishReplicationPacket(byte[] packetBytes) {
        redisParameters.getReplicationPublishingChannels().forEach(c -> publisherJedis.publish(c, packetBytes));
    }

    public void closeBridge() {
        redisParameters.saveParameters();
        subscriberJedis.close();
        publisherJedis.close();
    }

    private void handleSubscribing() {
        Log.info("onSubscribe");
    }

    private void handleUnsubscribing() {
        Log.info("onUnsubscribe");
    }

    private void handleReceivedMessage(byte[] channel, byte[] message) {
        callbacks.forEach(c -> c.onReceivedMessage(channel, message));
    }

    public void subscribeMessages(MessageReceived callback) {
        callbacks.add(callback);
    }
}
