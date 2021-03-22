package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:42

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "team", true))
                return false;

            if(args.length < 1){
                player.sendMessage(traxFight.getPrefix() + "/kick <spiler> <grund>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target, args[0]))
                return false;

            if(traxFight.hasPermission(target, "nokick", false)){
                player.sendMessage(traxFight.getPrefix() + "Du kannst " + target.getName() + " nicht kicken.");
                target.sendMessage(traxFight.getPrefix() + player.getName() + " hat versucht dich zu kicken.");
                return false;
            }

            String kickMessage = args[1];

            if(args.length > 1){
                for(int i = 2; i < args.length; i++){
                    kickMessage = kickMessage + " " + args[i];
                }
            }

            kickMessage = ChatColor.translateAlternateColorCodes('&', kickMessage);

            target.kickPlayer(kickMessage);
        }
        return false;
    }
}
