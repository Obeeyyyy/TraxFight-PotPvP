package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        06.03.2021 | 20:26

*/

import de.obey.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class WarpManager {

    private Inventory warpInventory;

    public WarpManager(){
        warpInventory = Bukkit.createInventory(null, 9*5, "§5§lWarps");

        warpInventory.setItem(12, new ItemBuilder(Material.NETHER_STAR, 1, (byte) 0)
                .setName("§c§lNether FPS")
                .build());
    }

    public Inventory getWarpInventory(){
        return warpInventory;
    }

}
