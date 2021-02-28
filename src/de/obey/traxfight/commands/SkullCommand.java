package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        25.02.2021 | 09:34

*/

import de.obey.traxfight.TraxFight;
import de.obey.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkullCommand implements CommandExecutor {

    private final TraxFight traxFight = TraxFight.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            final Player player =(Player) sender;

            if(!traxFight.hasPermission(player, "skull", true))
                return false;

            if(args.length > 2 || args.length == 0){
                player.sendMessage(traxFight.getPrefix() + "/skull <player>");
                return false;
            }

            if(args.length == 1){
                String playerName = args[0];

                //final ItemStack skull = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setPlayerTexture(playerName, "§f§oKopf von " + playerName).build();
                final ItemStack skull = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setOwner(playerName).setName("§f§oKopf von " + playerName).build();

                player.getInventory().addItem(skull);
                return false;
            }

            if(!traxFight.hasPermission(player, "*", false))
                return false;

            String code = args[0];

            ItemStack item = new ItemBuilder(Material.SKULL_ITEM,1 , (byte) 3).setTexture(code).build();

            player.getInventory().addItem(item);
        }

        return false;
    }
}
