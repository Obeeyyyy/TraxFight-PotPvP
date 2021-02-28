package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        14.02.2021 | 02:48

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){

            final Player player =(Player) sender;

            if(traxFight.getLocationManager().getSimpleFile() == null)
                traxFight.getLocationManager().loadLocations();

            if(args.length != 0){
                player.sendMessage(traxFight.getPrefix() + "/spawn");
                return false;
            }

            traxFight.getLocationManager().teleportToSpawn(player);
        }
        return false;
    }
}
