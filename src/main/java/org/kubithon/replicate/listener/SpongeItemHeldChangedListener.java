package org.kubithon.replicate.listener;

import org.kubithon.replicate.ReplicatePluginSponge;
import org.kubithon.replicate.replication.ReplicationManager;
import org.kubithon.replicate.util.SpongeScheduler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;

/**
 * Creation: 16/09/2017.
 *
 * @author troopy28
 * @since 1.0.0
 */
public class SpongeItemHeldChangedListener {

    /**
     * Called whenever a player changes the active slot of his inventory (using the scroll, for instance). When it
     * occurs, if the player is a sponsor, sends the <b>visible</b> stuff of this player through the Redis network,
     * one 5 ticks later.
     *
     * @param event The item changing event.
     */
    @Listener
    public void onItemHeldChanged(ChangeInventoryEvent.Held event) {
        Object rootCause = event.getCause().root();
        if (!(rootCause instanceof Player))
            return;
        Player player = (Player) rootCause;
        if(ReplicatePluginSponge.get().shouldBeReplicated(player))
            SpongeScheduler.scheduleTaskLater(() -> ReplicationManager.sendPlayerStuff(player), 5);
    }

}
