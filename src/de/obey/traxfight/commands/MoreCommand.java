package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:43

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoreCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (!traxFight.hasPermission(player, "*", true))
                return false;

            if (args.length > 0) {
                player.sendMessage(traxFight.getPrefix() + "/more");
                return false;
            }

            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                player.getItemInHand().setAmount(64);
                player.setItemInHand(player.getItemInHand());
                player.playSound(player.getLocation(), Sound.DRINK, 3, 3);
            } else {
                player.sendMessage(traxFight.getPrefix() + "Du musst ein Item in der Hand halten.");
            }

        }
        return false;
    }
}
