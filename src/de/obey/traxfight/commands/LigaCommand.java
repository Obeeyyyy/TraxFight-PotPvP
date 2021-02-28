package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 09:25

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.objects.Liga;
import de.obey.utils.MathUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LigaCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(args.length != 0) {
            sender.sendMessage(traxFight.getPrefix() + "/liga");
            return false;
        }

        sender.sendMessage("§8§l§m----------------------------------");
        sender.sendMessage("§8                       §b§lLIGA");
        sender.sendMessage("");

        for(Liga liga : traxFight.getLigaManager().getLigen()){
            sender.sendMessage("§8(" + liga.getSmallPrefix() + "§8) » " + liga.getPrefix() + " : §f§oAb " + MathUtil.getLongWithDots(liga.getElo()) + " Elo");
            sender.sendMessage("");
        }

        sender.sendMessage("§8§l§m----------------------------------");

        return false;
    }
}
