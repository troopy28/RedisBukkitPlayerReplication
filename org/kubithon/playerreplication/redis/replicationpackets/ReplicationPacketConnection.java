package org.kubithon.playerreplication.redis.replicationpackets;

import java.nio.charset.StandardCharsets;

/**
 * Created by troopy28 on 27/02/2017.
 */
public final class ReplicationPacketConnection extends AbstractReplicationPacket {

    private byte state; // 0 : connection, 1 : disconnection
    private String uuid;
    private double x;
    private double y;
    private double z;
    private byte worldIndex;
    private String pseudo;

    public ReplicationPacketConnection(int sponsorReplicationId, byte state, String uuid, double x, double y, double z, int worldIndex, String pseudo) {
        super(ReplicationPacketTypes.CONNECTION_PACKET, sponsorReplicationId);
        this.state = state;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldIndex = (byte) worldIndex;
        this.pseudo = pseudo;
    }

    @Override
    protected void composePacket() {
        writeByte(packetType);
        writeShort(packetSize);
        writeInteger(sponsorReplicationId);
        writeByte(state);
        writeString(uuid);
        writeDouble(x);
        writeDouble(y);
        writeDouble(z);
        writeByte(worldIndex);
        writeByte((byte) pseudo.getBytes(StandardCharsets.UTF_8).length);
        writeString(pseudo);
    }
}
