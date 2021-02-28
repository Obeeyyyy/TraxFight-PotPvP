package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 20:50

*/

import de.obey.traxfight.Loader;
import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(traxFight.hasPermission(player, "*", true)){
                final Loader loader = traxFight.getLoader();

                if(traxFight.getLoader().whitelist()) {
                    traxFight.getLoader().setWhitelist(false);
                    player.sendMessage(traxFight.getPrefix() + "Du hast die Whitelist deaktiviert.");

                    for(Player all : Bukkit.getOnlinePlayers()){
                        if(!traxFight.hasPermission(all, "team", false))
                            all.kickPlayer(traxFight.getPrefix() + "Die Whitelist wurde aktiviert.");
                    }

                }else{
                    traxFight.getLoader().setWhitelist(true);
                    player.sendMessage(traxFight.getPrefix() + "Du hast die Whitelist aktiviert.");
                }

            }

        }
        return false;
    }
}
