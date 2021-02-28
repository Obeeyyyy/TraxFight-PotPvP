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

public class ClearCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "clear", true))
                return false;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/clear <spieler>");
                return false;
            }

            if(args.length == 0){
                player.getInventory().clear();
                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);

                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 0.4f, 0.4f);

                return false;
            }

            if(!traxFight.hasPermission(player, "clear.other", true))
                return false;

            final Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(traxFight.getPrefix() + args[0] + " ist nicht Online.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                return false;
            }

            player.sendMessage(traxFight.getPrefix() + "Inventar von " + args[0] + " gecleart.");

            target.getInventory().clear();
            target.getInventory().setHelmet(null);
            target.getInventory().setChestplate(null);
            target.getInventory().setLeggings(null);
            target.getInventory().setBoots(null);

            target.playSound(target.getLocation(), Sound.HORSE_ARMOR, 0.4f, 0.4f);

        }
        return false;
    }
}
