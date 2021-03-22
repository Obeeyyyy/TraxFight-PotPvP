package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        10.03.2021 | 23:27

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTabSuffixCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "*", true))
                return false;

            if(args.length < 1){
                player.sendMessage(traxFight.getPrefix() + "/settabsuffix <spieler> <suffix>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target,args[0]))
                return false;

            String suffix = args[1];

            if(args.length > 1){
                for(int i = 2; i < args.length; i++)
                    suffix = suffix + " " + args[i];
            }

            suffix = ChatColor.translateAlternateColorCodes('&', suffix);

            User targetUser = traxFight.getUserManager().getUserFromPlayer(target);

            if(targetUser == null){
                player.sendMessage(traxFight.getPrefix() + "Ein Fehler ist aufgetreten.");
                return false;
            }

            targetUser.saveData();

            targetUser.getUserFile().getConfiguration().set("tab.suffix", suffix);
            targetUser.getUserFile().getFile().saveFile();

            player.sendMessage(traxFight.getPrefix() + "Tabsuffix für " + target.getName() + " geupdated.");

            targetUser.reloadData(target);

            traxFight.getScoreboarder().setPlayerListName(target);
        }
        return false;
    }
}
