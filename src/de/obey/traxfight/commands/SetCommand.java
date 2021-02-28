package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        14.02.2021 | 02:30

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){

            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "*", true))
                return false;

            if(args.length != 1){
                player.sendMessage(traxFight.getPrefix() + "/set <locationName>");
                return false;
            }

            traxFight.getLocationManager().setLocation(args[0], player.getLocation());
            player.sendMessage(traxFight.getPrefix() + "Location '" + args[0] + "' gesetzt.");
        }
        return false;
    }
}
