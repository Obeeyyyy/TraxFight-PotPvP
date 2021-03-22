package de.obey.traxfight.listener.inventoryclick;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        10.03.2021 | 22:27

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamInventoryClickListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory().getTitle() == null)
            return;

        if(event.getClickedInventory().getTitle().startsWith("§a§lTrax"))
            event.setCancelled(true);

        if(event.getClickedInventory().getTitle().equalsIgnoreCase("§a§lTrax§7 Team")) {
            final Player player = (Player) event.getWhoClicked();

            if(event.getSlot() == 4){
                player.openInventory(traxFight.getTeamManager().getInventories().get("highteam"));
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.4f, 0.4f);
            }
        }
    }
}
