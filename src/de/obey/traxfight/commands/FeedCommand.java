package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 13:43

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (args.length > 1) {
                player.sendMessage(traxFight.getPrefix() + "/feed <name>");
                return false;
            }

            if (args.length == 0) {
                player.setFoodLevel(20);
                player.playSound(player.getLocation(), Sound.BURP, 0.4f, 0.4f);

                Actionbar.sendActionBar(player, traxFight.getPrefix() + "Dein Hunger wurde gestillt.");

            } else if (args.length == 1) {
                if (!traxFight.hasPermission(player, "feed.other", true))
                    return false;

                final Player target = Bukkit.getPlayer(args[0]);

                if(!traxFight.isOnline(player, target, args[0]))
                    return false;

                target.setFoodLevel(20);
                player.sendMessage(traxFight.getPrefix() + args[0] + " wurde gefüttert.");
                Actionbar.sendActionBar(target, traxFight.getPrefix() + "Dein Hunger wurde gestillt.");
            }
        }
        return false;
    }
}
