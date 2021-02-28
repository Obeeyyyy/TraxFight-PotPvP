package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 08:11

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.User;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StatsCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/stats <name>");
                return false;
            }

            if(args.length == 0){
                final User user = traxFight.getUserManager().getUserFromPlayer(player);

                if(user == null)
                    return false;

                sendStatsMessage(player, user, player.getName());

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

                if (user != null){

                    sendStatsMessage(player, user, args[0]);

                    return false;
                }else {
                    user = traxFight.getUserManager().getUserFromOfflinePlayer(offlineTarget);
                }

                final User finalUser = user;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendStatsMessage(player, finalUser, args[0]);
                    }
                }.runTaskLater(traxFight, 10);
            }
        }
        return false;
    }

    private void sendStatsMessage(Player player, User user, String name){
        try {

            int id = user.getInteger("id");

            if(id == -111){
                player.sendMessage("Der Spieler §8'§a" + name + "§8'§7 spielt nicht auf unserem Server.");
                player.sendMessage("");
                player.sendMessage("§8§l§m----------------------------------");
                player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
                return;
            }

            player.sendMessage("§8§l§m----------------------------------");
            player.sendMessage("§8                       §a§lSTATS");
            player.sendMessage("");
            player.sendMessage("§8» §7Name§8:§a " + user.getOfflinePlayer().getName() + " §8┃ » §7ID§8:§a " + id);
            player.sendMessage("§8» §7LigaPunkte§8:§a " + MathUtil.getLongWithDots(user.getInteger("ligapoints")) + " §8┃ » §7Liga§8: §r" + traxFight.getLigaManager().getLigaFromPoints(user.getInteger("ligapoints")).getPrefix());
            player.sendMessage("§8» §7Kills§8: §a" + MathUtil.getLongWithDots(user.getInteger("kills")) + " §8┃ » §7Tode§8: §c" + MathUtil.getLongWithDots(user.getInteger("deaths")) + " §8┃ » §7Coins§8: §e" + MathUtil.getLongWithDots(user.getLong("coins")));
            player.sendMessage("§8» §7Killstreak§8: §3" + MathUtil.getLongWithDots(user.getInteger("killstreak")) + " §8┃ » §7Killstreakrekord§8: §3" + MathUtil.getLongWithDots(user.getInteger("killstreakrekord")) + " §8┃ » §7Kopfgeld§8: §e" + MathUtil.getLongWithDots(user.getLong("bounty")));
            player.sendMessage("§8» §7Beigetreten§8: §a" + user.getString("joindate"));
            player.sendMessage("");
            player.sendMessage("§8§l§m----------------------------------");

        }catch (NullPointerException ex){
            player.sendMessage("Der Spieler §8'§a" + name + "§8'§7 spielt nicht auf unserem Server.");
            player.sendMessage("");
            player.sendMessage("§8§l§m----------------------------------");
            player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
        }
    }
}
