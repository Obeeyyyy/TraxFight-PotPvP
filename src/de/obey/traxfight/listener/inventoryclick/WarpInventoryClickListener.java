package de.obey.traxfight.listener.inventoryclick;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        11.03.2021 | 00:08

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.LocationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WarpInventoryClickListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private LocationManager locationManager;

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null)
            return;

        if(event.getClickedInventory().getTitle() == null)
            return;

        if(locationManager == null)
            locationManager = traxFight.getLocationManager();

        if(event.getClickedInventory().getTitle().equalsIgnoreCase("§5§lWarps")) {
            event.setCancelled(true);

            if (event.getSlot() == 12) {
                locationManager.teleportToLocation((Player) event.getWhoClicked(), locationManager.getLocation("netherfps"));
                event.getWhoClicked().closeInventory();
                return;
            }
        }
    }
}
