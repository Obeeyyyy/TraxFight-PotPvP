package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        28.02.2021 | 08:22

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankingCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;

            if(args.length != 0){
                player.sendMessage(traxFight.getPrefix() + "/ranking");
                return false;
            }

            try {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("kills"));
            }catch (NullPointerException ex){
                player.sendMessage(traxFight.getPrefix() + "Bitte warte einen Moment.");
            }
        }
        return false;
    }
}
