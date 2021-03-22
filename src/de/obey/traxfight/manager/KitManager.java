package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        26.02.2021 | 09:52

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import de.obey.utils.InventoryUtil;
import de.obey.utils.ItemBuilder;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionType;

public class KitManager {

    private final TraxFight traxFight = TraxFight.getInstance();

    public KitManager() {
    }

    public void updateKitInventory(User user) {
        Inventory inventory = user.getInventory("kit");

        if (inventory == null)
            inventory = Bukkit.createInventory(null, 9 * 5, "§6§lKits §7" + user.getPlayer().getName());

        final Inventory finalInventory = inventory;

        traxFight.getExecutorService().submit(() -> {
            InventoryUtil.fill(finalInventory, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName("§8-/-").build());
            InventoryUtil.fillRow(finalInventory, new ItemBuilder(Material.IRON_FENCE, 1, (byte) 0).setName("§8-/-").build(), 0, 8);
            InventoryUtil.fillRow(finalInventory, new ItemBuilder(Material.IRON_FENCE, 1, (byte) 0).setName("§8-/-").build(), 36, 45);

            final long currentMillis = System.currentTimeMillis();
            final long pvpKitCooldown = user.getLong("pvp_cooldown");
            final long potionKitCooldown = user.getLong("potion_cooldown");
            final long toolKitCooldown = user.getLong("tools_cooldown");
            final long baseKitCooldown = user.getLong("base_cooldown");

            if (currentMillis >= pvpKitCooldown) {
                finalInventory.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0)
                        .setName("§7Kit §8(§4§lPvP§8)")
                        .setLore("", "§8  »§7 Status§8: §a§lAbholbar")
                        .build());
            } else {
                finalInventory.setItem(10, new ItemBuilder(Material.BARRIER, 1, (byte) 0)
                        .setName("§7Kit §8(§4§lPvP§8)")
                        .setLore("", "§8  »§7 Status§8: §c§lNicht Abholbar", "§8    × §f§o" + MathUtil.getHoursFromMillis(pvpKitCooldown - currentMillis))
                        .build());
            }

            if (currentMillis >= potionKitCooldown) {
                finalInventory.setItem(12, new ItemBuilder(Material.POTION, 1, (byte) 0)
                        .setName("§7Kit §8(§d§lPotion§8)")
                        .setLore("", "§8  »§7 Status§8: §a§lAbholbar")
                        .build());
            } else {
                finalInventory.setItem(12, new ItemBuilder(Material.BARRIER, 1, (byte) 0)
                        .setName("§7Kit §8(§d§lPotion§8)")
                        .setLore("", "§8  »§7 Status§8: §c§lNicht Abholbar", "§8    × §f§o" + MathUtil.getHoursFromMillis(potionKitCooldown - currentMillis))
                        .build());
            }
        });
        user.setInventory("kit", finalInventory);
    }

    public void equipPvPKit(Player player) {
        final User user = traxFight.getUserManager().getUserFromPlayer(player);

        traxFight.getExecutorService().submit(() -> {
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0)
                    .setName("§c§lSchwert §8(§7Sharp III§8)")
                    .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_HELMET, 1, (byte) 0)
                    .setName("§c§lHelm §8(§7Prot III§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, (byte) 0)
                    .setName("§c§lPlatte §8(§7Prot III§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_LEGGINGS, 1, (byte) 0)
                    .setName("§c§lHose §8(§7Prot III§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_BOOTS, 1, (byte) 0)
                    .setName("§c§lSchuhe §8(§7Prot III§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            long delay = 172800000l;

            if (traxFight.hasPermission(player, "trax", false))
                delay = 43200000;

            user.setLong("pvp_cooldown", System.currentTimeMillis() + delay);

            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 0.4f, 0.4f);
            player.sendMessage(traxFight.getPrefix() + "Du kannst das Kit wieder in " + MathUtil.getHoursFromMillis(user.getLong("pvp_cooldown") - System.currentTimeMillis()) + " abhohlen.");

            updateKitInventory(user);
        });
    }

    public void equipPotionKit(Player player) {
        traxFight.getExecutorService().submit(() -> {
            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16393)
                    .setName("§4§lStärke I")
                    .setPotion(PotionType.STRENGTH, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16450)
                    .setName("§b§lSpeed I")
                    .setPotion(PotionType.SPEED, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16451)
                    .setName("§6§lFireresi")
                    .setPotion(PotionType.FIRE_RESISTANCE, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16417)
                    .setName("§c§lRegen I")
                    .setPotion(PotionType.REGEN, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            final User user = traxFight.getUserManager().getUserFromPlayer(player);

            long delay = 86400000;

            if (traxFight.hasPermission(player, "trax", false))
                delay = 21600000;

            user.setLong("potion_cooldown", System.currentTimeMillis() + delay);

            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 0.4f, 0.4f);
            player.sendMessage(traxFight.getPrefix() + "Du kannst das Kit wieder in " + MathUtil.getHoursFromMillis(user.getLong("potion_cooldown") - System.currentTimeMillis()) + " abhohlen.");

            updateKitInventory(user);
        });
    }

    public void equipRespawnKit(Player player) {
        traxFight.getExecutorService().submit(() -> {

            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0)
                    .setName("§c§lSchwert §8(§7Sharp III§8)")
                    .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 2)
                    .hideAttributs()
                    .build());

            player.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET, 1, (byte) 0)
                    .setName("§c§lHelm §8(§7Prot II§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 2)
                    .hideAttributs()
                    .build());

            player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, (byte) 0)
                    .setName("§c§lPlatte §8(§7Prot II§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 2)
                    .hideAttributs()
                    .build());

            player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS, 1, (byte) 0)
                    .setName("§c§lHose §8(§7Prot II§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 2)
                    .hideAttributs()
                    .build());

            player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS, 1, (byte) 0)
                    .setName("§c§lSchuhe §8(§7Prot II§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 2)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16393)
                    .setName("§4§lStärke I")
                    .setPotion(PotionType.STRENGTH, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16450)
                    .setName("§b§lSpeed I")
                    .setPotion(PotionType.SPEED, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16451)
                    .setName("§6§lFireresi")
                    .setPotion(PotionType.FIRE_RESISTANCE, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16417)
                    .setName("§c§lRegen I")
                    .setPotion(PotionType.REGEN, 1, true)
                    .build());

            player.getInventory().setItem(8, new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().setItem(7, new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().setItem(6, new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().setItem(5, new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.ENDER_STONE, 1, (byte) 0).setName("§e§lBaseblock").build());
        });
    }

    public void equipFirstJoinKit(Player player) {
        traxFight.getExecutorService().submit(() -> {
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0)
                    .setName("§c§lSchwert §8(§7Sharp V§8)")
                    .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET, 1, (byte) 0)
                    .setName("§c§lHelm §8(§7Prot IV§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, (byte) 0)
                    .setName("§c§lPlatte §8(§7Prot IV§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS, 1, (byte) 0)
                    .setName("§c§lHose §8(§7Prot IV§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS, 1, (byte) 0)
                    .setName("§c§lSchuhe §8(§7Prot IV§8)")
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                    .addEnchantment(Enchantment.DURABILITY, 3)
                    .hideAttributs()
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16393)
                    .setName("§4§lStärke I")
                    .setPotion(PotionType.STRENGTH, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16450)
                    .setName("§b§lSpeed I")
                    .setPotion(PotionType.SPEED, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16451)
                    .setName("§6§lFireresi")
                    .setPotion(PotionType.FIRE_RESISTANCE, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16417)
                    .setName("§c§lRegen I")
                    .setPotion(PotionType.REGEN, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.POTION, 64, (byte) 16453)
                    .setName("§f§lHEAL I")
                    .setPotion(PotionType.INSTANT_HEAL, 1, true)
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.ENDER_STONE, 64, (byte) 0)
                    .setName("§e§lBaseblock")
                    .setLore("", "§7Mit diesem Block kannst du deine Base protecten.")
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.ENDER_STONE, 64, (byte) 0)
                    .setName("§e§lBaseblock")
                    .setLore("", "§7Mit diesem Block kannst du deine Base protecten.")
                    .build());

            player.getInventory().addItem(new ItemBuilder(Material.IRON_PICKAXE, 2, (byte) 0).build());
            player.getInventory().addItem(new ItemBuilder(Material.IRON_AXE, 1, (byte) 0).build());
            player.getInventory().addItem(new ItemBuilder(Material.IRON_SPADE, 1, (byte) 0).build());
        });
    }
}
