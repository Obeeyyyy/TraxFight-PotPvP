package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 09:53

*/

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.obey.traxfight.TraxFight;
import de.obey.traxfight.manager.CombatManager;
import de.obey.traxfight.objects.Combat;
import de.obey.utils.Actionbar;
import de.obey.utils.WGUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttackListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private CombatManager combatManager;

    @EventHandler
    public void on(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){

            if(combatManager == null)
                combatManager = traxFight.getCombatManager();

            final Player player = (Player) event.getEntity();
            final Player attacker = (Player) event.getDamager();

            if(!WGUtils.allows(DefaultFlag.PVP, player) || !WGUtils.allows(DefaultFlag.PVP, attacker)){
                event.setCancelled(true);
                return;
            }

            if(!combatManager.isInCombat(player))
                new Combat(player);

            combatManager.getPlayerCombat().get(player).hit(attacker);


            if(!combatManager.isInCombat(attacker))
                new Combat(attacker);

            combatManager.getPlayerCombat().get(attacker).hit(player);

            traxFight.getExecutorService().submit(() -> {
                try {
                    Thread.sleep(20);

                    if(player.isDead())
                        return;

                    Actionbar.sendActionBar(player,"§a§lTraxFight §8- §c" + attacker.getName() + " " + getHealthStringFromPlayer(attacker));
                    Actionbar.sendActionBar(attacker,"§a§lTraxFight §8- §c" + player.getName() + " " + getHealthStringFromPlayer(player));
                } catch (InterruptedException e) {}
            });
        }

    }

    private String getHealthStringFromPlayer(Player player){

        String text = "";

        double maxHealth = 10.0;
        double health = player.getHealth()/2;

        for(int i = 0 ; i < maxHealth; i++){
            if(i < health){
                if( health - i > 0.5){
                    text = text + "§4❤";
                }else{
                    text = text + "§c❤";
                }
            }else {
                text = text + "§7❤";
            }
        }

        return text;
    }
}
