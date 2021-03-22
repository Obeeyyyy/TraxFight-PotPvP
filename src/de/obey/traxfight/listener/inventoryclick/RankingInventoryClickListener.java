package de.obey.traxfight.listener.inventoryclick;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        28.02.2021 | 09:48

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RankingInventoryClickListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory().getTitle() == null)
            return;

        if(event.getClickedInventory().getTitle().startsWith("§a§lRanking")) {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            if (event.getSlot() == 10) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("kills"));
                return;
            }

            if (event.getSlot() == 11) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("lp"));
                return;
            }

            if (event.getSlot() == 12) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("killstreak"));
                return;
            }

            if (event.getSlot() == 14) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("coins"));
                return;
            }

            if (event.getSlot() == 15) {
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
                player.openInventory(traxFight.getRankingManager().getInventoryMap().get("playtime"));
                return;
            }
        }
    }
}
