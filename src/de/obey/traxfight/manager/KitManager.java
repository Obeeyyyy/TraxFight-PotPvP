package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        26.02.2021 | 09:52

*/

import de.obey.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

public class KitManager {

    public KitManager(){

    }

    public void equipRespawnKit(Player player){
        player.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET, 1, (byte) 0).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setName("§a§lTraxFight §8§l⚔ §7Respawn§8-§7Helm").build());
        player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, (byte) 0).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setName("§a§lTraxFight §8§l⚔ §7Respawn§8-§7Platte").build());
        player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS, 1, (byte) 0).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setName("§a§lTraxFight §8§l⚔ §7Respawn§8-§7Hose").build());
        player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS, 1, (byte) 0).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setName("§a§lTraxFight §8§l⚔ §7Respawn§8-§7Schuhe").build());

        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD, 1, (byte) 0).addEnchantment(Enchantment.DAMAGE_ALL, 3).setName("§a§lTraxFight §8§l⚔ §7Respawn§8-§7Schwert").build());

        player.getInventory().setItem(8, new ItemBuilder(Material.POTION, 64, (byte) 16453).setPotion(PotionType.INSTANT_HEAL, 1, true).build());
        player.getInventory().setItem(7, new ItemBuilder(Material.POTION, 64, (byte) 16453).setPotion(PotionType.INSTANT_HEAL, 1, true).build());

        player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16385).setPotion(PotionType.SPEED, 1, true).build());
        player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16385).setPotion(PotionType.STRENGTH, 1, true).build());
        player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16385).setPotion(PotionType.FIRE_RESISTANCE, 1, true).build());
        player.getInventory().addItem(new ItemBuilder(Material.POTION, 32, (byte) 16385).setPotion(PotionType.REGEN, 1, true).build());
    }

}
