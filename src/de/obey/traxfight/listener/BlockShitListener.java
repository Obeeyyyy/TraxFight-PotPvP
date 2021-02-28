package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 12:11

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.commands.BuildCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlockShitListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private final Map<Player, Long> lastCommand = new HashMap<>();

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(!BuildCommand.build.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(!BuildCommand.build.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void itemPiuckup(PlayerPickupItemEvent e) {
        final Player player = e.getPlayer();
        if(e.getItem().getItemStack().getType() == Material.POTION){
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack[] contents = player.getInventory().getContents();
                    int changed = 0;
                    int i = 0;
                    while (i < contents.length) {
                        ItemStack current = contents[i];
                        if (current != null && current.getType() != Material.AIR && current.getAmount() > 0 && current.getAmount() < 64 && current.getType() == Material.POTION) {
                            int needed = 64 - current.getAmount();
                            int i2 = i + 1;
                            while (i2 < contents.length) {
                                ItemStack nextCurrent = contents[i2];
                                if (nextCurrent != null && nextCurrent.getType() != Material.AIR && nextCurrent.getAmount() > 0 && current.getType() == nextCurrent.getType() && current.getDurability() == nextCurrent.getDurability() && (current.getItemMeta() == null && nextCurrent.getItemMeta() == null || current.getItemMeta() != null && current.getItemMeta().equals((Object)nextCurrent.getItemMeta()))) {
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
                    if(changed > 0) {
                        player.getInventory().setContents(contents);
                        player.updateInventory();
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5, 5);
                    }
                }
            }.runTaskLater(traxFight, 1);
        }
    }

    @EventHandler
    public void onDie(PlayerDeathEvent event){
        final Player player = event.getEntity();

        event.setDeathMessage("");

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PacketPlayInClientCommand packet = new PacketPlayInClientCommand(
                            PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                    ((CraftPlayer) player).getHandle().playerConnection.a(packet);

                    traxFight.getLocationManager().teleportToSpawnInstant(player);
                    traxFight.getKitManager().equipRespawnKit(player);

                } catch (NullPointerException es) {}
            }
        }.runTaskLater(traxFight, 10);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        if(event.getClickedInventory() != null && event.getClickedInventory().getTitle() != null){
            if(event.getClickedInventory().getTitle().startsWith("§aInv "))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void Weather(WeatherChangeEvent e) {
        boolean rain = e.toWeatherState();
        if (rain)
            e.setCancelled(true);
    }

    @EventHandler
    public void hanldeAchievment(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        final Player player = event.getPlayer();

        if(traxFight.restart){
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\n§8» §a§lTraxFight§8 «\n\n§cDer Server restartet.");
            return;
        }

        if(!traxFight.getLoader().whitelist())
            return;

        if(traxFight.hasPermission(player, "team", false))
            return;

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\n§8» §a§lTraxFight§8 «\n\n§7Du stehts nicht auf der Whitelist.\n\n§a§lDiscord\n§fdiscord.TraxFight.net");

        Bukkit.broadcastMessage(traxFight.getPrefix() + player.getName() + " hat versucht den Server zu betreten");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        final Player player = event.getPlayer();

        if(player.getLocation().getY() >= 300)
            ((CraftPlayer)player).getHandle().playerConnection.disconnect("Nope");
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent e){
        final Player player = e.getPlayer();

        if(traxFight.getCombatManager().isInCombat(player)) {
            if (traxFight.getCombatManager().getBlockedCommand().contains(e.getMessage().toLowerCase())) {
                e.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Dieser Command ist im Kampf verboten.");
            }
        }

        if(lastCommand.containsKey(player)){
            if(System.currentTimeMillis() - lastCommand.get(player) <= 500){
                e.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Bitte warte einen Moment.");
                return;
            }
        }else {
            lastCommand.put(player, System.currentTimeMillis());
        }

        if (Bukkit.getServer().getHelpMap().getHelpTopic(e.getMessage().split(" ")[0]) == null) {
            e.setCancelled(true);
            player.sendMessage(traxFight.getPrefix() + "Der Befehl §8'§a" + e.getMessage().split(" ")[0] + "§8'§7 existiert nicht§8.");
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 0.5f);

        }
    }

    @EventHandler
    public void onChangeSign(SignChangeEvent event){
        final Player player = event.getPlayer();

        if(!traxFight.hasPermission(player, "colorsign", true))
            return;

        event.setLine(0, ChatColor.translateAlternateColorCodes('&', event.getLine(0)));
        event.setLine(1, ChatColor.translateAlternateColorCodes('&', event.getLine(1)));
        event.setLine(2, ChatColor.translateAlternateColorCodes('&', event.getLine(2)));
        event.setLine(3, ChatColor.translateAlternateColorCodes('&', event.getLine(3)));
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        player.setFoodLevel(20);
    }
}
