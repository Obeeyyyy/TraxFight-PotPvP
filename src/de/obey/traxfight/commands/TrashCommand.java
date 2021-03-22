package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:48

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCommand implements CommandExecutor {

    private final TraxFight traxfight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;

            if(args.length > 0){
                player.sendMessage(traxfight.getPrefix() + "/trash");
                return false;
            }

            player.openInventory(Bukkit.createInventory(null, 9*6, "§a§lTrash"));
        }
        return false;
    }
}
