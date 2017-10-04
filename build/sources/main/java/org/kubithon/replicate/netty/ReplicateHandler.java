package org.kubithon.replicate.netty;

import net.minecraft.entity.player.EntityPlayerMP;
import org.kubithon.replicate.ReplicatePluginSponge;
import org.kubithon.replicate.broking.BrokingConstant;
import org.kubithon.replicate.replication.protocol.PlayerConnectionKubicket;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Base64;

/**
 * Util class for adding the {@link ReplicationChannelHandler}.
 *
 * @author Oscar Davis, troopy28
 * @since 1.0.0
 */
public class ReplicateHandler {

    private static ReplicatePluginSponge plugin = ReplicatePluginSponge.get();

    private ReplicateHandler() {
    }

    /**
     * Adds a channel handler to the given player which is going to replicate his packets
     * to servers on the whole network.
     *
     * @param player The player.
     */
    public static void handle(Player player) {
        ((EntityPlayerMP)player).connection.getNetworkManager().
                channel().pipeline().addBefore("packet_handler", "replication-channel", new ReplicationChannelHandler(player));
    }

    /**
     * Creates a {@link PlayerConnectionKubicket} to send a message saying that the specified player has disconnected,
     * and should no longer be replicated.
     *
     * @param player The player you no more want to be replicated.
     */
    public static void stopHandling(Player player) {
        PlayerConnectionKubicket connectionKubicket = new PlayerConnectionKubicket();
        connectionKubicket.setPlayerName(player.getName());
        connectionKubicket.setPlayerUuid(player.getUniqueId().toString());
        connectionKubicket.setState((byte) 1);
        ReplicatePluginSponge.get().getMessageBroker().publish(
                BrokingConstant.REPLICATION_PATTERN.concat(String.valueOf(plugin.getServerId())).concat(player.getName()),
                Base64.getEncoder().encodeToString(connectionKubicket.serialize()));
    }
}