package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:41

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatclearCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "team", true))
                return false;

            if(args.length > 0){
                player.sendMessage(traxFight.getPrefix() + "/chatclear");
                return false;
            }

            for(int i = 0; i < 200; i++){
                for(Player all : Bukkit.getOnlinePlayers()){
                    if(!traxFight.hasPermission(all, "team", false))
                        all.sendMessage(" ");
                }
            }

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("                              §a§lCHATCLEAR");
            Bukkit.broadcastMessage("            §7§lDer Chat wurde von " + player.getName() + " gecleart");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");

        }
        return false;
    }
}
