package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.packetwrapper.WrapperPlayClientFlying;
import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketFlying extends AbstractReplicationPacket {

    private boolean getOnGround;

    public ReplicationPacketFlying(PacketContainer flyingPacket, int sponsorReplicationId) {
        super(ReplicationPacketTypes.FLYING_PACKET, sponsorReplicationId);
        WrapperPlayClientFlying wrapper = new WrapperPlayClientFlying(flyingPacket);
        getOnGround = wrapper.getOnGround();
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeBoolean(getOnGround);
    }
}
