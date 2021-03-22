package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:48

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCommand implements CommandExecutor {


    private final TraxFight traxFight = TraxFight.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "team", true))
                return false;

            if(args.length == 0){
                player.sendMessage(traxFight.getPrefix() + "/teamchat <nachricht>");
                return false;
            }

            String message = args[0];

            if(args.length > 0){
                for(short i = 1; i < args.length; i++)
                    message = message + " " + args[i];
            }

            message = ChatColor.translateAlternateColorCodes('&', message);

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(traxFight.hasPermission(all, "team", false))
                    all.sendMessage("§8(§c§lTeamchat§8) §7" + player.getName() + "§8 > §e" + message);
            }
        }
        return false;
    }
}
