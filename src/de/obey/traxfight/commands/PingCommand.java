package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:27

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length > 1){
                player.sendMessage(traxFight.getPrefix() + "/ping <spieler>");
                return false;
            }

            if(args.length == 0){
                player.sendMessage(traxFight.getPrefix() + "Dein Ping§8: §a" + ((CraftPlayer)player).getHandle().ping);
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(traxFight.getPrefix() + args[0] + " ist nicht Online.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                return false;
            }

            player.sendMessage(traxFight.getPrefix() + args[0] + "'s Ping§8: §a" + ((CraftPlayer)target).getHandle().ping);
        }
        return false;
    }
}
