package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientChat;
import com.comphenix.protocol.events.PacketContainer;

import java.nio.charset.StandardCharsets;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketChat extends AbstractReplicationPacket {

    private String message;

    public ReplicationPacketChat(PacketContainer packet, int sponsorReplicationId) {
        super(ReplicationPacketTypes.CHAT_PACKET, sponsorReplicationId);
        WrapperPlayClientChat wrapper = new WrapperPlayClientChat(packet);
        this.message = wrapper.getMessage();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeShort((short) message.getBytes(StandardCharsets.UTF_8).length);
        writeString(message);
    }
}
