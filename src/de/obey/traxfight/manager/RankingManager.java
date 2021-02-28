package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        28.02.2021 | 07:38

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.User;
import de.obey.utils.InventoryUtil;
import de.obey.utils.ItemBuilder;
import de.obey.utils.MathUtil;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankingManager {

    private final TraxFight traxFight = TraxFight.getInstance();
    private SimpleMySQL mysql;
    private final Map<String, Inventory> inventoryMap = new HashMap<>();

    private BukkitTask updater;

    public RankingManager(){

        updater = new BukkitRunnable() {
            @Override
            public void run() {

                if(inventoryMap.size() == 0) {
                    createInventories();
                    return;
                }

                updateInventory("kills");
                updateInventory("lp");
                updateInventory("killstreak");
                updateInventory("coins");
                updateInventory("playtime");
            }
        }.runTaskTimerAsynchronously(traxFight, 100, 20*60*10);
    }

    private void createInventories(){
        final Inventory kills = Bukkit.createInventory(null, 9*6, "§a§lRanking §6Kills");
        final Inventory lp = Bukkit.createInventory(null, 9*6, "§a§lRanking §6Ligapunkte");
        final Inventory killstreak = Bukkit.createInventory(null, 9*6, "§a§lRanking §6Killstreak");
        final Inventory coins = Bukkit.createInventory(null, 9*6, "§a§lRanking §6Münzen");
        final Inventory playtime = Bukkit.createInventory(null, 9*6, "§a§lRanking §6Playtime");

        setInvItems(kills);
        setInvItems(lp);
        setInvItems(killstreak);
        setInvItems(coins);
        setInvItems(playtime);

        inventoryMap.put("kills", kills);
        inventoryMap.put("lp", lp);
        inventoryMap.put("killstreak", killstreak);
        inventoryMap.put("coins", coins);
        inventoryMap.put("playtime", playtime);

        updateInventory("kills");
        updateInventory("lp");
        updateInventory("killstreak");
        updateInventory("coins");
        updateInventory("playtime");
    }


    private void setInvItems(Inventory inventory){
        final ItemStack glas = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName("§8-/-").build();
        final ItemStack bar = new ItemBuilder(Material.IRON_FENCE, 1, (byte) 0).setName("§8-/-").build();

        InventoryUtil.fill(inventory, glas);

        inventory.setItem(0, bar);
        inventory.setItem(8, bar);
        inventory.setItem(9, bar);
        inventory.setItem(17, bar);
        inventory.setItem(18, bar);
        inventory.setItem(26, bar);
        inventory.setItem(27, bar);
        inventory.setItem(35, bar);
        inventory.setItem(36, bar);
        inventory.setItem(44, bar);
        inventory.setItem(45, bar);
        inventory.setItem(53, bar);

        inventory.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0).setName("§3Top 10 Kills §7<Anzeigen>").build());
        inventory.setItem(11, new ItemBuilder(Material.EMERALD, 1, (byte) 0).setName("§3Top 10 Ligapunkte §7<Anzeigen>").build());
        inventory.setItem(12, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setTexture("YTg4MzJjMTQ2NmM4NDFjYzc5ZDVmMTAyOTVkNDY0Mjc5OTY3OTc1YTI0NTFjN2E1MzNjNzk5Njg5NzQwOGJlYSJ9fX0=").setName("§3Top 10 Killstreak §7<Anzeigen>").build());
        inventory.setItem(14, new ItemBuilder(Material.DOUBLE_PLANT, 1, (byte) 0).setName("§3Top 10 Münzen §7<Anzeigen>").build());
        inventory.setItem(15, new ItemBuilder(Material.WATCH, 1, (byte) 0).setName("§3Top 10 Playtime §7<Anzeigen>").build());
    }

    private void updateInventory(String what) {

        if (mysql == null)
            mysql = traxFight.getSimpleMySQL();

        //traxFight.getExecutorService().submit(() -> {
            if (what.equals("coins")) {
                final Inventory inventory = inventoryMap.get(what);
                final HashMap<Integer, Integer> ranking = new HashMap<>();
                final ResultSet resultSet = mysql.getResultSet("SELECT ID FROM userstats ORDER BY COINS DESC LIMIT 11");
                int rank = 1;

                if (resultSet == null)
                    return;

                try {
                    while (resultSet.next()) {
                        ranking.put(rank, resultSet.getInt("ID"));
                        rank++;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for (int i = 1; i < 11; i++) {
                    if (ranking.size() < i) {
                        inventory.setItem(getSlotFromRank(i), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§7 ??? §8(§a#" + i + "§8)").setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=").build());
                    } else {
                        inventory.setItem(getSlotFromRank(i), getPlayerHeadWithData(ranking.get(i), i, what));
                    }
                }
                return;
            }

            if(what.equals("kills")){
                final Inventory inventory = inventoryMap.get(what);
                final HashMap<Integer, Integer> ranking = new HashMap<>();
                final ResultSet resultSet = mysql.getResultSet("SELECT ID FROM userstats ORDER BY KILLS DESC LIMIT 11");
                int rank = 1;

                if (resultSet == null)
                    return;

                try {
                    while (resultSet.next()) {
                        ranking.put(rank, resultSet.getInt("ID"));
                        rank++;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for (int i = 1; i < 11; i++) {
                    if (ranking.size() < i) {
                        inventory.setItem(getSlotFromRank(i), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§7 ??? §8(§a#" + i + "§8)").setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=").build());
                    } else {
                        inventory.setItem(getSlotFromRank(i), getPlayerHeadWithData(ranking.get(i), i, what));
                    }
                }
                return;
            }

            if(what.equals("lp")){
                final Inventory inventory = inventoryMap.get(what);
                final HashMap<Integer, Integer> ranking = new HashMap<>();
                final ResultSet resultSet = mysql.getResultSet("SELECT ID FROM userstats ORDER BY LIGAPOINTS DESC LIMIT 11");
                int rank = 1;

                if (resultSet == null)
                    return;

                try {
                    while (resultSet.next()) {
                        ranking.put(rank, resultSet.getInt("ID"));
                        rank++;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for (int i = 1; i < 11; i++) {
                    if (ranking.size() < i) {
                        inventory.setItem(getSlotFromRank(i), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§7 ??? §8(§a#" + i + "§8)").setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=").build());
                    } else {
                        inventory.setItem(getSlotFromRank(i), getPlayerHeadWithData(ranking.get(i), i, what));
                    }
                }
                return;
            }

            if(what.equals("killstreak")){
                final Inventory inventory = inventoryMap.get(what);
                final HashMap<Integer, Integer> ranking = new HashMap<>();
                final ResultSet resultSet = mysql.getResultSet("SELECT ID FROM userstats ORDER BY KILLSTREAKREKORD DESC LIMIT 11");
                int rank = 1;

                if (resultSet == null)
                    return;

                try {
                    while (resultSet.next()) {
                        ranking.put(rank, resultSet.getInt("ID"));
                        rank++;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for (int i = 1; i < 11; i++) {
                    if (ranking.size() < i) {
                        inventory.setItem(getSlotFromRank(i), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§7 ??? §8(§a#" + i + "§8)").setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=").build());
                    } else {
                        inventory.setItem(getSlotFromRank(i), getPlayerHeadWithData(ranking.get(i), i, what));
                    }
                }
                return;
            }

            if(what.equals("playtime")){
                final Inventory inventory = inventoryMap.get(what);
                final HashMap<Integer, Integer> ranking = new HashMap<>();
                final ResultSet resultSet = mysql.getResultSet("SELECT ID FROM userplaytime ORDER BY DAYS DESC LIMIT 11");
                int rank = 1;

                if (resultSet == null)
                    return;

                try {
                    while (resultSet.next()) {
                        ranking.put(rank, resultSet.getInt("ID"));
                        rank++;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                for (int i = 1; i < 11; i++) {
                    if (ranking.size() < i) {
                        inventory.setItem(getSlotFromRank(i), new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§7 ??? §8(§a#" + i + "§8)").setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=").build());
                    } else {
                        inventory.setItem(getSlotFromRank(i), getPlayerHeadWithData(ranking.get(i), i, what));
                    }
                }
                return;
            }
        //});
    }

    private ItemStack getPlayerHeadWithData(int id, int rank, String what){
        final UUID uuid = UUID.fromString(mysql.getString("users", "uuid", "id", String.valueOf(id)));
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if(player == null)
            return null;

        User user = traxFight.getUserManager().getUserFromOfflinePlayer(player);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(user == null)
            return null;

        ItemStack head = null;

        if(what.equals("coins")) {
            head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§7" + player.getName() + "§8 (§a#" + rank + "§8)")
                    .setLore("§7", "§8 »§7 " + MathUtil.getLongWithDots(user.getLong("coins")))
                    .setOwner(player.getName())
                    .build();
        }

        if(what.equals("kills")) {
            head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§7" + player.getName() + "§8 (§a#" + rank + "§8)")
                    .setLore("§7", "§8 »§7 " + MathUtil.getLongWithDots(user.getInteger("kills")))
                    .setOwner(player.getName())
                    .build();
        }

        if(what.equals("lp")) {
            head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§7" + player.getName() + "§8 (§a#" + rank + "§8)")
                    .setLore("§7", "§8 »§7 " + MathUtil.getLongWithDots(user.getInteger("ligapoints")))
                    .setOwner(player.getName())
                    .build();
        }

        if(what.equals("killstreak")) {
            head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§7" + player.getName() + "§8 (§a#" + rank + "§8)")
                    .setLore("§7", "§8 »§7 " + MathUtil.getLongWithDots(user.getInteger("killstreakrekord")))
                    .setOwner(player.getName())
                    .build();
        }

        if(what.equals("playtime")) {
            if(user.getInteger("days") > 0) {
                head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                        .setName("§7" + player.getName() + "§8 (§a#" + rank + "§8)")
                        .setLore("§7", "§8 »§7 " + MathUtil.getLongWithDots(user.getInteger("days")))
                        .setOwner(player.getName())
                        .build();
            }else{
                head = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                        .setName("§7 ??? §8(§a#" + rank + "§8)")
                        .setTexture("YmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19=")
                        .build();
            }
        }

        return head;
    }

    private int getSlotFromRank(int rank){
        if(rank == 1)
            return 29;

        if(rank == 2)
            return 30;

        if(rank == 3)
            return 31;

        if(rank == 4)
            return 32;

        if(rank == 5)
            return 33;

        if(rank == 6)
            return 38;

        if(rank == 7)
            return 39;

        if(rank == 8)
            return 40;

        if(rank == 9)
            return 41;

        if(rank == 10)
            return 42;

        return 0;
    }

    public Map<String, Inventory> getInventoryMap() {
        return inventoryMap;
    }
}
