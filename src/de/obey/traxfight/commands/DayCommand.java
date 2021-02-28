package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:41

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "day", true))
                return false;

            if(args.length > 0){
                player.sendMessage(traxFight.getPrefix() + "/day");
                return false;
            }

            player.getWorld().setTime(1000);
            player.sendMessage(traxFight.getPrefix() + "Zeit auf Tag gestellt.");
        }
        return false;
    }
}
