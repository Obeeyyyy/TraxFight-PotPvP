package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:41

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "fly", true))
                return false;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/fly <spieler>");
                return false;
            }

            if(args.length == 0){
                if(player.getAllowFlight()){
                    player.setAllowFlight(false);
                    player.sendMessage(traxFight.getPrefix() + "Flugmodus deaktiviert.");
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.4f, 0.4f);
                }else{
                    player.setAllowFlight(true);
                    player.sendMessage(traxFight.getPrefix() + "Flugmodus aktiviert.");
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.4f, 0.4f);
                }
                return false;
            }

            if(!traxFight.hasPermission(player, "fly.other", true))
                return false;

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target, args[0]))
                return false;

            if(target.getAllowFlight()){
                target.setAllowFlight(false);
                target.sendMessage(traxFight.getPrefix() + "Flugmodus deaktiviert.");
                player.sendMessage(traxFight.getPrefix() + args[0] + " kann jetzt nicht mehr fliegen.");
                target.playSound(target.getLocation(), Sound.ENDERDRAGON_WINGS, 0.4f, 0.4f);
            }else{
                target.setAllowFlight(true);
                target.sendMessage(traxFight.getPrefix() + "Flugmodus aktiviert.");
                player.sendMessage(traxFight.getPrefix() + args[0] + " kann jetzt fliegen.");
                target.playSound(target.getLocation(), Sound.ENDERDRAGON_WINGS, 0.4f, 0.4f);
            }

        }
        return false;
    }
}
