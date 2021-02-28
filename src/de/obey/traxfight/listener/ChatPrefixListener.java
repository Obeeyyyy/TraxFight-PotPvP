package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 22:45

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.usermanager.User;
import de.obey.traxfight.usermanager.UserManager;
import de.obey.traxfight.utils.Bools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatPrefixListener implements Listener {

    private TraxFight traxFight = TraxFight.getInstance();
    private UserManager userManager;

    @EventHandler
    public void on(AsyncPlayerChatEvent event){
        if(userManager == null)
            userManager = traxFight.getUserManager();

        Player player = event.getPlayer();

        event.setCancelled(true);

        User user = userManager.getUserFromPlayer(player);

        String message = event.getMessage();

        if(player.hasPermission("traxfight.chatcolor") || player.hasPermission("traxfight.team"))
            message = ChatColor.translateAlternateColorCodes('&', message);

        String format = user.getRang().getChatPrefix() + player.getName() + user.getRang().getChatSuffix() + message;

        if(Bools.globalmute && !traxFight.hasPermission(player, "team", false)) {
            player.sendMessage(traxFight.getPrefix() + "Du kannst während einem Globalmute nicht schreiben.");

            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return;
        }

        if(message.equalsIgnoreCase("#save")){
            if(player.hasPermission("*")) {
                userManager.saveAllUsers();
                player.sendMessage(traxFight.getPrefix() + "Alle user gespeichert.");
                return;
            }
        }

        player.playSound(player.getLocation(), Sound.CLICK, 0.4f, 0.4f);

        for(Player all : Bukkit.getOnlinePlayers())
            all.sendMessage(format);
    }
}
