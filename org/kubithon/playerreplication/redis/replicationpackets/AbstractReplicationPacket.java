package org.kubithon.playerreplication.redis.replicationpackets;

import org.apache.commons.lang.ArrayUtils;
import org.kubithon.playerreplication.Main;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by troopy28 on 26/02/2017.
 * Contains the header of the packet. The header is always 3 bytes (1 byte = type; 2 bytes = size).
 */
public abstract class AbstractReplicationPacket {

    private List<Byte> packetBytesList;
    /**
     * The size of the final packet.
     */
    short packetSize;
    /**
     * The type of the packet.
     */
    byte packetType;
    /**
     * The replication ID of the sponsor that sent this packet.
     */
    int sponsorReplicationId;

    AbstractReplicationPacket(ReplicationPacketTypes packetType, int sponsorReplicationId) {
        this.packetType = packetType.getType();
        this.sponsorReplicationId = sponsorReplicationId;
        packetSize = packetType.getSize();
        packetBytesList = new ArrayList<>();
    }

    protected abstract void composePacket();

    private void sendPacket() {
        Main.get().getRedisBridge().publishReplicationPacket(
                ArrayUtils.toPrimitive(
                        packetBytesList.toArray(new Byte[packetBytesList.size()])
                )
        );
    }

    /**
     * Composes and sends the packet to other servers using Redis pub / sub.
     */
    public void publishPacket() {
        composePacket();
        sendPacket();
    }

    void writeByte(byte var) {
        packetBytesList.add(var);
    }

    void writeBytes(byte[] var) {
        for (byte b : var) {
            packetBytesList.add(b);
        }
    }

    void writeString(String var) {
        for (byte b : var.getBytes(StandardCharsets.UTF_8)) {
            packetBytesList.add(b);
        }
    }

    void writeFloat(float var) {
        for (byte b : floatToByteArray(var)) {
            packetBytesList.add(b);
        }
    }

    void writeShort(short var) {
        for (byte b : shortToByteArray(var)) {
            packetBytesList.add(b);
        }
    }

    void writeLong(long var) {
        for (byte b : longToByteArray(var)) {
            packetBytesList.add(b);
        }
    }

    void writeDouble(double var) {
        for (byte b : doubleToByteArray(var)) {
            packetBytesList.add(b);
        }
    }

    void writeBoolean(boolean var) {
        packetBytesList.add(booleanToByte(var));
    }

    void writeInteger(int var) {
        for (byte b : integerToByteArray(var)) {
            packetBytesList.add(b);
        }
    }

    public static byte booleanToByte(boolean value) {
        return (byte) (value ? 1 : 0);
    }

    public static boolean byteToBoolean(byte value) {
        return value == 1;
    }

    public static byte[] shortToByteArray(short value) {
        return new byte[]{(byte) ((value >> 8) & 0xFF), (byte) (value & 0xFF)};
    }

    public static short byteArrayToShort(byte[] value) {
        return (short) (((value[0] & 0xFF) << 8) | (value[1] & 0xFF));
    }

    public static byte[] longToByteArray(long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    public static byte[] floatToByteArray(float value) {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    public static float byteArrayToFloat(byte[] value) {
        return ByteBuffer.wrap(value).getFloat();
    }

    public static double byteArrayToDouble(byte[] value) {
        return ByteBuffer.wrap(value).getDouble();
    }

    public static byte[] doubleToByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static byte[] integerToByteArray(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        return buffer.array();
    }

    public static int byteArrayToInteger(byte[] bytes) {
        ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
        return wrapped.getInt(); // 1
    }
}
