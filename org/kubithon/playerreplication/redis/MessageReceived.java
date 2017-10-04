package org.kubithon.playerreplication.redis;

/**
 * Created by troopy28 on 26/02/2017.
 */
@FunctionalInterface
public interface MessageReceived {
    void onReceivedMessage(byte[] channel, byte[] message);
}
