package de.obey.traxfight.listener.inventoryclick;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        07.03.2021 | 15:30

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitInventoryClickListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory().getTitle() == null)
            return;

        if(event.getClickedInventory().getTitle().startsWith("§6§lKits")) {
            event.setCancelled(true);

            final Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                return;

            if(event.getCurrentItem().getType() == Material.BARRIER) {
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                return;
            }

            if(event.getSlot() == 10)
                traxFight.getKitManager().equipPvPKit(player);

            if(event.getSlot() == 12)
                traxFight.getKitManager().equipPotionKit(player);
        }
    }
}
