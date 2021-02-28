package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 05:18

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.User;
import de.obey.traxfight.usermanager.events.UserLoadDataEvent;
import de.obey.traxfight.usermanager.events.UserReloadDataEvent;
import de.obey.traxfight.usermanager.events.UserSaveDataEvent;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class UserListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private ExecutorService executorService;
    private SimpleMySQL simpleMySQL;

    @EventHandler
    public void on(UserLoadDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final String uuid = event.getUser().getOfflinePlayer().getUniqueId().toString();

            int userID = simpleMySQL.getInt("users", "id", "uuid", uuid);

        if(userID == -111){
            final Player player = event.getUser().getPlayer();

            if(player == null)
                return;

            traxFight.getLoader().addPlayerCount();

            final int newUserID = traxFight.getLoader().getPlayercount();

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);

            executorService.submit(() -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm / dd-MM-yyyy");

                simpleMySQL.execute("INSERT INTO users(UUID, NAME, ID, JOINDATE, JOINCOUNT) VALUES('" + player.getUniqueId().toString() + "', '" + player.getName() + "', '" + newUserID + "', '" + dateFormat.format(new Date()) + "', '" + newUserID + "')");
                simpleMySQL.execute("INSERT INTO userstats(ID, COINS, BOUNTY, KILLS, DEATHS, LIGAPOINTS, KILLSTREAK, KILLSTREAKREKORD) VALUES('" + newUserID + "', '500', '0', '0', '0', '100', '0', '0')");
                simpleMySQL.execute("INSERT INTO userplaytime(ID, DAYS, HOURS, MINUTES, SECONDS) " +
                        "VALUES('" + newUserID + "', '0', '0', '0', '0')");

                final User user = event.getUser();

                load(user, newUserID);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        traxFight.getLocationManager().teleportToSpawnInstant(player);
                    }
                }.runTaskLater(traxFight, 10);

                Bukkit.broadcastMessage(traxFight.getPrefix() + "Der Spieler §8'§a" + player.getName() + "§8'§7 ist neu. §8(§7#§2§l" + newUserID + "§8)");
            });

            return;
        }

        final User user = event.getUser();
        load(user, userID);
    }

    @EventHandler
    public void on(UserSaveDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final Player player = event.getUser().getPlayer();

        executorService.submit(() -> {
            simpleMySQL.execute("UPDATE users SET name='" + player.getName() + "' WHERE UUID='" + player.getUniqueId().toString() + "';");

            final User user = event.getUser();

            int id = user.getInteger("id");

            simpleMySQL.execute("UPDATE userstats SET coins='" + user.getLong("coins") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET bounty='" + user.getLong("bounty") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET kills='" + user.getInteger("kills") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET deaths='" + user.getInteger("deaths") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET ligapoints='" + user.getInteger("ligapoints") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET killstreak='" + user.getInteger("killstreak") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET killstreakrekord='" + user.getInteger("killstreakrekord") + "' WHERE ID='" + id + "';");

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

            traxFight.getSimpleMySQL().execute("UPDATE userplaytime SET seconds='" + onlineSeconds + "' WHERE ID='" + id +"'");
            traxFight.getSimpleMySQL().execute("UPDATE userplaytime SET minutes='" + onlineMinutes + "' WHERE ID='" + id +"'");
            traxFight.getSimpleMySQL().execute("UPDATE userplaytime SET hours='" + onlineHours + "' WHERE ID='" + id +"'");
            traxFight.getSimpleMySQL().execute("UPDATE userplaytime SET days='" + onlineDays + "' WHERE ID='" + id +"'");

            user.setLong("joinmillis", System.currentTimeMillis());
        });

        traxFight.getScoreboarder().updateAll();
    }

    @EventHandler
    public void on(UserReloadDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final User user = event.getUser();

        final int userID = simpleMySQL.getInt("users", "id", "uuid", user.getPlayer().getUniqueId().toString());

        load(user, userID);
    }

    private void load(User user, int userID){
        executorService.submit(() -> {
            final String id = String.valueOf(userID);

            user.setString("joindate", simpleMySQL.getString("users", "joindate", "id", id));

            user.setLong("coins", simpleMySQL.getLong("userstats", "coins", "id", id));
            user.setLong("bounty", simpleMySQL.getLong("userstats", "bounty", "id", id));
            user.setLong("joinmillis", System.currentTimeMillis());

            user.setInteger("seconds", simpleMySQL.getInt("userplaytime", "seconds", "id",id));
            user.setInteger("minutes", simpleMySQL.getInt("userplaytime", "minutes", "id", id));
            user.setInteger("hours", simpleMySQL.getInt("userplaytime", "hours", "id", id));
            user.setInteger("days", simpleMySQL.getInt("userplaytime", "days", "id", id));

            user.setInteger("id", userID);
            user.setInteger("kills", simpleMySQL.getInt("userstats", "kills", "id", id));
            user.setInteger("deaths", simpleMySQL.getInt("userstats", "deaths", "id", id));
            user.setInteger("ligapoints", simpleMySQL.getInt("userstats", "ligapoints", "id", id));
            user.setInteger("killstreak", simpleMySQL.getInt("userstats", "killstreak", "id", id));
            user.setInteger("killstreakrekord", simpleMySQL.getInt("userstats", "killstreakrekord", "id", id));
        });

        if(user.getPlayer() != null && user.getPlayer().isOnline())
            user.setRang(traxFight.getRangManager().getPlayerRang(user.getPlayer()));
    }
}
