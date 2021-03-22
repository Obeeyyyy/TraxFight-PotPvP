package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        12.03.2021 | 12:36

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

public class PayCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(args.length != 2){
                player.sendMessage(traxFight.getPrefix() + "/pay <spieler> <betrag>");
                return false;
            }

            final Player target = Bukkit.getPlayer(args[0]);

            if(!traxFight.isOnline(player, target, args[0]))
                return false;

            if(target == player) {
                player.sendMessage(traxFight.getPrefix() + "Lügen darf man nicht sagen.");
                return false;
            }

            try {
                final long amount = Long.parseLong(args[1]);

                if(amount < 1){
                    player.sendMessage(traxFight.getPrefix() + "Bitte wähle eine Zahl die größer als 0 ist.");
                    return false;
                }

                final User user = traxFight.getUserManager().getUserFromPlayer(player);
                final User targetUser = traxFight.getUserManager().getUserFromPlayer(target);

                if(!traxFight.hasEnoughtLong(user, "coins", amount))
                    return false;

                user.removeLong("coins", amount);
                targetUser.addLong("coins", amount);

                player.sendMessage(traxFight.getPrefix() + "Du hast §a" + MathUtil.getLongWithDots(amount) + "§7 Münzen an §a" + target.getName() + "§7 überwiesen.");
                target.sendMessage(traxFight.getPrefix() + "§a" + player.getName() + "§7 hat dir §a" + MathUtil.getLongWithDots(amount) + "§7 Münzen überwiesen.");

                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.4f, 0.4f);
                target.playSound(target.getLocation(), Sound.VILLAGER_YES, 0.4f, 0.4f);

            }catch (NumberFormatException exception){
                player.sendMessage(traxFight.getPrefix() + "Bitte gebe eine Zahl an.");
            }
        }
        return false;
    }
}
