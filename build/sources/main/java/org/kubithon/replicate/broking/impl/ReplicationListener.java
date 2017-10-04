package org.kubithon.replicate.broking.impl;

import org.kubithon.replicate.ReplicatePluginSponge;
import org.kubithon.replicate.broking.BrokingConstant;
import org.kubithon.replicate.broking.MessageListener;
import org.kubithon.replicate.replication.ReplicationManager;
import org.kubithon.replicate.replication.protocol.KubithonPacket;

import java.util.Base64;

/**
 * Listens for replications.
 *
 * @author Oscar Davis, troopy28
 * @since 1.0.0
 */
public class ReplicationListener implements MessageListener {

    private ReplicationManager replicationManager;
    private int patternLength = BrokingConstant.REPLICATION_PATTERN.length();

    public ReplicationListener() {
        this.replicationManager = ReplicatePluginSponge.get().getReplicationManager();
    }

    @Override
    public void patternReceive(String pattern, String topic, String message) {
        int senderUid = Integer.parseInt(topic.substring(patternLength, patternLength + 1));

        String playerName = topic.substring(patternLength + 1);
        if (senderUid != ReplicatePluginSponge.get().getServerId()) {
            byte[] bytes = Base64.getDecoder().decode(message);

            KubithonPacket receivedKubicket = KubithonPacket.deserialize(bytes);
            if (receivedKubicket != null)
                replicationManager.handleKubicket(playerName, receivedKubicket);
        }
    }
}
