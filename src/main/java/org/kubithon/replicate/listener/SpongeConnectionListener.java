package org.kubithon.replicate.listener;

import org.kubithon.replicate.ReplicatePluginSponge;
import org.kubithon.replicate.netty.ReplicateHandler;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.LiteralText;

/**
 * Created by troopy28 on 08/09/2017.
 */
public class SpongeConnectionListener {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        if (ReplicatePluginSponge.get().shouldBeReplicated(player))
            ReplicateHandler.handle(player);

        // Test
        final Entity ent = player.getLocation().getExtent().createEntity(EntityTypes.HUMAN, player.getLocation().getPosition());
        player.getWorld().spawnEntity(ent, Cause.of(NamedCause.owner(ReplicatePluginSponge.get())));
        player.sendMessage(LiteralText.of("Hey !"));
    }
}