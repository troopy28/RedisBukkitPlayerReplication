package org.kubithon.replicate.broking;

/**
 * @author Oscar Davis
 * @since 1.0.0
 */
public class BrokingConstant {

    /**
     * The Redis pattern used for replications. The topic depends on the packet that has been sent.
     * The part after the colon is the name of the player.
     */
    public static final String REPLICATION_PATTERN = "replicate:";
    /**
     * The Redis topic used for the replication packets.
     */
    public static final String REPLICATION_TOPIC = "replication";

    /**
     * This class cannot be implemented.
     */
    private BrokingConstant() {
    }

}
