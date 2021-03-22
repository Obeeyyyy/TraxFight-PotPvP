package de.obey.traxfight.listener.custom;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 05:18

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import de.obey.traxfight.backend.UserFile;
import de.obey.traxfight.backend.events.UserChangeDataEvent;
import de.obey.traxfight.backend.events.UserLoadDataEvent;
import de.obey.traxfight.backend.events.UserReloadDataEvent;
import de.obey.traxfight.backend.events.UserSaveDataEvent;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public void on(UserChangeDataEvent event){
        final Player player = event.getUser().getPlayer();

        if(player == null)
            return;

        if(event.getUser() == null)
            return;

        if(event.getUser().getInteger("id") <= 0)
            return;

        traxFight.getScoreboarder().updateScoreboard(player);
    }

    @EventHandler
    public void on(UserLoadDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final User user = event.getUser();
        final String uuid = user.getOfflinePlayer().getUniqueId().toString();
        final int userID = simpleMySQL.getInt("users", "id", "uuid", uuid);

        if(userID == -111){
            final Player player = event.getUser().getPlayer();

            if(player == null)
                return;

            traxFight.getLoader().addPlayerCount();

            final int newUserID = traxFight.getLoader().getPlayercount();

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 4, 4);
            player.getInventory().clear();

            executorService.submit(() -> {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm / dd-MM-yyyy");

                simpleMySQL.execute("INSERT INTO users(UUID, NAME, ID, JOINDATE, JOINCOUNT) VALUES('" + player.getUniqueId().toString() + "', '" + player.getName() + "', '" + newUserID + "', '" + dateFormat.format(new Date()) + "', '" + newUserID + "')");
                simpleMySQL.execute("INSERT INTO userstats(ID, COINS, BOUNTY, KILLS, DEATHS, LIGAPOINTS, KILLSTREAK, KILLSTREAKREKORD) VALUES('" + newUserID + "', '500', '0', '0', '0', '100', '0', '0')");
                simpleMySQL.execute("INSERT INTO userplaytime(ID, DAYS, HOURS, MINUTES, SECONDS) " +
                        "VALUES('" + newUserID + "', '0', '0', '0', '0')");
                simpleMySQL.execute("INSERT INTO userkits(ID, PVP, POTION, TOOLS, BASE) VALUES('" + newUserID + "', '0', '0', '0', '0');");
                simpleMySQL.execute("INSERT INTO userclans(ID, CLANID) VALUES('" + newUserID + "', '-1')");

                load(user, newUserID);
            });

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getInventory().clear();
                    traxFight.getLocationManager().teleportToLocationInstant(player, traxFight.getLocationManager().getLocation("spawn"));
                    traxFight.getKitManager().equipFirstJoinKit(player);
                    Bukkit.broadcastMessage(traxFight.getPrefix() + "Der Spieler §8'§a" + player.getName() + "§8'§7 ist neu. §8(§7#§2§l" + newUserID + "§8)");
                }
            }.runTaskLater(traxFight, 20);

            return;
        }

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

            simpleMySQL.execute("UPDATE userclans SET clanid='" + user.getInteger("clanid") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userclans SET clanname='" + user.getClan().getString("name") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET coins='" + user.getLong("coins") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET bounty='" + user.getLong("bounty") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET kills='" + user.getInteger("kills") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET deaths='" + user.getInteger("deaths") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET ligapoints='" + user.getInteger("ligapoints") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET killstreak='" + user.getInteger("killstreak") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userstats SET killstreakrekord='" + user.getInteger("killstreakrekord") + "' WHERE ID='" + id + "';");

            simpleMySQL.execute("UPDATE userkits SET pvp='" + user.getLong("pvp_cooldown") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userkits SET potion='" + user.getLong("potion_cooldown") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userkits SET tools='" + user.getLong("tools_cooldown") + "' WHERE ID='" + id + "';");
            simpleMySQL.execute("UPDATE userkits SET base='" + user.getLong("base_cooldown") + "' WHERE ID='" + id + "';");

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

            user.getUserFile().getConfiguration().set("name", player.getName());
            user.getUserFile().getFile().saveFile();

            if(user.getClan() != null) {
                user.getClan().saveData();
            }else{
                traxFight.getSimpleMySQL().execute("UPDATE userclans SET clanid='-1' WHERE ID='" + user.getInteger("id) + '"));
                traxFight.getSimpleMySQL().execute("UPDATE userclans SET clanname='" + null + "' WHERE ID='" + user.getInteger("id) + '"));
            }
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
        user.setUserFile(new UserFile(traxFight.getDataFolder().getPath() + "/UserFiles/" + user.getOfflinePlayer().getUniqueId().toString()));

        if(user.getPlayer() != null && user.getPlayer().isOnline())
            user.setRang(traxFight.getRangManager().getPlayerRang(user.getPlayer()));

        if(simpleMySQL == null)
            return;

        final String id = String.valueOf(userID);

        user.setInteger("clanid", simpleMySQL.getInt("userclans", "clanid", "id", id));

        executorService.submit(() -> {

            user.setString("joindate", simpleMySQL.getString("users", "joindate", "id", id));

            user.setLong("coins", simpleMySQL.getLong("userstats", "coins", "id", id));
            user.setLong("bounty", simpleMySQL.getLong("userstats", "bounty", "id", id));
            user.setLong("pvp_cooldown", simpleMySQL.getLong("userkits", "pvp", "id", id));
            user.setLong("potion_cooldown", simpleMySQL.getLong("userkits", "potion", "id", id));
            user.setLong("tools_cooldown", simpleMySQL.getLong("userkits", "tools", "id", id));
            user.setLong("base_cooldown", simpleMySQL.getLong("userkits", "base", "id", id));
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

            user.setString("joinmessage", ChatColor.translateAlternateColorCodes('&', user.getUserFile().getConfiguration().getString("join.message")));
            user.setString("tabsuffix", ChatColor.translateAlternateColorCodes('&', user.getUserFile().getConfiguration().getString("tab.suffix")));
            user.setInteger("ecseiten", user.getUserFile().getConfiguration().getInt("ec.seiten"));

            if(user.getInteger("clanid") > 0)
                traxFight.getClanManager().loadClanByUser(user);
        });
    }
}
