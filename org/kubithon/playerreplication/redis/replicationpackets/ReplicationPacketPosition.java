package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by troopy28 on 26/02/2017.
 * The packet sent through Redis to update the position of a replicated player.
 */
public final class ReplicationPacketPosition extends AbstractReplicationPacket{

    private double x;
    private double y;
    private double z;

    public ReplicationPacketPosition(PacketContainer positionPacketContainer, int sponsorReplicationId) {
        super(ReplicationPacketTypes.POSITION_PACKET, sponsorReplicationId);
        WrapperPlayClientPosition receivedPacket = new WrapperPlayClientPosition(positionPacketContainer);
        x = receivedPacket.getX();
        y = receivedPacket.getY();
        z = receivedPacket.getZ();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);
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
}
