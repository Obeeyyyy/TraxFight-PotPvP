package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        26.02.2021 | 18:23

*/

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import de.obey.traxfight.TraxFight;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class RegionStuffListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();

    @EventHandler
    public void on(RegionEnterEvent event){
        Player player = event.getPlayer();

        if(event.getRegion().getId().equalsIgnoreCase("spawn")){
            if(traxFight.getCombatManager().isInCombat(player)){
                Vector playerVector = player.getLocation().toVector();
                Vector worldSpawnVector = player.getWorld().getSpawnLocation().toVector();
                Vector vector = worldSpawnVector.clone().subtract(playerVector).multiply(-2 / worldSpawnVector.distance(playerVector)).setY(0.7);

                player.setVelocity(vector);

                player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 1);
            }
        }

    }
}
