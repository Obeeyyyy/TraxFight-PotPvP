package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 22:49

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.utils.Bools;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalMuteCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "globalmute", true))
                return false;

            if(Bools.globalmute){
                Bools.globalmute = false;
                Bukkit.broadcastMessage(traxFight.getPrefix() + "Der Globalmute wurde von " + player.getName() + " deaktiviert.");

                for(Player all : Bukkit.getOnlinePlayers())
                    all.playSound(all.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
            }else{
                Bools.globalmute = true;
                Bukkit.broadcastMessage(traxFight.getPrefix() + "Der Globalmute wurde von " + player.getName() + " aktiviert.");

                for(Player all : Bukkit.getOnlinePlayers())
                    all.playSound(all.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
            }
        }
        return false;
    }
}
