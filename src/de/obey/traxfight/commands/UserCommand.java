package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 23:52

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import de.obey.traxfight.backend.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class UserCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();
    private UserManager userManager;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(userManager == null)
            userManager = traxFight.getUserManager();

        if(!sender.hasPermission("*"))
            return false;

        if (args.length == 1 && args[0].equalsIgnoreCase("rlrang")) {
            traxFight.getRangManager().getRangMap().clear();
            traxFight.getRangManager().setup(traxFight.getDataFolder());
            traxFight.getScoreboarder().getTeams().clear();
            sender.sendMessage(traxFight.getPrefix() + "Ränge erfolgreich neu geladen.");
            return false;
        }

        if(args.length != 2) {
            sender.sendMessage(traxFight.getPrefix() + "/user <save/reload> <player>");
            return false;
        }

        final Player target = Bukkit.getPlayer(args[1]);

        if(target == null){
            sender.sendMessage(traxFight.getPrefix() + "Der Spieler " + args[1] + " ist nicht Online!");
            return false;
        }

        final User user = userManager.getUserFromPlayer(target);

        if(args[0].equalsIgnoreCase("save")){
            user.saveData();
            sender.sendMessage(traxFight.getPrefix() + "User (" + target.getName() + ") gespeichert.");

        } else if (args[0].equalsIgnoreCase("reload")) {
            user.setClan(null);
            user.loadData();
            sender.sendMessage(traxFight.getPrefix() + "User (" + target.getName() + ") neugeladen.");
        }

        return false;
    }
}
