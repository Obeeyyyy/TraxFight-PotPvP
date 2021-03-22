package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 10:57

*/

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.CombatManager;
import de.obey.traxfight.manager.LigaManager;
import de.obey.traxfight.objects.Combat;
import de.obey.traxfight.backend.User;
import de.obey.utils.Actionbar;
import de.obey.utils.ItemBuilder;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private CombatManager combatManager;
    private LigaManager ligaManager;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (combatManager == null)
            combatManager = traxFight.getCombatManager();

        if (ligaManager == null)
            ligaManager = traxFight.getLigaManager();

        final User user = traxFight.getUserManager().getUserFromPlayer(player);

        user.addInteger("deaths", 1);
        user.removeLong("coins", 50);
        user.setInteger("killstreak", 0);

        final Player killer = player.getKiller();

        traxFight.getKillFarmManager().check(player, killer);

        if (traxFight.getKillFarmManager().getBlocked().contains(player.getUniqueId())) {
            if (combatManager.isInCombat(player)) {
                final Combat combat = combatManager.getPlayerCombat().get(player);

                combat.end();
                combatManager.getPlayerCombat().get(combat.getOpponent()).end();
            }
            return;
        }

        if (player.getKiller() == null)
            return;

        final User killerUser = traxFight.getUserManager().getUserFromPlayer(killer);

        killer.playSound(player.getLocation(), Sound.LEVEL_UP, 0.4f, 0.4f);

        killerUser.addInteger("kills", 1);
        killerUser.addInteger("killstreak", 1);
        killerUser.addLong("coins", 200);

        if (killerUser.getInteger("killstreak") > killerUser.getInteger("killstreakrekord")) {
            killerUser.setInteger("killstreakrekord", killerUser.getInteger("killstreak"));
            killer.sendMessage(traxFight.getPrefix() + "Du hast einen neuen Killstreakrekord aufgestellt! §8(§a" + killerUser.getInteger("killstreakrekord") + " Kills§8)");
        }

        final long bounty = user.getLong("bounty");

        if (bounty > 0) {
            killer.sendMessage(traxFight.getPrefix() + "Du hast §a" + MathUtil.getLongWithDots(bounty) + " §7Münzen für §a" + player.getName() + "'s §7Kopf erhalten.");
            killerUser.addLong("coins", bounty);
            user.setLong("bounty", 0);
        }

        Bukkit.broadcastMessage(traxFight.getPrefix() + "§c§o" + player.getName() + " §8(" + ligaManager.getLigaFromPoints(user.getInteger("ligapoints")).getSmallPrefix() + "§8)§7 wurde von §a§o" + killer.getName() + " §8(" + ligaManager.getLigaFromPoints(killerUser.getInteger("ligapoints")).getSmallPrefix() + "§8) §7getötet.");

        // Elo Earnings - Start

        Actionbar.sendActionBar(killer, "§a§lTraxFight§8 ┃ §c§m" + player.getName());

        final int killerLigaId = ligaManager.getLigaFromPoints(killerUser.getInteger("ligapoints")).getId();
        final int playerLigaId = ligaManager.getLigaFromPoints(user.getInteger("ligapoints")).getId();

        int eloadd = 0;
        int eloremove = 0;

        if (playerLigaId - killerLigaId >= -2 && playerLigaId - killerLigaId <= 2) {
            eloadd = 6;
            eloremove = 2;
        }

        if (playerLigaId - killerLigaId < -2 && playerLigaId - killerLigaId > -5) {
            eloadd = 3;
            eloremove = 2;
        }

        if (playerLigaId - killerLigaId < -5) {
            eloadd = 2;
            eloremove = 1;
        }

        if (playerLigaId - killerLigaId > 2 && playerLigaId - killerLigaId < 5) {
            eloadd = 8;
            eloremove = 2;

            return;
        }

        if (playerLigaId - killerLigaId > 5) {
            eloadd = 12;
            eloremove = 4;
        }

        killerUser.addInteger("ligapoints", eloadd);
        user.removeInteger("ligapoints", eloremove);

        spawnHolo(player, 200, eloadd);

        if (combatManager.isInCombat(player)) {
            final Combat combat = combatManager.getPlayerCombat().get(player);

            combat.end();
            combatManager.getPlayerCombat().get(combat.getOpponent()).end();
        }

        // Elo Earnings - End

    }

    private void spawnHolo(Player player, int moneyReward, int eloReward){
        final Hologram h = HologramsAPI.createHologram(traxFight, player.getLocation().add(0, 3, 0));

        h.appendTextLine("§a§lTraxFight");
        h.appendTextLine("§7Kill §8┃ §a+1");
        h.appendTextLine("§7Münzen §8┃ §a+" + moneyReward);
        h.appendTextLine("§7Elo §8┃ §a+" + eloReward);
        h.appendItemLine(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setPlayerTexture(player.getName(), "").build());

        new BukkitRunnable() {
            @Override
            public void run() {
                h.delete();
            }
        }.runTaskLater(traxFight, 100);
    }
}
