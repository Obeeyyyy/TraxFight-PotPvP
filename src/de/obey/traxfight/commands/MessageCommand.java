package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        12.03.2021 | 18:50

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();
    private Map<Player, Player> messages = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;

            if(cmd.getName().equalsIgnoreCase("message")){

                if(args.length < 2){
                    player.sendMessage(traxFight.getPrefix() + "/message <spieler> <nachricht>");
                    return false;
                }

                final Player target = Bukkit.getPlayer(args[0]);

                if(!traxFight.isOnline(player, target, args[0]))
                    return false;

                if(target == player){
                    player.sendMessage(traxFight.getPrefix() + "Lügen darf man nicht sagen.");
                    return false;
                }

                String message = args[1];

                if(args.length > 2) {
                    for (int i = 2; i < args.length; i++)
                        message = message + " " + args[i];
                }

                if(traxFight.hasPermission(player, "colormsg", false))
                    message = ChatColor.translateAlternateColorCodes('&', message);

                target.sendMessage("§8(§c§lMSG§8)§7 " + player.getName() + " zu dir §8»§f " + message);
                player.sendMessage("§8(§c§lMSG§8)§7 du zu " + target.getName() + "§8 »§f " + message);

                messages.put(player, target);
                messages.put(target, player);

                return false;
            }

            if(cmd.getName().equalsIgnoreCase("respond")){

                if(args.length < 1){
                    player.sendMessage(traxFight.getPrefix() + "/respond <nachricht>");
                    return false;
                }

                final Player target = messages.get(player);

                if(target == null){
                    player.sendMessage(traxFight.getPrefix() + "Du kannst niemandem Antworten.");
                    return false;
                }

                if(!traxFight.isOnline(player, target, args[0]))
                    return false;

                if(target == player){
                    player.sendMessage(traxFight.getPrefix() + "Lügen darf man nicht sagen.");
                    return false;
                }

                String message = args[0];

                if(args.length > 1) {
                    for (int i = 1; i < args.length; i++)
                        message = message + " " + args[i];
                }

                if(traxFight.hasPermission(player, "colormsg", false))
                    message = ChatColor.translateAlternateColorCodes('&', message);

                target.sendMessage("§8(§c§lMSG§8)§7 " + player.getName() + " zu dir §8»§f " + message);
                player.sendMessage("§8(§c§lMSG§8)§7 du zu " + target.getName() + "§8 »§f " + message);

                messages.put(player, target);
                messages.put(target, player);
                return false;
            }
        }
        return false;
    }
}
