package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        15.03.2021 | 18:14

*/

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    final private ArrayList<String> returns = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] strings) {
        if (cmd.getName().equalsIgnoreCase("/pl")
                || cmd.getName().equalsIgnoreCase("/version")
                || cmd.getName().equalsIgnoreCase("/ver")
                || cmd.getName().equalsIgnoreCase("/?")
                || cmd.getName().startsWith("/bukkit")
                || cmd.getName().startsWith("/minecraft")
                || cmd.getName().equalsIgnoreCase("/")) {

            if(returns.size() == 0){
                returns.add("Lügen");
                returns.add("darf");
                returns.add("man");
                returns.add("nicht");
                returns.add("sagen");
            }

            Collections.sort(returns);

            return returns;
        }
        return null;
    }
}
