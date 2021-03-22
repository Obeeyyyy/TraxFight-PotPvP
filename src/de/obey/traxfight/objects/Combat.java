package de.obey.traxfight.objects;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 07:41

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.CombatManager;
import de.obey.traxfight.utils.Scoreboarder;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class Combat {

    private final TraxFight traxFight = TraxFight.getInstance();
    private final CombatManager combatManager = traxFight.getCombatManager();

    private final String prefix = "§8» §c§lCOMBAT §8×§7 ";
    private String durationString = "0M 0S";

    private Player player;

    private final long startMillis = System.currentTimeMillis();
    private long lastHit = -1;
    private int cooldown = 11;

    private BukkitTask runnable;

    private final ArrayList<Player> attackers = new ArrayList<>();

    private Player opponent;
    private Scoreboard scoreboard;

    public Combat(Player player){
        this.player = player;
        scoreboard = player.getScoreboard();

        combatManager.getPlayerCombat().put(player, this);

        player.setScoreboard(traxFight.getScoreboarder().createScoreboard(player));

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                cooldown--;
                durationString = MathUtil.getMinutesFromMillis(System.currentTimeMillis()-startMillis);

                traxFight.getScoreboarder().updateScoreboard(player);

                if(cooldown <= 0)
                    end();

            }
        }.runTaskTimer(traxFight, 20, 20);
    }

    public void end(){
        runnable.cancel();
        player.setScoreboard(scoreboard);
        combatManager.getPlayerCombat().remove(player);
        traxFight.getScoreboarder().updateScoreboard(player);
    }

    public void hit(Player player){
        cooldown = 11;

        lastHit = System.currentTimeMillis();

        if(attackers.contains(player))
            attackers.remove(player);

        attackers.add(player);
        opponent = player;
    }

    public void logout(){
        combatManager.getCombatLoggers().add(player.getName());

        for(Player team : Bukkit.getOnlinePlayers()){
            if(traxFight.hasPermission(team, "team", false)){
                team.sendMessage(prefix + "§8'§e§o" + player.getName() + "§8'§7 hat sich in einem Kampf ausgeloggt§8.");
            }
        }
    }

    public String getDurationString() {
        return durationString;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Player getOpponent() {
        return opponent;
    }
}
