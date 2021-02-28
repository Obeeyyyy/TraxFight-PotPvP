package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:49

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VanishCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();
    public static final ArrayList<Player> vanished = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "team", true))
                return false;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/vanish <spieler>");
                return false;
            }

            if(args.length == 0){
                if(vanished.contains(player)){
                    vanished.remove(player);

                    player.sendMessage(traxFight.getPrefix() + "Du bist jetzt wieder sichtbar.");
                    traxFight.getScoreboarder().updateAll();
                    Bukkit.broadcastMessage("§8[§a+§8] §7" + player.getName());

                    for(Player all : Bukkit.getOnlinePlayers())
                        all.showPlayer(player);

                }else{
                    vanished.add(player);

                    player.sendMessage(traxFight.getPrefix() + "Du bist jetzt unsichtbar.");
                    traxFight.getScoreboarder().updateAll();
                    Bukkit.broadcastMessage("§8[§c-§8] §7" + player.getName());

                    for(Player all : Bukkit.getOnlinePlayers()) {
                        if(!traxFight.hasPermission(all, "team", false)) {
                            all.hidePlayer(player);
                        }
                    }
                }
                return false;
            }

            if(!traxFight.hasPermission(player, "vanish.other", true))
                return false;

            final Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(traxFight.getPrefix() + args[0] + " ist nicht Online.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                return false;
            }

            if(vanished.contains(target)){
                vanished.remove(target);

                player.sendMessage(traxFight.getPrefix() + target.getName() + " ist jetzt wieder sichtbar.");
                target.sendMessage(traxFight.getPrefix() + "Du bist jetzt wieder sichtbar.");
                traxFight.getScoreboarder().updateAll();

                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.showPlayer(target);
                }

                Bukkit.broadcastMessage("§8[§a+§8] §7" + target.getName());

            }else{
                vanished.add(target);

                player.sendMessage(traxFight.getPrefix() + target.getName() + " ist unsichtbar.");
                target.sendMessage(traxFight.getPrefix() + "Du bist jetzt unsichtbar.");
                traxFight.getScoreboarder().updateAll();

                for(Player all : Bukkit.getOnlinePlayers()) {
                    if (!traxFight.hasPermission(all, "team", false)) {
                        all.hidePlayer(target);
                    }
                }

                Bukkit.broadcastMessage("§8[§c-§8] §7" + target.getName());
            }

        }
        return false;
    }
}
