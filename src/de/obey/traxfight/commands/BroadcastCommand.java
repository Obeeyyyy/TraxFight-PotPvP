package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        12.03.2021 | 11:35

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
            if(sender instanceof Player){
                final Player player =(Player) sender;

                if(!traxFight.hasPermission(player, "broadcast", true))
                    return false;

                if(args.length < 1){
                    player.sendMessage(traxFight.getPrefix() + "/broadcast <message>");
                    return false;
                }

                String message  = args[0];

                if(args.length > 0){
                    for(int i = 1; i < args.length; i++){
                        message = message  + " " + args[i];
                    }
                }

                message = ChatColor.translateAlternateColorCodes('&', message);

                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(traxFight.getPrefix() + message);
                Bukkit.broadcastMessage("");
            }
        return false;
    }
}
