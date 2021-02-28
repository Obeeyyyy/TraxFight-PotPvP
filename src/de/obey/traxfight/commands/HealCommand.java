package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:41

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "heal", true))
                return false;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/heal <spieler>");
                return false;
            }

            if(args.length == 0){
                player.setHealth(20);
                player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 0.4f, 0.4f);
                Actionbar.sendActionBar(player, traxFight.getPrefix() + "Du wurdest gehealt.");
                return false;
            }

            if(!traxFight.hasPermission(player, "heal.other", true))
                return false;

            final Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(traxFight.getPrefix() + args[0] + " ist nicht Online.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                return false;
            }

            player.sendMessage(traxFight.getPrefix() + args[0] + " wurde gehealt.");

            target.setHealth(20);
            target.playSound(target.getLocation(), Sound.VILLAGER_HAGGLE, 0.4f, 0.4f);
            Actionbar.sendActionBar(target, traxFight.getPrefix() + "Du wurdest gehealt.");
        }
        return false;
    }
}
