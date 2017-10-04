package org.kubithon.playerreplication.redis.replicationpackets;

/**
 * Created by troopy28 on 26/02/2017.
 * List of the packets sent through Redis.
 */
public enum ReplicationPacketTypes {

    ADD_REPLICABLE_PLAYER_PACKET(0x00, 10),
    POSITION_PACKET(0x01, 10),
    BLOCK_DIG_PACKET(0x02, 10),
    CHAT_PACKET(0x03, 10),
    FLYING_PACKET(0x04, 10),
    POSITION_LOOK_PACKET(0x05, 10),
    LOOK_PACKET(0x06, 10),
    ARM_ANIMATION_PACKET(0x07, 10),
    CONNECTION_PACKET(0x08, 10);

    private byte type;
    private short size;

    ReplicationPacketTypes(int type, int size) {
        this.type = (byte) type;
        this.size = (short) size;
    }

    public byte getType() {
        return type;
    }

    public short getSize() {
        return size;
    }
}