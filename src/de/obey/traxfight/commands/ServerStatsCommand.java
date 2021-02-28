package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        07.02.2021 | 01:15

*/

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.NumberFormat;

public class ServerStatsCommand implements CommandExecutor {

    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("*")) {

            NumberFormat numberFormat = NumberFormat.getInstance();

            long maxMem = runtime.maxMemory() / 1024 / 1024;
            long allocatedMem = runtime.totalMemory() / 1024 / 1024;
            long freeMemory = runtime.freeMemory() / 1024 / 1024;

            sender.sendMessage("§8§l§m----------------------");
            sender.sendMessage("§8");
            sender.sendMessage("§8  * §7 RAM Information");
            sender.sendMessage("§8    §8 > §f Max§8: §e" + numberFormat.format(maxMem) + " MB");
            sender.sendMessage("§8    §8 > §f Allocated§8: §e" + numberFormat.format(allocatedMem) + " MB");
            sender.sendMessage("§8    §8 > §f Free§8: §e" + numberFormat.format(maxMem - allocatedMem) + " MB");
            sender.sendMessage("§8    §8 > §f Used§8: §e" + numberFormat.format(freeMemory) + " MB");
            sender.sendMessage("§8");
            sender.sendMessage("§8§l§m----------------------");
        }
        return false;
    }
}
