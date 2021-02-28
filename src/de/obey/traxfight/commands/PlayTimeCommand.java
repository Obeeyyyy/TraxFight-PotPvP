package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 08:48

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayTimeCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();
    private final Map<User, Long> lastUpdate = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/playtime <name>");
                return false;
            }

            if(args.length == 0){
                final User user = traxFight.getUserManager().getUserFromPlayer(player);

                if(user == null)
                    return false;

                updatePlayTime(user);

                player.sendMessage(traxFight.getPrefix() + "Deine aktuelle Spielzeit§8: §a" + user.getInteger("days") + "§f Tag(e) §a" + user.getInteger("hours") + "§f Stunde(n) §a" + user.getInteger("minutes") + "§f Minute(n) §a" + user.getInteger("seconds") + "§f Sekunde(n)");

                return false;
            }

            if(args.length == 1) {
                final Player target = Bukkit.getPlayer(args[0]);

                User user = null;

                if (target != null)
                    user = traxFight.getUserManager().getUserFromPlayer(target);

                final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);

                if (offlineTarget == null) {
                    player.sendMessage(traxFight.getPrefix() + "Der Spieler §8'§a" + args[0] + "§8'§7 spielt nicht auf unserem Server.");
                    return false;
                }

                user = traxFight.getUserManager().getUserFromUUID(offlineTarget.getUniqueId());

                if (user == null)
                    user = traxFight.getUserManager().getUserFromOfflinePlayer(offlineTarget);

                final User finalUser = user;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {

                            if(finalUser.getPlayer() != null && finalUser.getPlayer().isOnline())
                                updatePlayTime(finalUser);

                            player.sendMessage(traxFight.getPrefix() + "§8'§a" + args[0] + "§8' §7Spielzeit§8: §a" + finalUser.getInteger("days") + "§f Tag(e) §a" + finalUser.getInteger("hours") + "§f Stunde(n) §a" + finalUser.getInteger("minutes") + "§f Minute(n) §a" + finalUser.getInteger("seconds") + "§f Sekunde(n)");

                        } catch (NullPointerException exception) {
                            player.sendMessage(traxFight.getPrefix() + "Der Spieler §8'§a" + args[0] + "§8'§7 spielt nicht auf unserem Server.");
                            player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
                        }
                    }
                }.runTaskLater(traxFight, 5);
            }
        }
        return false;
    }

    private void updatePlayTime(User user){

        if(lastUpdate.containsKey(user.getPlayer())){
            if(System.currentTimeMillis() - lastUpdate.get(user.getPlayer()) < 10000)
                return;
        }

        lastUpdate.put(user, System.currentTimeMillis());

        long onlineMillis = System.currentTimeMillis() - user.getLong("joinmillis");

        int onlineSeconds = user.getInteger("seconds");
        int onlineMinutes = user.getInteger("minutes");
        int onlineHours = user.getInteger("hours");
        int onlineDays = user.getInteger("days");

        while(onlineMillis >= 1000){
            onlineMillis -= 1000;
            onlineSeconds++;
        }

        while(onlineSeconds >= 60){
            onlineSeconds -= 60;
            onlineMinutes++;
        }

        while(onlineMinutes >= 60){
            onlineMinutes -= 60;
            onlineHours++;
        }

        while(onlineHours >= 24){
            onlineHours -= 24;
            onlineDays++;
        }

        user.setInteger("seconds", onlineSeconds);
        user.setInteger("minutes", onlineMinutes);
        user.setInteger("hours", onlineHours);
        user.setInteger("days", onlineDays);

        user.setLong("joinmillis", System.currentTimeMillis());
    }
}
