package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        27.02.2021 | 12:01

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillFarmManager {

    private final TraxFight traxFight = TraxFight.getInstance();

    private final Map<Player, Long> lastDeath = new HashMap<>();
    private final Map<Player, Player> killers = new HashMap<>();
    private final Map<Player, Integer> deathAmount = new HashMap<>();
    private final ArrayList<UUID> blocked = new ArrayList<>();

    public KillFarmManager(){}

    public void check(Player player, Player killer){
        if(lastDeath.containsKey(player)){
            if(System.currentTimeMillis() - lastDeath.get(player) <= 60000){

                if (!deathAmount.containsKey(player)) {
                    deathAmount.put(player, 1);
                }else{
                    deathAmount.put(player, deathAmount.get(player) + 1);
                }

                killers.put(player, killer);

                if(deathAmount.containsKey(player) && deathAmount.get(player) >= 2){
                    blocked.add(player.getUniqueId());
                    for(Player team : Bukkit.getOnlinePlayers()){

                        if(!traxFight.hasPermission(team, "team", false))
                                    return;

                        team.sendMessage("");
                        team.sendMessage("");
                        team.sendMessage("§8§l» §4§lKillfarm-Warn §8§l× §c" + player.getName() + " §7ist oft gestorben! §8(§f" + deathAmount.get(player) + "x§8)");
                        team.sendMessage("§8§l» §7Zuletzt getötet von §c" + killers.get(player).getName());
                        team.sendMessage("§8§l» §c§lZeit §7zwischen den §c§lKills§8: §f" + MathUtil.getMinutesFromMillis(System.currentTimeMillis() - lastDeath.get(player)));
                        team.sendMessage("");
                        team.sendMessage("");
                    }
                }
            }else{
                if(System.currentTimeMillis() - lastDeath.get(player) >= 180000) {
                    if (deathAmount.containsKey(player)) {
                        deathAmount.remove(player);
                        if(blocked.contains(player.getUniqueId()))
                            blocked.remove(player.getUniqueId());
                    }
                }
            }
        }

        lastDeath.put(player, System.currentTimeMillis());
    }

    public ArrayList<UUID> getBlocked() {
        return blocked;
    }
}
