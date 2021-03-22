package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        09.03.2021 | 11:47

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.InventoryUtil;
import de.obey.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

    private final TraxFight traxFight = TraxFight.getInstance();
    private final Map<String, Inventory> inventories = new HashMap<>();

    public TeamManager() {
        final Inventory baseInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§7 Team");
        final Inventory highteamInv = Bukkit.createInventory(null, 9, "§a§lTrax§4§l Highteam");
        final Inventory devInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§b§l Developer");
        final Inventory modInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§5§l Moderation");
        final Inventory builderInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§6§l Building");
        final Inventory staffInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§a§l Staff");
        final Inventory guideInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§3§l Guide");
        final Inventory contentInv = Bukkit.createInventory(null, 9 * 5, "§a§lTrax§e§l Content");

        InventoryUtil.fill(baseInv, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 15)
                .setName("§8-/-s")
                .build());

        baseInv.setItem(4, new ItemBuilder(Material.INK_SACK, 1, (byte) 1)
                .setName("§4§lHighteam")
                .build());

        baseInv.setItem(20, new ItemBuilder(Material.INK_SACK, 1, (byte) 5)
                .setName("§5§lModeratoren")
                .build());

        baseInv.setItem(22, new ItemBuilder(Material.INK_SACK, 1, (byte) 14)
                .setName("§5§lBuilder")
                .build());

        baseInv.setItem(24, new ItemBuilder(Material.INK_SACK, 1, (byte) 11)
                .setName("§e§lContent")
                .build());

        baseInv.setItem(39, new ItemBuilder(Material.INK_SACK, 1, (byte) 10)
                .setName("§a§lStaffs")
                .build());

        baseInv.setItem(41, new ItemBuilder(Material.INK_SACK, 1, (byte) 6)
                .setName("§3§lGuides")
                .build());

        inventories.put("base", baseInv);
        inventories.put("highteam", highteamInv);
        inventories.put("dev", devInv);
        inventories.put("mod", modInv);
        inventories.put("builder", builderInv);
        inventories.put("staff", staffInv);
        inventories.put("guide", guideInv);
        inventories.put("content", contentInv);

        updateInventories();
    }

    public void updateInventories() {
        final Inventory highteam = inventories.get("highteam");

        final OfflinePlayer obey = Bukkit.getOfflinePlayer(UUID.fromString("f4b1497c-622e-4f50-b87a-059a8fa5b024"));
        final OfflinePlayer vince = Bukkit.getOfflinePlayer(UUID.fromString("5f451678-a43e-4e5e-bce5-8ea1235ab7e4"));
        final OfflinePlayer vic = Bukkit.getOfflinePlayer(UUID.fromString("096254dd-fb8f-4f3f-a346-30ef72a1cb53"));
        final OfflinePlayer modz = Bukkit.getOfflinePlayer(UUID.fromString("8325996f-68e3-44d3-8185-cfa316a629a4"));

        traxFight.getExecutorService().submit(() -> {
            highteam.setItem(2, getHeadFromPlayer(obey, "§b§lDevelopment"));
            highteam.setItem(3, getHeadFromPlayer(vince, "§6§lCommunitymanagement"));
            highteam.setItem(4, getHeadFromPlayer(vic, "§c§lAdministration"));
            highteam.setItem(5, getHeadFromPlayer(modz, "§9§lTeammanagement"));
        });
    }

    private ItemStack getHeadFromPlayer(OfflinePlayer player, String aufgabe){
        if(player.isOnline()){
            return new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§7" + player.getName())
                    .setOwner(player.getName())
                    .setLore("", "§8» §3§lInformationen§8:", "", "§8  ×§7 Status§8: §a§lOnline", "§8  ×§7 Aufgabe§8: §r" + aufgabe)
                    .build();
        }

        return new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                .setName("§7" + player.getName())
                .setOwner(player.getName())
                .setLore("", "§8» §3§lInformationen§8:", "", "§8  ×§7 Status§8: §c§lOffline", "§8  ×§7 Aufgabe§8: §r" + aufgabe)
                .build();
    }

    public Map<String, Inventory> getInventories() {
        return inventories;
    }
}