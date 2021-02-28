package de.obey.traxfight.commands;

/*

        (TraxFight-Systems)
  This Class was created by Obey
        11.02.2021 | 22:37

*/

import de.obey.traxfight.TraxFight;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "rename", true))
                return false;

            if(player.getItemInHand() == null){
                player.sendMessage(traxFight.getPrefix() + "Du musst ein  Item in der Hand halten.");
                return false;
            }

            if(args.length == 0){
                player.sendMessage(traxFight.getPrefix() + "/rename <name>");
                return false;
            }

            String name = args[0];

            if(args.length > 1){
                for(int i = 1; i < args.length; i++){
                    name = name + " " + args[i];
                }
            }

            name = ChatColor.translateAlternateColorCodes('&', name);

            if(!player.hasPermission("*")){
                name = " " + name;
            }

            ItemMeta itemMeta = player.getItemInHand().getItemMeta();
            itemMeta.setDisplayName(name);
            player.getItemInHand().setItemMeta(itemMeta);

            player.updateInventory();
        }
        return false;
    }
}
