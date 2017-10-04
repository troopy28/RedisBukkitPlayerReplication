package org.kubithon.playerreplication.redis.replicationpackets.converter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.kubithon.playerreplication.Main;
import org.kubithon.playerreplication.redis.replicationpackets.*;
import org.kubithon.playerreplication.replication.ReplicationMaster;
import org.kubithon.playerreplication.replication.replicator.ReplicatorNpc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by troopy28 on 26/02/2017.
 */
public final class PacketConverter {

    private Map<PacketType, ReplicationPacketTypes> association;
    private ReplicationMaster replicationMaster;

    public PacketConverter(ReplicationMaster replicationMaster) {
        association = new HashMap<>();
        this.replicationMaster = replicationMaster;
    }

    public AbstractReplicationPacket getAssociatedReplicationPacket(PacketContainer receivedPacket, int sponsorReplicationId) {
        switch (receivedPacket.getType().getLegacyId()) {
            case 1: // Chat
                return new ReplicationPacketChat(receivedPacket, sponsorReplicationId);
            case 3: // Flying
                return new ReplicationPacketFlying(receivedPacket, sponsorReplicationId);
            case 4: // PacketPlayInPosition
                return new ReplicationPacketPosition(receivedPacket, sponsorReplicationId);
            case 5: // Look
                return new ReplicationPacketLook(receivedPacket, sponsorReplicationId);
            case 6: // Position look
                return new ReplicationPacketPositionLook(receivedPacket, sponsorReplicationId);
            case 7: // Block dig
                return new ReplicationPacketBlockDig(receivedPacket, sponsorReplicationId);
            case 10: // Arm animation
                return new ReplicationPacketArmAnimation(receivedPacket, sponsorReplicationId);
            default:
                return null;
        }
    }

    public void interpretPacket(byte[] packetBytes) {
        byte type = packetBytes[0];
        short packetSize = AbstractReplicationPacket.byteArrayToShort(new byte[]{packetBytes[1], packetBytes[2]});
        int sponsorReplicationId = AbstractReplicationPacket.byteArrayToInteger(
                new byte[]{packetBytes[3], packetBytes[4], packetBytes[5], packetBytes[6]});
        //Log.info("Type : " + type + " Size : " + packetSize + " RepID : " + sponsorReplicationId);

        switch (type) {
            case 0x00: // Add replicable player
                interpretAddReplicablePlayerPacket(packetBytes, sponsorReplicationId);
                break;
            case 0x01: // Position only
                interpretPositionPacket(packetBytes, sponsorReplicationId);
                break;
            case 0x03: // Chat
                interpretChatPacket(packetBytes, sponsorReplicationId);
                break;
            case 0x05: // Position and look
                interpretPositionLookPacket(packetBytes, sponsorReplicationId);
                break;
            case 0x06: // Only look
                interpretLookPacket(packetBytes, sponsorReplicationId);
                break;
            case 0x07: // Arm animation
                interpretArmAnimationPacket(sponsorReplicationId);
                break;
            case 0x08: // Connection / disconnection
                interpretConnectionPacket(packetBytes, sponsorReplicationId);
            default:
                break;
        }
    }

    private void interpretChatPacket(byte[] packetBytes, int sponsorReplicationId) {
        // byte 8 to 9 (base 1, i.e 7, 8 in array) -> length of message. short
        short messageLength = AbstractReplicationPacket.byteArrayToShort(new byte[]{packetBytes[7], packetBytes[8]});
        Log.info("LENGTH OF MESSAGE IS " + messageLength);
        byte[] messageBytes = Arrays.copyOfRange(packetBytes, 9, 9 + messageLength);
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        Log.info("Message is : " + message);
        // TODO PROCESS RECEIVED MESSAGE
    }

    private void interpretAddReplicablePlayerPacket(byte[] packetBytes, int sponsorReplicationId) {
        byte[] uuidBytes = Arrays.copyOfRange(packetBytes, 7, 43); // 7 + 36 = 43. UUID is 36 chars.
        String uuid = new String(uuidBytes, StandardCharsets.UTF_8);
        Log.info("UUID is : " + uuid + " and replication ID is " + sponsorReplicationId);
        Main.get().getReplicationMaster().addReplicablePlayer(UUID.fromString(uuid));
    }

    private void interpretPositionLookPacket(byte[] packetBytes, int sponsorReplicationId) {
        double xpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[7], packetBytes[8], packetBytes[9], packetBytes[10],
                        packetBytes[11], packetBytes[12], packetBytes[13], packetBytes[14]});
        double ypos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[15], packetBytes[16], packetBytes[17], packetBytes[18],
                        packetBytes[19], packetBytes[20], packetBytes[21], packetBytes[22]});
        double zpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[23], packetBytes[24], packetBytes[25], packetBytes[26],
                        packetBytes[27], packetBytes[28], packetBytes[29], packetBytes[30]});
        float pitch = AbstractReplicationPacket.byteArrayToFloat(
                new byte[]{packetBytes[31], packetBytes[32], packetBytes[33], packetBytes[34]});
        float yaw = AbstractReplicationPacket.byteArrayToFloat(
                new byte[]{packetBytes[35], packetBytes[36], packetBytes[37], packetBytes[38]});

        ReplicatorNpc replicatorNpc = ReplicatorNpc.getReplicatorNpc(sponsorReplicationId);
        if (replicatorNpc != null)
            replicatorNpc.updateLocationAndLook(xpos, ypos, zpos, yaw, pitch);
    }

    private void interpretPositionPacket(byte[] packetBytes, int sponsorReplicationId) {
        double xpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[7], packetBytes[8], packetBytes[9], packetBytes[10],
                        packetBytes[11], packetBytes[12], packetBytes[13], packetBytes[14]});
        double ypos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[15], packetBytes[16], packetBytes[17], packetBytes[18],
                        packetBytes[19], packetBytes[20], packetBytes[21], packetBytes[22]});
        double zpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[23], packetBytes[24], packetBytes[25], packetBytes[26],
                        packetBytes[27], packetBytes[28], packetBytes[29], packetBytes[30]});
        ReplicatorNpc replicatorNpc = ReplicatorNpc.getReplicatorNpc(sponsorReplicationId);
        if (replicatorNpc != null)
            replicatorNpc.updateLocation(xpos, ypos, zpos);
    }

    private void interpretLookPacket(byte[] packetBytes, int sponsorReplicationId) {
        float pitch = AbstractReplicationPacket.byteArrayToFloat(
                new byte[]{packetBytes[7], packetBytes[8], packetBytes[9], packetBytes[10]});
        float yaw = AbstractReplicationPacket.byteArrayToFloat(
                new byte[]{packetBytes[11], packetBytes[12], packetBytes[13], packetBytes[14]});

        ReplicatorNpc replicatorNpc = ReplicatorNpc.getReplicatorNpc(sponsorReplicationId);
        if (replicatorNpc != null)
            replicatorNpc.updateLook(yaw, pitch);
    }

    private void interpretArmAnimationPacket(int sponsorReplicationId) {
        ReplicatorNpc replicatorNpc = ReplicatorNpc.getReplicatorNpc(sponsorReplicationId);
        if (replicatorNpc != null)
            replicatorNpc.moveArm();
    }

    private void interpretConnectionPacket(byte[] packetBytes, int sponsorReplicationId) {
        byte state = packetBytes[7]; // Connection or disconnection ?
        byte[] uuidBytes = Arrays.copyOfRange(packetBytes, 8, 44); // 8 + 36 = 44. UUID is 36 chars.
        String uuid = new String(uuidBytes, StandardCharsets.UTF_8);

        double xpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[44], packetBytes[45], packetBytes[46], packetBytes[47],
                        packetBytes[48], packetBytes[49], packetBytes[50], packetBytes[51]});
        double ypos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[52], packetBytes[53], packetBytes[54], packetBytes[55],
                        packetBytes[56], packetBytes[57], packetBytes[58], packetBytes[59]});
        double zpos = AbstractReplicationPacket.byteArrayToDouble(
                new byte[]{
                        packetBytes[60], packetBytes[61], packetBytes[62], packetBytes[63],
                        packetBytes[64], packetBytes[65], packetBytes[66], packetBytes[67]});
        byte worldIndex = packetBytes[68];

        byte pseudoLength = packetBytes[69];
        byte[] pseudoBytes = Arrays.copyOfRange(packetBytes, 70, 70 + pseudoLength);
        String pseudo = new String(pseudoBytes, StandardCharsets.UTF_8);

        if (state == 0) { // Connection
            ReplicatorNpc.createReplicatorNpc(
                    Main.get().getReplicationParameters().getReplicatedPlayer(UUID.fromString(uuid)),
                    new Location(Bukkit.getWorlds().get(worldIndex), xpos, ypos, zpos),
                    pseudo);
        } else { // Disconnection
            ReplicatorNpc replicatorNpc = ReplicatorNpc.getReplicatorNpc(sponsorReplicationId);
            if (replicatorNpc != null) {
                replicatorNpc.destroy();
            }
        }
    }
}