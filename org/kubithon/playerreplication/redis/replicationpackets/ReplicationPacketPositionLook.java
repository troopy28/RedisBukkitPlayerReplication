package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketPositionLook extends AbstractReplicationPacket {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public ReplicationPacketPositionLook(PacketContainer clientPositionLook, int sponsorReplicationId) {
        super(ReplicationPacketTypes.POSITION_LOOK_PACKET, sponsorReplicationId);
        WrapperPlayClientPositionLook wrapper = new WrapperPlayClientPositionLook(clientPositionLook);
        this.x = wrapper.getX();
        this.y = wrapper.getY();
        this.z = wrapper.getZ();
        this.pitch = wrapper.getPitch();
        this.yaw = wrapper.getYaw();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType); // size : 1 | packet size : 1
        writeShort(packetSize); // size : 2 | packet size : 2
        writeInteger(sponsorReplicationId); // size : 4 | packet size : 6
        writeDouble(x); // size : 8 | packet size : 14
        writeDouble(y); // size : 8 | packet size : 22
        writeDouble(z); // size : 8 | packet size : 30
        writeFloat(pitch); // size : 4 | packet size : 34
        writeFloat(yaw); // size : 4 | packet size : 38
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
