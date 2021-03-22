package de.obey.traxfight;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 05:10

*/

import de.obey.traxfight.commands.*;
import de.obey.traxfight.listener.*;
import de.obey.traxfight.listener.custom.ClanListener;
import de.obey.traxfight.listener.custom.RegionStuffListener;
import de.obey.traxfight.listener.custom.UserListener;
import de.obey.traxfight.listener.inventoryclick.KitInventoryClickListener;
import de.obey.traxfight.listener.inventoryclick.RankingInventoryClickListener;
import de.obey.traxfight.listener.inventoryclick.TeamInventoryClickListener;
import de.obey.traxfight.listener.inventoryclick.WarpInventoryClickListener;
import de.obey.utils.SimpleFile;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.File;

public class Loader {

    private final TraxFight traxFight = TraxFight.getInstance();
    private SimpleFile dataFile;
    private int playercount;
    private int clancount;
    private boolean whitelist;

    public Loader(){
        dataFile = new SimpleFile(traxFight.getDataFolder().getPath() + "/data.yml");

        if(!dataFile.exists()) {
            dataFile.createFile();
            dataFile.getConfiguration().set("playercount", 0);
            dataFile.getConfiguration().set("clancount", 0);
            dataFile.getConfiguration().set("whitelist", true);
            dataFile.getConfiguration().set("mysql.username", "changeme");
            dataFile.getConfiguration().set("mysql.database", "changeme");
            dataFile.getConfiguration().set("mysql.password", "changeme");
            dataFile.getConfiguration().set("mysql.host", "changeme");
            dataFile.saveFileAsync();
        }

        final File clanFolder = new File(traxFight.getDataFolder().getPath() + "/Clans");

        if(!clanFolder.exists())
            clanFolder.mkdir();

        playercount = dataFile.getConfiguration().getInt("playercount");
        whitelist = dataFile.getConfiguration().getBoolean("whitelist");

        loadCommands();
        loadListener();
    }

    public void save(){
        if(dataFile.exists()) {
            dataFile.getConfiguration().set("playercount", playercount);
            dataFile.getConfiguration().set("clancount", clancount);
            dataFile.saveFileAsync();
        }
    }

    public void loadMysql(){
        final YamlConfiguration cfg = dataFile.getConfiguration();

        traxFight.getExecutorService().submit(() -> {
            traxFight.setSimpleMySQL(new SimpleMySQL(cfg.getString("mysql.username"), cfg.getString("mysql.password"), cfg.getString("mysql.database"), cfg.getString("mysql.host")));

            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS users(UUID text, NAME text, ID int, JOINDATE text, JOINCOUNT text);");
            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS userstats(ID int, COINS bigint, BOUNTY bigint, KILLS int, DEATHS int, LIGAPOINTS int, KILLSTREAK int, KILLSTREAKREKORD int);");
            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS userplaytime(ID int, DAYS int, HOURS int, MINUTES int, SECONDS int);");
            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS userlevel(ID int, LEVEL int, XP int);");
            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS userkits(ID int, PVP bigint, POTION bigint, TOOLS bigint, BASE bigint);");

            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS userclans(ID text, CLANID int, CLANNAME text);");
            traxFight.getSimpleMySQL().execute("CREATE TABLE IF NOT EXISTS clanstats(ID int, NAME text, TAG text, LEADERUUID text, CREATED text, KILLS int, DEATHS int, SLOTS int, BANK bigint, CLANCOLOR text);");
        });
    }

    private void loadCommands(){
        traxFight.getCommand("bounty").setExecutor(new BountyCommand());
        traxFight.getCommand("broadcast").setExecutor(new BroadcastCommand());
        traxFight.getCommand("build").setExecutor(new BuildCommand());
        traxFight.getCommand("chatclear").setExecutor(new ChatclearCommand());
        traxFight.getCommand("clan").setExecutor(new ClanCommand());
        traxFight.getCommand("clear").setExecutor(new ClearCommand());
        traxFight.getCommand("day").setExecutor(new DayCommand());
        traxFight.getCommand("feed").setExecutor(new FeedCommand());
        traxFight.getCommand("fix").setExecutor(new FixCommand());
        traxFight.getCommand("fly").setExecutor(new FlyCommand());
        traxFight.getCommand("gamemode").setExecutor(new GameModeCommand());
        traxFight.getCommand("globalmute").setExecutor(new GlobalMuteCommand());
        traxFight.getCommand("heal").setExecutor(new HealCommand());
        traxFight.getCommand("invsee").setExecutor(new InvseeCommand());
        traxFight.getCommand("kick").setExecutor(new KickCommand());
        traxFight.getCommand("kit").setExecutor(new KitCommand());
        traxFight.getCommand("kitreset").setExecutor(new KitResetCommand());
        traxFight.getCommand("liga").setExecutor(new LigaCommand());
        traxFight.getCommand("message").setExecutor(new MessageCommand());
        traxFight.getCommand("respond").setExecutor(new MessageCommand());
        traxFight.getCommand("more").setExecutor(new MoreCommand());
        traxFight.getCommand("night").setExecutor(new NightCommand());
        traxFight.getCommand("pay").setExecutor(new PayCommand());
        traxFight.getCommand("ping").setExecutor(new PingCommand());
        traxFight.getCommand("playtime").setExecutor(new PlayTimeCommand());
        traxFight.getCommand("privatechatclear").setExecutor(new PrivateChatclearCommand());
        traxFight.getCommand("rang").setExecutor(new RangCommand());
        traxFight.getCommand("ranking").setExecutor(new RankingCommand());
        traxFight.getCommand("realreload").setExecutor(new RealReloadCommand());
        traxFight.getCommand("rename").setExecutor(new RenameCommand());
        traxFight.getCommand("serverstats").setExecutor(new ServerStatsCommand());
        traxFight.getCommand("set").setExecutor(new SetCommand());
        traxFight.getCommand("settabsuffix").setExecutor(new SetTabSuffixCommand());
        traxFight.getCommand("skull").setExecutor(new SkullCommand());
        traxFight.getCommand("spawn").setExecutor(new SpawnCommand());
        traxFight.getCommand("stack").setExecutor(new StackCommand());
        traxFight.getCommand("stats").setExecutor(new StatsCommand());
        traxFight.getCommand("teamchat").setExecutor(new TeamChatCommand());
        traxFight.getCommand("team").setExecutor(new TeamCommand());
        traxFight.getCommand("teleportall").setExecutor(new TeleportAllCommand());
        traxFight.getCommand("trash").setExecutor(new TrashCommand());
        traxFight.getCommand("user").setExecutor(new UserCommand());
        traxFight.getCommand("vanish").setExecutor(new VanishCommand());
        traxFight.getCommand("warp").setExecutor(new WarpCommand());
        traxFight.getCommand("whitelist").setExecutor(new WhitelistCommand());

        traxFight.getCommand("whitelist").setTabCompleter(new TabComplete());
        traxFight.getCommand("clan").setTabCompleter(new ClanCommand());
    }

    private void loadListener(){
        final PluginManager pluginManager = Bukkit.getPluginManager();

        // Custom Listener
        pluginManager.registerEvents(new ClanListener(), traxFight);
        pluginManager.registerEvents(new UserListener(), traxFight);
        pluginManager.registerEvents(new RegionStuffListener(), traxFight);

        // InventoryClick Listener
        pluginManager.registerEvents(new KitInventoryClickListener(), traxFight);
        pluginManager.registerEvents(new RankingInventoryClickListener(), traxFight);
        pluginManager.registerEvents(new TeamInventoryClickListener(), traxFight);
        pluginManager.registerEvents(new WarpInventoryClickListener(), traxFight);

        // Normal Listener
        pluginManager.registerEvents(new BlockShitListener(), traxFight);
        pluginManager.registerEvents(new ChatPrefixListener(), traxFight);
        pluginManager.registerEvents(new DestroyBaseBlockListener(), traxFight);
        pluginManager.registerEvents(new JoinAndQuitListener(), traxFight);
        pluginManager.registerEvents(new PlayerAttackListener(), traxFight);
        pluginManager.registerEvents(new PlayerDeathListener(), traxFight);
    }

    public void addPlayerCount(){
        playercount++;
    }

    public void addClanCount(){
        clancount++;
    }

    public int getPlayercount() {
        return playercount;
    }

    public int getClancount() {
        return clancount;
    }

    public boolean whitelist() {
        return whitelist;
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;

        dataFile.getConfiguration().set("whitelist", whitelist);
        dataFile.saveFileAsync();
    }
}
