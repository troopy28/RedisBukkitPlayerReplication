package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketBlockDig extends AbstractReplicationPacket {

    private static EnumWrappers.PlayerDigType[] digTypes;

    private byte status;
    private int x;
    private int y;
    private int z;

    public ReplicationPacketBlockDig(PacketContainer blockDigPacketContainer, int sponsorReplicationId) {
        super(ReplicationPacketTypes.BLOCK_DIG_PACKET, sponsorReplicationId);
        WrapperPlayClientBlockDig packet = new WrapperPlayClientBlockDig(blockDigPacketContainer);
        this.status = (byte) packet.getStatus().ordinal();
        this.x = packet.getLocation().getX();
        this.y = packet.getLocation().getY();
        this.z = packet.getLocation().getZ();

        if(digTypes == null)
            digTypes = EnumWrappers.PlayerDigType.values();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeByte(status);
        writeInteger(x);
        writeInteger(y);
        writeInteger(z);
    }

    public EnumWrappers.PlayerDigType getStatus() {
        return digTypes[status];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
