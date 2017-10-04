package org.kubithon.replicate.listener;

import org.kubithon.replicate.ReplicatePluginSponge;
import org.kubithon.replicate.replication.ReplicationManager;
import org.kubithon.replicate.util.SpongeScheduler;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

import java.util.Optional;

/**
 * Creation: 10/09/2017.
 *
 * @author troopy28
 * @since 1.0.0
 */
public class SpongeInventoryClickListener {

    @Listener
    public void onInventoryClick(ClickInventoryEvent event) {

        Transaction<ItemStackSnapshot> clickTransaction = event.getCursorTransaction();
        Slot slot = event.getTransactions().get(0).getSlot();


        Optional<Inventory> inventoryOptional = event.getCause().first(Inventory.class);
        if (!inventoryOptional.isPresent())
            return;

        Inventory inventory = inventoryOptional.get();
        if (!(inventory instanceof CarriedInventory))
            return; // The inventory doesn't belong to an entity

        CarriedInventory carriedInventory = (CarriedInventory) inventory;
        Optional carrierOp = carriedInventory.getCarrier();
        if (!carrierOp.isPresent())
            return; // No carrier

        Object rawCarrier = carrierOp.get();
        if (!(rawCarrier instanceof Player))
            return; // Not a player

        Player player = (Player) rawCarrier;
        if(!ReplicatePluginSponge.get().shouldBeReplicated(player))
            return; // No replication for this player

        SpongeScheduler.scheduleTaskLater(() -> ReplicationManager.sendPlayerStuff(player), 5);
    }

    private boolean isArmorSlot(int slot) {
        return slot <= 8 && slot >= 5;
    }

    private boolean isVisibleInventorySlot(int slot) {
        return slot <= 44 && slot >= 36;
    }
}