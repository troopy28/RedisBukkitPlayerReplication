package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientLook;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketLook extends AbstractReplicationPacket {

    private float pitch;
    private float yaw;

    public ReplicationPacketLook(PacketContainer lookPacket, int sponsorReplicationId) {
        super(ReplicationPacketTypes.LOOK_PACKET, sponsorReplicationId);
        WrapperPlayClientLook wrapper = new WrapperPlayClientLook(lookPacket);
        this.yaw = wrapper.getYaw();
        this.pitch = wrapper.getPitch();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeFloat(pitch);
        writeFloat(yaw);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
