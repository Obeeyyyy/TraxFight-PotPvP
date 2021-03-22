package de.obey.traxfight.manager;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        14.02.2021 | 02:13

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.SimpleFile;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final TraxFight traxFight = TraxFight.getInstance();
    private SimpleFile simpleFile;

    private final ArrayList<Player> teleporting = new ArrayList<>();
    private final Map<String, Location> locations = new HashMap<>();

    public LocationManager(){
        simpleFile = new SimpleFile(traxFight.getDataFolder().getPath() + "/locations.yml");

        if(!simpleFile.exists())
            simpleFile.createFile();
    }

    public void teleportToLocation(Player player, Location location){
        if(teleporting.contains(player))
            return;

        teleporting.add(player);

        new BukkitRunnable() {
            int remain = 3;
            int ticks = 0;
            final Location saved = player.getLocation();
            @Override
            public void run() {
                final Location neu = player.getLocation();

                remain--;
                ticks++;

                if(ticks == 4){
                    player.teleport(location);
                    player.sendTitle("§a§l✔", "");
                    player.sendMessage(traxFight.getPrefix() + "§aTeleportation erfolgreich.");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 2);
                    teleporting.remove(player);
                    cancel();
                    return;
                }

                if(ticks == 0)
                    player.sendMessage(traxFight.getPrefix() + "Starte teleportation.");

                if (saved.distance(neu) > 1) {
                    if (saved.getX() != neu.getX() || saved.getZ() != neu.getZ())
                        teleporting.remove(player);
                }

                if(!teleporting.contains(player)){
                    player.sendMessage(traxFight.getPrefix() + "§cTeleportation abgebrochen.");
                    player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                    cancel();
                    return;
                }

                String message = "";

                for(int i = 0; i < ticks; i++)
                    message = message + "§a▉";


                for(int i = 0; i < remain; i++)
                    message = message + "§7▉";

                player.sendTitle("", message);

                for(Player all : player.getWorld().getPlayers()){
                    all.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 5);
                    all.playSound(player.getLocation(), Sound.NOTE_PLING, 2, 2);
                }
            }
        }.runTaskTimer(traxFight, 0, 20);
    }

    public void teleportToLocationInstant(Player player, Location location){
        if(location == null)
            return;

        player.teleport(location);
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 0.4f, 0.4f);
    }

    public void loadLocations(){
        simpleFile = new SimpleFile(traxFight.getDataFolder().getPath() + "/locations.yml");

        if(!simpleFile.exists())
            simpleFile.createFile();

        try {
            for (String location : simpleFile.getConfiguration().getKeys(false))
                locations.put(location, (Location) simpleFile.getConfiguration().get(location));
        }catch (NullPointerException ex){
            System.out.println("Keine Locations vorhande.");
        }
    }

    public void saveLocations(){
        if(simpleFile == null)
            return;

        if(locations == null)
            return;

        for(String location : locations.keySet())
            simpleFile.getConfiguration().set(location, locations.get(location));

        simpleFile.saveFile();
    }

    public void setLocation(String name, Location location){
        locations.put(name, location);
        saveLocations();
    }

    public Location getLocation(String name){
        if(simpleFile == null)
            loadLocations();

        return locations.get(name);
    }

    public SimpleFile getSimpleFile() {
        return simpleFile;
    }
}
