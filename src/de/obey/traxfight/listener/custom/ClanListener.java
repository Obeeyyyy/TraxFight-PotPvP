package de.obey.traxfight.listener.custom;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        12.03.2021 | 12:53

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.Clan;
import de.obey.traxfight.backend.ClanFile;
import de.obey.traxfight.backend.events.ClanLoadDataEvent;
import de.obey.traxfight.backend.events.ClanSaveDataEvent;
import de.obey.utils.SimpleMySQL;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class ClanListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private ExecutorService executorService;
    private SimpleMySQL simpleMySQL;

    @EventHandler
    public void on(ClanLoadDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final Clan clan = event.getClan();

        final int clanID = clan.getInteger("clanid");
        final String stringId = clanID + "";

        executorService.submit(() -> {
            clan.setInteger("kills", simpleMySQL.getInt("clanstats", "kills", "id", stringId));
            clan.setInteger("deaths", simpleMySQL.getInt("clanstats", "deaths", "id", stringId));
            clan.setInteger("slots", simpleMySQL.getInt("clanstats", "slots", "id", stringId));

            clan.setLong("bank", simpleMySQL.getInt("clanstats", "bank", "id", stringId));

            clan.setString("name", simpleMySQL.getString("clanstats", "name", "id", stringId));
            clan.setString("tag", simpleMySQL.getString("clanstats", "tag", "id", stringId));
            clan.setString("clancolor", ChatColor.translateAlternateColorCodes('&', simpleMySQL.getString("clanstats", "clancolor", "id", stringId)));
            clan.setString("leaderuuid", simpleMySQL.getString("clanstats", "leaderuuid", "id", stringId));
            clan.setString("created", simpleMySQL.getString("clanstats", "created", "id", stringId));

            simpleMySQL.execute("UPDATE userclans SET CLANANME='" + clan.getString("clanname") + "' WHERE ID='" + clanID + "'");

            traxFight.getClanManager().getNameClans().put(clan.getString("name").toLowerCase(), clanID);

            clan.setClanFile(new ClanFile(traxFight.getDataFolder().getPath() + "/Clans", clan));

            if(clan.getClanFile().getFile().getConfiguration().get("member") != null)
                clan.setList("member", (ArrayList) clan.getClanFile().getFile().getConfiguration().get("member"));

            if(clan.getClanFile().getFile().getConfiguration().get("mods") != null)
                clan.setList("mods", (ArrayList) clan.getClanFile().getFile().getConfiguration().get("mods"));
        });
    }

    @EventHandler
    public void on(ClanSaveDataEvent event){
        if(executorService == null)
            executorService = traxFight.getExecutorService();

        if(simpleMySQL == null)
            simpleMySQL = traxFight.getSimpleMySQL();

        final Clan clan = event.getClan();

        final int clanID = clan.getInteger("clanid");
        final String stringId = clanID + "";

        executorService.submit(() -> {
           simpleMySQL.execute("UPDATE clanstats SET KILLS='" + clan.getInteger("kills") + "' WHERE ID='" + clanID + "'");
           simpleMySQL.execute("UPDATE clanstats SET DEATHS='" + clan.getInteger("deaths") + "' WHERE ID='" + clanID + "'");
           simpleMySQL.execute("UPDATE clanstats SET SLOTS='" + clan.getInteger("slots") + "' WHERE ID='" + clanID + "'");
           simpleMySQL.execute("UPDATE clanstats SET MEMBERS='" + clan.getInteger("members") + "' WHERE ID='" + clanID + "'");

           simpleMySQL.execute("UPDATE clanstats SET clancolor='" + clan.getString("clancolor") + "' WHERE ID='" + clanID + "'");
        });

        clan.getClanFile().getFile().getConfiguration().set("member", clan.getList("member"));
        clan.getClanFile().getFile().getConfiguration().set("mods", clan.getList("mods"));
        clan.getClanFile().getFile().saveFile();
    }
}
