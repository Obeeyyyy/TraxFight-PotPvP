package de.obey.traxfight.utils;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 10:06

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.commands.VanishCommand;
import de.obey.traxfight.objects.Combat;
import de.obey.traxfight.usermanager.Rang;
import de.obey.traxfight.usermanager.User;
import de.obey.traxfight.usermanager.UserManager;
import de.obey.utils.MathUtil;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Scoreboarder {

    private final TraxFight traxFight = TraxFight.getInstance();
    private UserManager userManager;

    private final Map<Scoreboard, Map<Rang, Team>> teams = new HashMap<>();

    public Scoreboarder(){
        userManager = traxFight.getUserManager();
    }

    public void updateAll(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers()){
                    setPlayerListName(all);
                    setTabDesign(all);
                    updateScoreboard(all);
                }
            }
        }.runTaskLater(traxFight, 10);
    }

    public void setPlayerListName(Player player){
        final User user = userManager.getUserFromPlayer(player);

        if(user == null)
            return;

        final Rang rang = user.getRang();

        if(rang == null)
            return;

        if(player.getName().equalsIgnoreCase("superbrow15") || player.getName().equalsIgnoreCase("Coca369")){
            player.setPlayerListName(rang.getTabPrefix() + player.getName() + rang.getTabSuffix() + " §3§l✦");
            return;
        }

        player.setPlayerListName(rang.getTabPrefix() + player.getName() + rang.getTabSuffix());
    }

    public void setTabDesign(Player player){
        //String header = "§8» §a§lTraxFight§8.§a§lnet §8«\n\n§8»§7 Online §8×§a " + Bukkit.getOnlinePlayers().size() + "\n§8»§7 Discord §8×§a discord.TraxFight.net\n\n";
        //String footer = "\n§7Viel spaß auf unserem Server.";

        //String header = "§8» §a§lTraxFight§8.§a§lnet §8«\n\n§8§l»§7 Spieler auf TraxFight§8:§e§l " + Bukkit.getOnlinePlayers().size() + "\n§7Aktueller Server§8:§e PotPvP\n\n";
        //tring footer = "\n§7Wir bitten um FeedBack im §3Discord §8(§e/discord§8)\n§7Viel Spaß beim Spielen!";

        final String header = "§8» §a§lTraxFight§8.§a§lnet §8«\n\n§8» §7Spieler Online §8×§a " + (Bukkit.getOnlinePlayers().size() - VanishCommand.vanished.size()) + "\n§8» §7Unser Discord §8×§a discord.TraxFight.net\n\n";
        final String footer = "\n§7Wir würden uns über Feedback freuen.\n§7Viel spaß beim Spielen!";

        final IChatBaseComponent head = IChatBaseComponent.ChatSerializer.a("{\"text\": \" " + header + "\"}");
        final IChatBaseComponent foot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer+ "\"}");

        final PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try{
            Field hf = packet.getClass().getDeclaredField("a");
            hf.setAccessible(true);
            hf.set(packet, head);
            hf.setAccessible(false);
            Field ff = packet.getClass().getDeclaredField("b");
            ff.setAccessible(true);
            ff.set(packet, foot);
            ff.setAccessible(false);
        }catch (Exception ex){ex.printStackTrace();}


        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public Scoreboard createScoreboard(Player player){
        if(player == null)
            return null;

        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective o = scoreboard.getObjective("aaa");

        if (o == null) {
            o = scoreboard.registerNewObjective("aaa", "bbb");
        }

        o.setDisplaySlot(DisplaySlot.SIDEBAR);

        String type = "normal";

        if(traxFight.getCombatManager().isInCombat(player))
            type = "combat";

        if(type.equalsIgnoreCase("normal")){

            /*
                Scoreboard
               Normal - Start
            */

            o.setDisplayName("§2§l⚜ §a§lTraxFight §2§l⚜");

            o.getScore("§7       PotPvP").setScore(12);

            o.getScore("§9").setScore(11);

            o.getScore("  §a§lRang").setScore(10);
            final Team rang = scoreboard.registerNewTeam("rang");
            rang.setPrefix("§7...");
            rang.addEntry("§8");
            o.getScore("§8").setScore(9);


            o.getScore("§7").setScore(8);


            o.getScore("  §a§lMünzen").setScore(7);
            final Team coins = scoreboard.registerNewTeam("coins");
            coins.setPrefix("§7...");
            coins.addEntry("§6");
            o.getScore("§6").setScore(6);


            o.getScore("§4").setScore(5);


            o.getScore("  §a§lStats").setScore(4);
            final Team stats = scoreboard.registerNewTeam("stats");
            stats.setPrefix("§7...");
            stats.addEntry("§1");
            o.getScore("§1").setScore(3);


            o.getScore("§2").setScore(2);


            o.getScore("  §a§lLiga").setScore(1);
            final Team liga = scoreboard.registerNewTeam("liga");
            liga.setPrefix("§7...");
            liga.addEntry("§3");
            o.getScore("§3").setScore(0);



        } else if(type.equalsIgnoreCase("combat")){

            /*
                Scoreboard
               Combat - Start
            */

            final Combat combat = traxFight.getCombatManager().getPlayerCombat().get(player);

            o.setDisplayName("§2§l⚜ §a§lTraxFight §2§l⚜");

            o.getScore("§7       Combat").setScore(10);

            o.getScore("§9").setScore(9);

            o.getScore("  §a§lGegner").setScore(8);

            final Team gegner = scoreboard.registerNewTeam("gegner");
            gegner.setPrefix("§7...");
            gegner.addEntry("§8");
            o.getScore("§8").setScore(7);

            o.getScore("§7").setScore(6);

            o.getScore("  §a§lDauer").setScore(5);

            final Team dauer = scoreboard.registerNewTeam("dauer");
            dauer.setPrefix("§7" + combat.getDurationString());
            dauer.addEntry("§6");
            o.getScore("§6").setScore(4);

            o.getScore("§5").setScore(3);

            o.getScore("  §a§lVerbleibend").setScore(2);

            final Team remain = scoreboard.registerNewTeam("remain");
            remain.setPrefix("§7" + combat.getCooldown() + " s");
            remain.addEntry("§4");
            o.getScore("§4").setScore(1);
        }

         /*
                Scoreboard
            Set Rang for everyone
         */


        final Map<Rang, Team> sip = new HashMap<>();

        for (Rang rang : traxFight.getRangManager().getRangMap().values()) {
            sip.put(rang, getTeam(scoreboard, rang.getRangId() + "", rang.getOverNamePrefix(), rang.getOverNameSuffix()));
        }

        teams.put(scoreboard, sip);

        for (Player all : Bukkit.getOnlinePlayers()) {
            Rang rang = traxFight.getRangManager().getPlayerRang(all);

            if (!teams.get(scoreboard).get(rang).hasPlayer(all))
                teams.get(scoreboard).get(rang).addPlayer(all);
        }

        updateAll();

        return scoreboard;
    }

    public void updateScoreboard(Player player){
        final Scoreboard scoreboard = player.getScoreboard();

        if(scoreboard == null){
            player.setScoreboard(createScoreboard(player));
            return;
        }

        traxFight.getExecutorService().submit(() -> {
            User user = traxFight.getUserManager().getUserFromPlayer(player);
            String type = "normal";

            if(traxFight.getCombatManager().isInCombat(player))
                type = "combat";


            if(type.equalsIgnoreCase("normal")) {

                final Team rang = scoreboard.getTeam("rang");
                rang.setPrefix("§7" + user.getRang().getRangNameColor());

                final Team coins = scoreboard.getTeam("coins");
                String coinString = MathUtil.getLongWithDots(user.getLong("coins"));

                if (user.getLong("coins") > 1000000000)
                    coinString = MathUtil.replaceLongWithSuffix(user.getLong("coins"));

                coins.setPrefix("§7" + coinString);

                final Team liga = scoreboard.getTeam("liga");
                int elo = user.getInteger("ligapoints");
                liga.setPrefix("§7" + traxFight.getLigaManager().getLigaFromPoints(elo).getPrefix());
                liga.setSuffix(" §8(§a" + MathUtil.getLongWithDots(elo) + "§8)");

                final Team stats = scoreboard.getTeam("stats");
                stats.setPrefix("§a" + user.getInteger("kills") + "§8 / §c" + user.getInteger("deaths"));

            } else if(type.equalsIgnoreCase("combat")){

                final Combat combat = traxFight.getCombatManager().getPlayerCombat().get(player);

                if(combat == null)
                    return;

                Team gegner = scoreboard.getTeam("gegner");
                gegner.setPrefix("§7" + combat.getOpponent().getName());

                Team dauer = scoreboard.getTeam("dauer");
                dauer.setPrefix("§7" + combat.getDurationString());

                Team remain = scoreboard.getTeam("remain");
                remain.setPrefix("§7" + combat.getCooldown() + " s");

            }
        });

        if(!teams.containsKey(scoreboard)){
            Map<Rang, Team> sip = new HashMap<>();

            for (Rang rang : traxFight.getRangManager().getRangMap().values()) {
                sip.put(rang, getTeam(scoreboard, rang.getRangId() + "", rang.getOverNamePrefix(), rang.getOverNameSuffix()));
            }

            teams.put(scoreboard, sip);
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            Rang rang = traxFight.getRangManager().getPlayerRang(all);

            if (rang == null)
                return;

            if (!teams.get(scoreboard).get(rang).hasPlayer(all))
                teams.get(scoreboard).get(rang).addPlayer(all);
        }
    }

    private Team getTeam(org.bukkit.scoreboard.Scoreboard board, String Team, String prefix, String suffix) {
        Team team = board.getTeam(Team);
        if (team == null) {
            team = board.registerNewTeam(Team);
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        return team;
    }

    public Map<Scoreboard, Map<Rang, Team>> getTeams() {
        return teams;
    }
}
