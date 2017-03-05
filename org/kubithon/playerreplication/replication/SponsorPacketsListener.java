package org.kubithon.playerreplication.replication;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.kubithon.playerreplication.Main;
import org.kubithon.playerreplication.redis.replicationpackets.AbstractReplicationPacket;

/**
 * Created by troopy28 on 27/02/2017.
 */
public class SponsorPacketsListener extends PacketAdapter {

    private ReplicationMaster replicationMaster;
    private PacketType packetType;
    private int sponsorReplicationId;

    public SponsorPacketsListener(Main main, ListenerPriority listenerPriority, PacketType type, int sponsorId) {
        super(main, listenerPriority, type);
        replicationMaster = main.getReplicationMaster();
        this.packetType = type;
        this.sponsorReplicationId = sponsorId;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() == packetType &&
                replicationMaster.shouldReplicate(event.getPlayer())) {
            AbstractReplicationPacket p = Main.get().getPacketConverter().getAssociatedReplicationPacket(event.getPacket(), sponsorReplicationId);
            if (p != null)
                p.publishPacket();
        }
    }
}
