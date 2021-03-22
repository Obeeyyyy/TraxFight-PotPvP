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
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.management.GarbageCollectorMXBean;
import java.util.HashMap;
import java.util.Map;

public class BlockShitListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private final Map<Player, Long> lastCommand = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
                return;

            final Material material = player.getItemInHand().getType();

            if(material == Material.GOLDEN_APPLE && player.getItemInHand().getData().getData() == 1){
                event.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Opäpfel sind auf dem Server deaktiviert.");
                return;
            }

            if(material == Material.POTION){
                final byte data = player.getItemInHand().getData().getData();

                player.sendMessage(data + "");

                if(data == 68 ||
                        data == 4 ||
                        data == 42 ||
                        data == 36 ||
                        data == 74 ||
                        data == 76 ||
                        data == 44 ||
                        data == 72 ||
                        data == 40){

                    event.setCancelled(true);
                    player.sendMessage(traxFight.getPrefix() + "Dieser Trank ist auf dem Server deaktiviert.");

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        final Location spawn = traxFight.getLocationManager().getLocation("spawn");
        final Location playerLocation = event.getPlayer().getLocation();

        if(spawn == null)
            return;

        if(playerLocation.getWorld() == spawn.getWorld() && playerLocation.distance(spawn) <= 400){
            if(!BuildCommand.build.contains(event.getPlayer()))
                event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        final Location spawn = traxFight.getLocationManager().getLocation("spawn");
        final Location playerLocation = event.getPlayer().getLocation();

        if(spawn == null)
            return;

        if(playerLocation.getWorld() == spawn.getWorld() && playerLocation.distance(spawn) <= 400){
            if(!BuildCommand.build.contains(event.getPlayer()))
                event.setCancelled(true);
            return;
        }
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

                    traxFight.getLocationManager().teleportToLocationInstant(player, traxFight.getLocationManager().getLocation("spawn"));
                    traxFight.getKitManager().equipRespawnKit(player);

                } catch (NullPointerException es) {}
            }
        }.runTaskLater(traxFight, 30);
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
        final boolean rain = e.toWeatherState();

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

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\n§8» §a§lTraxFight§8 «\n\n§7Du stehst nicht auf der Whitelist.\n\n§a§lDiscord\n§fdiscord.TraxFight.net");

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

        if(lastCommand.containsKey(player)){
            if(System.currentTimeMillis() - lastCommand.get(player) <= 2000){
                e.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Bitte warte einen Moment.");
                return;
            }
        }else {
            lastCommand.put(player, System.currentTimeMillis());
        }

        if(traxFight.getCombatManager().isInCombat(player)) {
            if (traxFight.getCombatManager().getBlockedCommand().contains(e.getMessage().toLowerCase())) {
                e.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Dieser Command ist im Kampf verboten.");
            }
        }

        final String command = e.getMessage().split(" ")[0];

        if (Bukkit.getServer().getHelpMap().getHelpTopic(command) == null) {
            e.setCancelled(true);
            player.sendMessage(traxFight.getPrefix() + "Der Befehl §8'§a" + command + "§8'§7 existiert nicht§8.");
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 0.5f);

        }

        if(command.equalsIgnoreCase("/pl") ||
                command.equalsIgnoreCase("/?") ||
                command.equalsIgnoreCase("/help") ||
                command.equalsIgnoreCase("/icanhasbukkit") ||
                command.equalsIgnoreCase("/pex") ||
                command.equalsIgnoreCase("//permissionsex") ||
                command.equalsIgnoreCase("/we") ||
                command.equalsIgnoreCase("/worldguard") ||
                command.equalsIgnoreCase("/wg") ||
                command.equalsIgnoreCase("/worldedit") ||
                command.equalsIgnoreCase("/version") ||
                command.equalsIgnoreCase("/about") ||
                command.equalsIgnoreCase("/plugins")){

            if(!traxFight.hasPermission(player, "*", false)){
                e.setCancelled(true);
                player.sendMessage(traxFight.getPrefix() + "Wenn du etwas über den Server wissen möchtest kannst du gerne im Discord nachfragen.");
                return;
            }

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

        if(!(event.getEntity() instanceof Player))
            return;

        if(event.getFoodLevel() < 20)
            event.setFoodLevel(20);
    }
}
