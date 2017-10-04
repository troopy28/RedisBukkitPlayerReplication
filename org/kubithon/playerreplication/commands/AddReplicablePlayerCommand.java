package org.kubithon.playerreplication.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kubithon.playerreplication.Main;

/**
 * Created by troopy28 on 20/02/2017.
 */
public class AddReplicablePlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return true;
        String pName = args[0];
        Player p = Bukkit.getPlayer(pName);
        Main.get().getReplicationMaster().addReplicablePlayer(p.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "The player " + p.getDisplayName() + " is now replicated.");
        return true;
    }
}
