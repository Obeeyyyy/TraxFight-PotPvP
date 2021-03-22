package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 13:43

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length != 0){
                player.sendMessage(traxFight.getPrefix() + "/fix");
                return false;
            }

            int count = 0;
            try {
                int i = 0;
                while (i < 36) {
                    final ItemStack is = player.getInventory().getItem(i);
                    final String type = is.getType().name();
                    if (is != null && !is.getType().isBlock() && is.getType() != Material.INK_SACK
                            && is.getType() != Material.GOLDEN_APPLE && is.getType() != Material.AIR
                            && is.getType() != Material.POTION && is.getType() != Material.SKULL_ITEM
                            && is.getType() != Material.MONSTER_EGG && is.getType() != Material.EXP_BOTTLE
                            && !type.contains("PICKAXE") && !type.contains("AXE") && !type.contains("HOE") && !type.contains("SPADE")) {
                        is.setDurability((short) 0);
                        ++count;
                    }
                    ++i;
                }
                if (player.getInventory().getHelmet() != null) {
                    player.getInventory().getHelmet().setDurability((short) 0);
                    ++count;
                }
                if (player.getInventory().getChestplate() != null) {
                    player.getInventory().getChestplate().setDurability((short) 0);
                    ++count;
                }
                if (player.getInventory().getBoots() != null) {
                    player.getInventory().getBoots().setDurability((short) 0);
                    ++count;
                }
                if (player.getInventory().getLeggings() != null) {
                    player.getInventory().getLeggings().setDurability((short) 0);
                    ++count;
                }
                if (count == 0) {
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5, 5);
                } else {
                    player.playSound(player.getLocation(), Sound.ANVIL_USE, 5, 5);
                }
                player.updateInventory();
            } catch (Exception e) {}

        }
        return false;
    }
}
