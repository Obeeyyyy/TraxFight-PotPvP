package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 13:42

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;
            if(args.length == 1){
                if(traxFight.hasPermission(player, "gamemode", true)){
                    if(args[0].equalsIgnoreCase("1")) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(traxFight.getPrefix() + "Du bist nun im Creative Modus.");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                    }
                    if(args[0].equalsIgnoreCase("2")) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(traxFight.getPrefix() + "Du bist nun im Adventure Modus.");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                    }
                    if(args[0].equalsIgnoreCase("3")) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(traxFight.getPrefix() + "Du bist nun im Spectator Modus.");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                    }
                    if(args[0].equalsIgnoreCase("0")) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(traxFight.getPrefix() + "Du bist nun im Survival Modus.");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                    }
                }
            }else if(args.length == 2){
                final Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    if(traxFight.hasPermission(player, "gamemode.other", true)){
                        if(args[0].equalsIgnoreCase("1")) {
                            target.setGameMode(GameMode.CREATIVE);
                            target.sendMessage(traxFight.getPrefix() + "Du bist nun im Creative Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                            player.sendMessage(traxFight.getPrefix() + args[1] + " ist jetzt im Creative Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                        }
                        if(args[0].equalsIgnoreCase("2")) {
                            target.setGameMode(GameMode.ADVENTURE);
                            target.sendMessage(traxFight.getPrefix() + "Du bist nun im Adventure Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                            player.sendMessage(traxFight.getPrefix() + args[1] + " ist jetzt im Adventure Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                        }
                        if(args[0].equalsIgnoreCase("3")) {
                            target.setGameMode(GameMode.SPECTATOR);
                            target.sendMessage(traxFight.getPrefix() + "Du bist nun im Spectator Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                            player.sendMessage(traxFight.getPrefix() + args[1] + " ist jetzt im Spectator Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                        }
                        if(args[0].equalsIgnoreCase("0")) {
                            target.setGameMode(GameMode.SURVIVAL);
                            target.sendMessage(traxFight.getPrefix() + "Du bist nun im Survival Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                            player.sendMessage(traxFight.getPrefix() + args[1] + " ist jetzt im Survival Modus.");
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.1f, 0.1f);
                        }
                    }
                }
            }
        }
        return false;
    }
}
