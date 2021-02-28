package de.obey.traxfight;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 04:34

*/

import de.obey.traxfight.manager.*;
import de.obey.traxfight.usermanager.RangManager;
import de.obey.traxfight.usermanager.UserManager;
import de.obey.traxfight.utils.Scoreboarder;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraxFight extends JavaPlugin {

    private SimpleMySQL simpleMySQL;
    private ExecutorService executorService;

    private Loader loader;

    private UserManager userManager;
    private RangManager rangManager;
    private RankingManager rankingManager;
    private LocationManager locationManager;
    private LigaManager ligaManager;
    private Scoreboarder scoreboarder;
    private CombatManager combatManager;
    private KitManager kitManager;
    private KillFarmManager killFarmManager;

    public boolean restart = true;

    public void onEnable(){
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        executorService = Executors.newCachedThreadPool();

        loader = new Loader();

        userManager = new UserManager();
        rangManager = new RangManager();
        rankingManager = new RankingManager();
        ligaManager = new LigaManager();
        scoreboarder = new Scoreboarder();
        combatManager = new CombatManager();
        kitManager = new KitManager();
        killFarmManager = new KillFarmManager();

        loader.loadMysql();
        rangManager.createFolder(getDataFolder());

        new BukkitRunnable() {
            @Override
            public void run() {
                locationManager = new LocationManager();

                if(userManager != null)
                    userManager.loadAllUsers();

                locationManager.loadLocations();
                rangManager.setup(getDataFolder());

                restart = false;
            }
        }.runTaskLater(this, 40);
    }

    public void onDisable() {
        if(loader != null)
            loader.save();

        if(locationManager != null)
            locationManager.saveLocations();

        if(userManager != null)
            userManager.saveAllUsers();
    }

    public boolean hasPermission(Player player, String permission, boolean sendMessage){
        if(!player.hasPermission("tf." + permission)){
            if(sendMessage) {
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                player.sendMessage(getPrefix() + "Du darfst das nicht!");
            }
            return false;
        }
        return true;
    }

    public String getPrefix(){
        return "§8» §a§lTraxFight §8×§7 ";
    }

    public static TraxFight getInstance(){
        return getPlugin(TraxFight.class);
    }

    public SimpleMySQL getSimpleMySQL() {
        return simpleMySQL;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Loader getLoader() {
        return loader;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public RangManager getRangManager() {
        return rangManager;
    }

    public RankingManager getRankingManager() {
        return rankingManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public LigaManager getLigaManager() {
        return ligaManager;
    }

    public Scoreboarder getScoreboarder() {
        return scoreboarder;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public KillFarmManager getKillFarmManager() {
        return killFarmManager;
    }

    public void setSimpleMySQL(SimpleMySQL simpleMySQL) {
        this.simpleMySQL = simpleMySQL;
    }
}
