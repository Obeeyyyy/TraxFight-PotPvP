package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 20:52

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.User;
import de.obey.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length != 2){
                player.sendMessage(traxFight.getPrefix() + "/bounty <spieler> <münzen>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target, args[0]))
                return false;

            if(target == player){
                player.sendMessage(traxFight.getPrefix() + "Du kannst deinen Kopf nicht aufs Spiel setzen.");
                return false;
            }

            try {
                final long amount = Long.parseLong(args[1]);

                if(amount < 1){
                    player.sendMessage(traxFight.getPrefix() + "Bitte wähle eine Zahl die größer als 0 ist.");
                    return false;
                }

                final User playerUser = traxFight.getUserManager().getUserFromPlayer(player);
                final User targetUser = traxFight.getUserManager().getUserFromPlayer(target);

                if(!traxFight.hasEnoughtLong(playerUser, "coins", amount))
                    return false;

                playerUser.removeLong("coins", amount);
                targetUser.addLong("bounty", amount);

                player.playSound(player.getLocation(), Sound.WITHER_DEATH, 0.4f, 0.4f);
                target.playSound(target.getLocation(), Sound.WITHER_DEATH, 0.4f, 0.4f);

                if(amount > 75000){
                    Bukkit.broadcastMessage(traxFight.getPrefix() + player.getName() + "hat §a" + MathUtil.getLongWithDots(amount) + "§7 Münzen auf den Kopf von §a" + target.getName() + "§7 gesetzt.");
                    return false;
                }

                player.sendMessage(traxFight.getPrefix() + "Du hast §a" + MathUtil.getLongWithDots(amount) + "§7 Münzen auf den Kopf von §a" + target.getName() + "§7 gesetzt.");
                target.sendMessage(traxFight.getPrefix() + player.getName() + " hat §a" + MathUtil.getLongWithDots(amount) + "§7 Münzen auf deinen Kopf gesetzt.");
            }catch (NumberFormatException exception){
                player.sendMessage(traxFight.getPrefix() + "Bitte gebe eine Zahl an.");
                return false;
            }
        }
        return false;
    }
}
