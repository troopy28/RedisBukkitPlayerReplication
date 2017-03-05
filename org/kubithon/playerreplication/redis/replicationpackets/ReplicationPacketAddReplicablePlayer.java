package org.kubithon.playerreplication.redis.replicationpackets;

import java.util.UUID;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class ReplicationPacketAddReplicablePlayer extends AbstractReplicationPacket {

    private UUID playerUUID; // 36 bytes

    public ReplicationPacketAddReplicablePlayer(UUID playerUUID, int sponsorReplicationId) {
        super(ReplicationPacketTypes.ADD_REPLICABLE_PLAYER_PACKET, sponsorReplicationId);
        this.playerUUID = playerUUID;
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeString(playerUUID.toString());
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
