package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        12.03.2021 | 12:03

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DestroyBaseBlockListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        final Block block = event.getBlock();

        if(block.getType() != Material.ENDER_STONE)
            return;

        final Player player = event.getPlayer();
        final ItemStack item = player.getItemInHand();

        if(item == null || item.getType() != Material.DIAMOND_AXE){
            event.setCancelled(true);
            player.sendMessage(traxFight.getPrefix() + "Um diesen Block abzubauen benötigst du einen §c§lBasebreaker§7.");
            return;
        }

        final ItemMeta meta = item.getItemMeta();

        if(meta == null || meta.getDisplayName() == null)
            return;

        final String name = meta.getDisplayName();
        final List<String> lore = meta.getLore();

        if(!name.equalsIgnoreCase("§c§lBasebreaker")) {
            event.setCancelled(true);
            player.sendMessage(traxFight.getPrefix() + "Um diesen Block abzubauen benötigst du einen §c§lBasebreaker§7.");
            return;
        }

        int chance = Integer.parseInt(lore.get(4).split(" ")[3]);

        player.sendMessage(chance + "");
    }

}
