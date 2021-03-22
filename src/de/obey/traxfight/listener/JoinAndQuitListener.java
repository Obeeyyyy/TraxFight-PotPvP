package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 05:29

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class JoinAndQuitListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private CombatManager combatManager;

   @EventHandler
   public void on(PlayerLoginEvent event){

       if(traxFight.getSimpleMySQL() == null){
           event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§7Wir haben aktuell ein internes Problem, bitte melde es einem Teammitglied.");
           return;
       }

       traxFight.getUserManager().loadUser(event.getPlayer());
   }

    @EventHandler
    public void on(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        event.setJoinMessage("");

        for(int i = 0; i < 200; i++) {
            player.sendMessage("" + ChatColor.values()[new Random().nextInt(6)]);
        }

        player.sendMessage("§7              Willkommen zurück §a" + player.getName() + "§7.");
        player.sendMessage("");
        player.sendMessage("§7            Discord§8: §adiscord.TraxFight.net");
        player.sendMessage("");
        player.sendMessage("§7    Wir würden uns über dein Feedback freuen.");
        player.sendMessage("§7                     Viel Spaß beim Spielen.");
        player.sendMessage("");
        player.sendMessage("");


        if(traxFight.hasPermission(player, "team", false)) {
            event.setJoinMessage("§8[§a+§8] §7" + player.getName());
            traxFight.getTeamManager().updateInventories();
        }

        if(combatManager == null)
            combatManager = traxFight.getCombatManager();

        if(combatManager.getCombatLoggers().contains(player.getName())){
            for(Player team : Bukkit.getOnlinePlayers()){
                if(traxFight.hasPermission(team, "team", false)){
                    team.sendMessage("§4§lCombatlogger§8 > §f" + player.getName() + "§7 c ist wieder gejoint.");
                }
            }
        }

        player.setScoreboard(traxFight.getScoreboarder().createScoreboard(player));
    }

    @EventHandler
    public void on(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        traxFight.getUserManager().saveUser(player);

        event.setQuitMessage("");

        if(traxFight.hasPermission(player, "team", false)) {
            event.setQuitMessage("§8[§c-§8] §7" + player.getName());
            traxFight.getTeamManager().updateInventories();
        }

        if(combatManager == null)
            combatManager = traxFight.getCombatManager();

        if(combatManager.isInCombat(player)) {
            player.damage(100);
            combatManager.getPlayerCombat().get(player).logout();
        }
    }
}
