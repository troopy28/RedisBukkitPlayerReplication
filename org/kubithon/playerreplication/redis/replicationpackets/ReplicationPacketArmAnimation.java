package org.kubithon.playerreplication.redis.replicationpackets;

import com.comphenix.protocol.events.PacketContainer;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketArmAnimation extends AbstractReplicationPacket {

    public ReplicationPacketArmAnimation(PacketContainer armPacket, int sponsorReplicationId) {
        super(ReplicationPacketTypes.ARM_ANIMATION_PACKET, sponsorReplicationId);
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
    }
}
