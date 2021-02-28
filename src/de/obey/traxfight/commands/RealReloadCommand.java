package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        28.01.2021 | 05:19

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RealReloadCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!sender.hasPermission("*")){
            return false;
        }

        Bukkit.broadcastMessage(traxFight.getPrefix() + "Der Server wird in 10 Sekunden neugestartet§8.");

        traxFight.restart = true;

        traxFight.getLocationManager().saveLocations();

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers()){
                    all.kickPlayer(traxFight.getPrefix() + " Server restartet.");
                }
            }
        }.runTaskLater(traxFight, 20*5);


        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().savePlayers();
                Bukkit.getServer().shutdown();
            }
        }.runTaskLater(traxFight, 20*10);

        return false;
    }
}
