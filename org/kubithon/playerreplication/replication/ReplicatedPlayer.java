package org.kubithon.playerreplication.replication;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by troopy28 on 26/02/2017.
 */
public class ReplicatedPlayer implements Serializable {
    /**
     * The Minecraft unique ID of the player.
     */
    private UUID uuid;
    /**
     * The replication ID of this sponsor.
     */
    private int replicationId;
    /**
     * Is the player currently replicated, i. e. connected AND replicated.
     */
    private transient boolean currentlyReplicated;

    public ReplicatedPlayer() {

    }

    public ReplicatedPlayer(UUID uuid, int replicationId) {
        this.uuid = uuid;
        this.replicationId = replicationId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getReplicationId() {
        return replicationId;
    }

    public boolean isCurrentlyReplicated() {
        return currentlyReplicated;
    }

    public void setCurrentlyReplicated(boolean currentlyReplicated) {
        this.currentlyReplicated = currentlyReplicated;
    }
}
