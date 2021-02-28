package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 05:29

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.CombatManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class JoinAndQuitListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private CombatManager combatManager;

    @EventHandler
    public void on(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        player.setScoreboard(traxFight.getScoreboarder().createScoreboard(player));
        traxFight.getUserManager().loadUser(player);

        event.setJoinMessage("");

        for(int i = 0; i < 200; i++) {
            player.sendMessage("" + ChatColor.values()[new Random().nextInt(6)]);
        }

        if(traxFight.hasPermission(player, "team", false))
            event.setJoinMessage("§8[§a+§8] §7" + player.getName());

        if(combatManager == null)
            combatManager = traxFight.getCombatManager();

        if(combatManager.getCombatLoggers().contains(player.getName()))
            player.sendMessage("SIP");
    }

    @EventHandler
    public void on(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        traxFight.getUserManager().saveUser(player);

        event.setQuitMessage("");

        if(traxFight.hasPermission(player, "team", false))
            event.setQuitMessage("§8[§c-§8] §7" + player.getName());

        if(combatManager == null)
            combatManager = traxFight.getCombatManager();
    }
}
