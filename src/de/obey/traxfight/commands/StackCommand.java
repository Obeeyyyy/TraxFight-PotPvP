package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        24.02.2021 | 17:35

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StackCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "stack", true))
                return false;

            ItemStack[] contents = player.getInventory().getContents();
            ArrayList<Material> no = new ArrayList<>();
            no.add(Material.DIAMOND_HELMET);
            no.add(Material.DIAMOND_CHESTPLATE);
            no.add(Material.DIAMOND_LEGGINGS);
            no.add(Material.DIAMOND_BOOTS);

            no.add(Material.IRON_HELMET);
            no.add(Material.IRON_CHESTPLATE);
            no.add(Material.IRON_LEGGINGS);
            no.add(Material.IRON_BOOTS);

            no.add(Material.GOLD_HELMET);
            no.add(Material.GOLD_CHESTPLATE);
            no.add(Material.GOLD_LEGGINGS);
            no.add(Material.GOLD_BOOTS);

            no.add(Material.LEATHER_HELMET);
            no.add(Material.LEATHER_CHESTPLATE);
            no.add(Material.LEATHER_LEGGINGS);
            no.add(Material.LEATHER_BOOTS);

            no.add(Material.SKULL_ITEM);
            no.add(Material.DIAMOND_SWORD);
            no.add(Material.IRON_SWORD);
            no.add(Material.STONE_SWORD);
            no.add(Material.FISHING_ROD);

            int changed = 0;
            int i = 0;
            while (i < contents.length) {
                ItemStack current = contents[i];
                if (current != null && current.getType() != Material.AIR && current.getAmount() > 0 && current.getAmount() < 64 && !no.contains(current.getType())) {
                    int needed = 64 - current.getAmount();
                    int i2 = i + 1;
                    while (i2 < contents.length) {
                        ItemStack nextCurrent = contents[i2];
                        if (nextCurrent != null && nextCurrent.getType() != Material.AIR && nextCurrent.getAmount() > 0 && current.getType() == nextCurrent.getType() && current.getDurability() == nextCurrent.getDurability() && (current.getItemMeta() == null && nextCurrent.getItemMeta() == null || current.getItemMeta() != null && current.getItemMeta().equals((Object) nextCurrent.getItemMeta()) && !no.contains(current.getType()))) {
                            if (nextCurrent.getAmount() > needed) {
                                current.setAmount(64);
                                nextCurrent.setAmount(nextCurrent.getAmount() - needed);
                                break;
                            }
                            contents[i2] = null;
                            current.setAmount(current.getAmount() + nextCurrent.getAmount());
                            needed = 64 - current.getAmount();
                            ++changed;
                        }
                        ++i2;
                    }
                }
                ++i;
            }
            if (changed > 0) {
                player.getInventory().setContents(contents);
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5, 5);
                player.sendMessage(traxFight.getPrefix() + "Alle Items in deinem Inventar wurden gestackt.");
            } else {
                player.sendMessage(traxFight.getPrefix() + "Es konnten keine Items gestackt werden.");
            }
        }
        return false;
    }
}
