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
import org.bukkit.inventory.Inventory;

public class InvseeCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "invsee", true))
                return false;

            if(args.length != 1){
                player.sendMessage(traxFight.getPrefix() + "/invsee <spieler>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target, args[0]))
                return false;

            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);

            if(traxFight.hasPermission(player, "*", false)){
                player.openInventory(target.getInventory());
                return false;
            }

            if(player == target){
                player.sendMessage(traxFight.getPrefix() + "lmao");
                return false;
            }

            final Inventory inventory = Bukkit.createInventory(null, 9*4, "§aInv §7" + target.getName());
            inventory.setContents(target.getInventory().getContents());

            player.openInventory(inventory);

        }
        return false;
    }
}
