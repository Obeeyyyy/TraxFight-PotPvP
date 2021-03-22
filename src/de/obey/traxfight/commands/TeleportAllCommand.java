package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:49

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAllCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "tpall", true))
                return false;

            if(args.length > 0){
                player.sendMessage(traxFight.getPrefix() + "/teleportall");
                return false;
            }

            for(Player all : Bukkit.getOnlinePlayers())
                all.teleport(player);

        }
        return false;
    }
}
