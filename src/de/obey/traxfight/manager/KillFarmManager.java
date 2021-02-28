package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        27.02.2021 | 12:01

*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KillFarmManager {

    private Map<Player, Long> lastDeath = new HashMap<>();
    private Map<Player, Player> killers = new HashMap<>();
    private Map<Player, Integer> deathAmount = new HashMap<>();
    private ArrayList<Player> blocked = new ArrayList<>();

    public KillFarmManager(){

    }

    public void check(Player player, Player killer){
        if(lastDeath.containsKey(player)){
            if(System.currentTimeMillis() - lastDeath.get(player) <= 60000){
                if (deathAmount.containsKey(player)) {
                    deathAmount.put(player, deathAmount.get(player) + 1);
                }else{
                    deathAmount.put(player, 1);
                }
                killers.put(player, killer);
            }
        }

        if(deathAmount.containsKey(player) && deathAmount.get(player) > 2){
            blocked.add(player);
            for(Player team : Bukkit.getOnlinePlayers()){
                team.sendMessage("");
                team.sendMessage("§8» §4§lKillFarm§c§l warning");
                team.sendMessage("§8» §f" + player.getName() + "§c ist zum §f" + deathAmount.get(player) + "x §czu schnell gestorben.");
                team.sendMessage("§8» §4§lKiller§c " + killers.get(player).getName());
            }
        }

        lastDeath.put(player, System.currentTimeMillis());
    }

    public ArrayList<Player> getBlocked() {
        return blocked;
    }
}
