package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:35

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BuildCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();
    public static ArrayList<Player> build = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "build", true))
                return false;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/build <spieler>");
                return false;
            }

            if(args.length == 0){
                if(!build.contains(player)){
                    build.add(player);
                    player.sendMessage(traxFight.getPrefix() + "Du bist jetzt im Build Modus.");
                }else{
                    build.remove(player);
                    player.sendMessage(traxFight.getPrefix() + "Du bist jetzt nicht mehr Build Modus.");
                }
            }

            if(args.length == 1){
                final Player target = Bukkit.getPlayer(args[0]);

                if(target == null){
                    player.sendMessage(traxFight.getPrefix() + "Der Spieler " + args[0] + " ist nicht Online.");
                    return false;
                }

                if(!build.contains(target)){
                    build.add(target);
                    player.sendMessage(traxFight.getPrefix() + args[0] + " ist jetzt im Build Modus.");
                }else{
                    build.remove(target);
                    player.sendMessage(traxFight.getPrefix() + args[0] + " ist jetzt nicht mehr Build Modus.");
                }
            }

        }
        return false;
    }
}
