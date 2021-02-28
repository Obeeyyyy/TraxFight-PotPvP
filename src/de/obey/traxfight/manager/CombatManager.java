package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        23.02.2021 | 09:46

*/

import de.obey.traxfight.objects.Combat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombatManager {

    private final Map<Player, Combat> playerCombat = new HashMap<>();
    private final ArrayList<String> combatLoggers = new ArrayList<>();
    private final ArrayList<String> blockedCommand = new ArrayList<>();

    public CombatManager(){
        blockedCommand.add("/spawn");
        blockedCommand.add("/ec");
        blockedCommand.add("/pay");
        blockedCommand.add("/tpa");
        blockedCommand.add("/warps");
    }

    public Map<Player, Combat> getPlayerCombat() {
        return playerCombat;
    }

    public boolean isInCombat(Player player){
        return playerCombat.containsKey(player);
    }

    public ArrayList<String> getCombatLoggers() {
        return combatLoggers;
    }

    public ArrayList<String> getBlockedCommand() {
        return blockedCommand;
    }

}
