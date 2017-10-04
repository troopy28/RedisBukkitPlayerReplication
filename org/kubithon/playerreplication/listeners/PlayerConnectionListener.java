package org.kubithon.playerreplication.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kubithon.playerreplication.Main;

/**
 * Created by troopy28 on 20/02/2017.
 */
public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        if (Main.get().getReplicationParameters().isReplicable(e.getPlayer().getUniqueId()))
            Main.get().getReplicationMaster().enableReplication(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Main.get().getReplicationMaster().disableReplication(e.getPlayer());
    }
}
