package de.obey.traxfight.listener;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        22.02.2021 | 22:45

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import de.obey.traxfight.backend.UserManager;
import de.obey.traxfight.utils.Bools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatPrefixListener implements Listener {

    private final TraxFight traxFight = TraxFight.getInstance();
    private UserManager userManager;

    private final Map<UUID, Long> lastmessage = new HashMap<>();

    @EventHandler
    public void on(AsyncPlayerChatEvent event){
        if(userManager == null)
            userManager = traxFight.getUserManager();

        final Player player = event.getPlayer();

        event.setCancelled(true);

        final User user = userManager.getUserFromPlayer(player);

        if(user == null || user.getRang() == null) {
            player.sendMessage(traxFight.getPrefix() + "Bitte warte bis deine Statistiken geladen wurden.");
            return;
        }

        String message = event.getMessage();

        if(lastmessage.containsKey(player.getUniqueId())){
            if(System.currentTimeMillis() - lastmessage.get(player.getUniqueId()) <= 800){
                player.sendMessage(traxFight.getPrefix() + "Bitte warte einen Moment.");
                return;
            }
        }

        if(player.hasPermission("traxfight.chatcolor") || player.hasPermission("traxfight.team"))
            message = ChatColor.translateAlternateColorCodes('&', message);

        String format = user.getRang().getChatPrefix() + player.getName() + user.getRang().getChatSuffix() + message;

        if(user.getClan() != null && user.getClan().getString("name") != null)
            format = user.getRang().getChatPrefix() + "§8*" + user.getClan().getString("clancolor") + user.getClan().getString("tag") + "§8*§7" + player.getName() + user.getRang().getChatSuffix() + message;

        if(Bools.globalmute && !traxFight.hasPermission(player, "team", false)) {
            player.sendMessage(traxFight.getPrefix() + "Du kannst während einem Globalmute nicht schreiben.");

            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return;
        }

        if(message.equalsIgnoreCase("#save")){
            if(player.hasPermission("*")) {
                userManager.saveAllUsers();
                traxFight.getClanManager().saveALlClans();
                player.sendMessage(traxFight.getPrefix() + "Alle user und clans gespeichert.");
                return;
            }
        }

        player.playSound(player.getLocation(), Sound.CLICK, 0.4f, 0.4f);

        for(Player all : Bukkit.getOnlinePlayers())
            all.sendMessage(format);

        lastmessage.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
