package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        13.02.2021 | 04:50

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.Rang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RangCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "*", true))
                return false;

            if(args.length != 2){
                player.sendMessage(traxFight.getPrefix() + "/rang <spieler> <rang>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(target == null || !target.isOnline()){
                player.sendMessage(traxFight.getPrefix() + "Der Spieler " + args[0] + " ist nicht Online.");
                return false;
            }

            final Rang rang = traxFight.getRangManager().getRangMap().get(args[1]);

            if(rang == null){
                player.sendMessage(traxFight.getPrefix() + "Der Rang " + args[1] + " existiert nicht.");
                return false;
            }

            PermissionsEx.getUser(target).setGroups(new String[]{rang.getRangName()});

            player.sendMessage(traxFight.getPrefix() + "Der Spieler " + args[0] + " hat jetzt den " + args[1] + " Rang.");
            target.getMetadata(traxFight.getPrefix() + "Du hast den " + args[1] + " Rang erhalten.");

            traxFight.getUserManager().getUserFromPlayer(target).setRang(rang);

            traxFight.getScoreboarder().updateAll();
        }
        return false;
    }
}
