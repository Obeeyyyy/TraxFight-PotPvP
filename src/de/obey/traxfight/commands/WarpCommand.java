package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        06.03.2021 | 20:33

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length > 0){
                player.sendMessage(traxFight.getPrefix() + "/warps");
                return false;
            }

            player.openInventory(traxFight.getWarpManager().getWarpInventory());
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
        }
        return false;
    }
}
