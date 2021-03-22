package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        05.03.2021 | 17:42

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length > 0){
                player.sendMessage(traxFight.getPrefix() + "/kit");
                return false;
            }

            final User user = traxFight.getUserManager().getUserFromPlayer(player);

            traxFight.getKitManager().updateKitInventory(user);

            player.openInventory(user.getInventory("kit"));
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
        }
        return false;
    }
}
