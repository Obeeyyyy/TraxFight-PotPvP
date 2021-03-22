package de.obey.traxfight.commands;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        13.03.2021 | 23:34

*/

import de.obey.traxfight.TraxFight;
import de.obey.traxfight.backend.Clan;
import de.obey.traxfight.backend.ClanManager;
import de.obey.traxfight.backend.User;
import de.obey.utils.ChatUtil;
import de.obey.utils.MathUtil;
import de.obey.utils.SimpleMySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final TraxFight traxFight = TraxFight.getInstance();
    private ClanManager clanManager;
    private SimpleMySQL mysql;

    private final Map<String, ArrayList<String>> invites = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(mysql == null)
            mysql = traxFight.getSimpleMySQL();

        if(clanManager == null)
            clanManager = traxFight.getClanManager();

        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (args.length == 0)
                return sendSyntax(player);

            final User user = traxFight.getUserManager().getUserFromPlayer(player);

            if(args.length == 1){
                if(args[0].equalsIgnoreCase("stats")){

                    if(!isInclan(user))
                        return false;

                    sendStatsMessage(player, user.getClan().getString("name"));

                    return false;
                }

                if(args[0].equalsIgnoreCase("leave")){

                    if(user.getClan() == null){
                        player.sendMessage(traxFight.getPrefix() + "Du bist in keinem Clan :c");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    final Clan clan = user.getClan();

                    if(clan.getString("leaderuuid").equalsIgnoreCase(player.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + "Du bist der Clanleader, du kannst den Clan nicht verlassen.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    final ArrayList<String> member = clan.getList("member");
                    final ArrayList<String> mods = clan.getList("mods");

                    member.remove(player.getUniqueId().toString());
                    mods.remove(player.getUniqueId().toString());

                    user.setClan(null);

                    traxFight.getClanManager().sendToAllClanMembers(clan, traxFight.getPrefix() + player.getName() + " hat den Clan verlassen.");
                    player.sendMessage(traxFight.getPrefix() + "Du hast deinen Clan verlassen.");

                    return false;
                }

                if(args[0].equalsIgnoreCase("list")){

                    if(!isInclan(user))
                        return false;

                    final Clan clan = user.getClan();
                    final ArrayList<String> members = clan.getList("member");

                    player.sendMessage("");
                    player.sendMessage("§8§l§m---------------------------------------");
                    player.sendMessage("§8                          §6§lCLAN");
                    player.sendMessage("");

                    for(String uuidString : members) {
                        final UUID uuid = UUID.fromString(uuidString);
                        final OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);

                        if (uuidString.equalsIgnoreCase(clan.getString("leaderuuid"))) {
                            if (member.isOnline()) {
                                player.sendMessage("§7  " + member.getName() + " §8(§4§lClanleader§8) - §a§lONLINE");
                                player.sendMessage("");
                            } else {
                                player.sendMessage("§7  " + member.getName() + " §8(§4§lClanleader§8) - §c§lOFFLINE");
                                player.sendMessage("");
                            }
                        } else {

                            if (member.isOnline()) {
                                player.sendMessage("§7  " + member.getName() + " §8(§9§lClanmember§8)- §a§lONLINE");
                                player.sendMessage("");
                            } else {
                                player.sendMessage("§7  " + member.getName() + " §8(§9§lClanmember§8)- §c§lOFFLINE");
                                player.sendMessage("");
                            }
                        }
                    }

                    player.sendMessage("§8§l§m---------------------------------------");
                    player.sendMessage("");

                    return false;
                }

                if (args[0].equalsIgnoreCase("delete")) {

                    if(!isInclan(user))
                        return false;

                    if(!user.getClan().getString("leaderuuid").equalsIgnoreCase(player.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + "Du bist nicht der Clan leader.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    final Clan clan = clanManager.getClanFromUser(user);

                    clan.getClanFile().getFile().getFile().delete();

                    mysql.execute("DELETE FROM clanstats WHERE ID='" + clan.getInteger("clanid") + "'");

                    user.setClan(null);

                    for(Object uuid : clan.getList("member")){
                        final String uuidString = (String) uuid;

                        final Player member = Bukkit.getPlayer(UUID.fromString(uuidString));

                        User memberUser = null;

                        if(member != null)
                            memberUser = traxFight.getUserManager().getUserFromPlayer(member);

                        if(memberUser == null)
                            memberUser = traxFight.getUserManager().getUserFromOfflinePlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuidString)));

                        User finalMemberUser = memberUser;

                        traxFight.getExecutorService().submit(() -> {
                            mysql.execute("UPDATE userclans SET clanid='-1' WHERE ID='" + finalMemberUser.getInteger("id") + "'");
                            mysql.execute("UPDATE userclans SET clanname='" + null + "' WHERE ID='" + finalMemberUser.getInteger("id") + "'");
                        });

                        final User u1 = traxFight.getUserManager().getUserFromUUID(UUID.fromString(uuidString));

                        if(u1 != null) {
                            u1.setClan(null);
                            if(u1.getPlayer() != null && u1.getPlayer().isOnline())
                                u1.getPlayer().sendMessage(traxFight.getPrefix() + "§cDer Clan §7" + clan.getString("name") + " §cwurde gerade aufgelöst.");
                        }
                    }

                    clanManager.getNameClans().remove(clan.getString("name"));
                    clanManager.getClans().remove(clan.getInteger("id"));


                    player.sendMessage(traxFight.getPrefix() + "Du hast deinen Clan gelöscht.");
                    player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);

                    return false;
                }

                if(args[0].equalsIgnoreCase("bank")){

                    if(!isInclan(user))
                        return false;

                    player.sendMessage(traxFight.getPrefix() + "In der Clanbank liegen §e" + MathUtil.getLongWithDots(user.getClan().getLong("bank")) + "§7 Münzen.");

                    return false;
                }

                sendSyntax(player);

                return false;
            }

            if(args.length == 2){

                if (args[0].equalsIgnoreCase("stats")) {
                    final String clanName = args[1];

                    sendStatsMessage(player, clanName);

                    return false;
                }

                if (args[0].equalsIgnoreCase("invite")) {

                    if(!isInclan(user))
                        return false;

                    final Clan clan = user.getClan();

                    if(!clan.getList("mods").contains(player.getUniqueId().toString()) && !clan.getString("leaderuuid").equalsIgnoreCase(player.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + "Du kannst keine Clanmember einladen.");
                        return false;
                    }

                    if(clan.getInteger("slots") <= clan.getList("member").size()+1){
                        player.sendMessage(traxFight.getPrefix() + "Der Clan ist voll, kaufe neue Slots im Clanshop.");
                        return false;
                    }

                    final Player target = Bukkit.getPlayer(args[1]);

                    if(!traxFight.isOnline(player, target, args[1]))
                        return false;

                    final User targetUser = traxFight.getUserManager().getUserFromPlayer(target);

                    if(isInclan(targetUser)){
                        player.sendMessage(traxFight.getPrefix() + target.getName() + " ist schon in einem Clan.");
                        return false;
                    }

                    ArrayList<String> invetedNames = invites.get(clan.getString("name"));

                    if(invetedNames == null)
                        invetedNames = new ArrayList<String>();

                    if(invetedNames.contains(target.getName())){
                        player.sendMessage(traxFight.getPrefix() + target.getName() + " wurde bereits eine Claneinladung gesendet.");
                        return false;
                    }

                    invetedNames.add(target.getName());

                    invites.put(clan.getString("name").toLowerCase(), invetedNames);

                    player.sendMessage(traxFight.getPrefix() + "Du hast " + target.getName() + " eine Claneinladung gesendet.");
                    target.sendMessage(traxFight.getPrefix() + "Du hast eine Claneinladung erhalten.");
                    target.sendMessage("              §8»§7 Clan§8: §7" + clan.getString("name") + "§8 (" + clan.getString("clancolor") + clan.getString("tag") + "§8)");
                    target.sendMessage("              §8»§7 Eingeladen von§8: §7" + player.getName());
                    ChatUtil.sendMessageOnClickCommand(target, "§a§oclicke um anzunehmen", "/clan accept " + clan.getString("name"));
                    ChatUtil.sendMessageOnClickCommand(target, "§c§oclicke um abzulehnen", "/clan deny " + clan.getString("name"));

                    return false;
                }

                if(args[0].equalsIgnoreCase("accept")){

                    if(isInclan(user)){
                        player.sendMessage(traxFight.getPrefix() + "Du bist bereits in einem Clan.");
                        return false;
                    }

                    final String clanName = args[1];

                    if(invites.get(clanName.toLowerCase()) == null || !invites.get(clanName.toLowerCase()).contains(player.getName())){
                        player.sendMessage(traxFight.getPrefix() + "Du hast keine Claneinladung von " + clanName + " erhalten.");
                        return false;
                    }

                    if (!clanManager.isLoaded(clanName)) {
                        player.sendMessage(traxFight.getPrefix() + "Bitte versuche es erneut wenn ein Clanmitglied online ist.");
                        return false;
                    }

                    final Clan clan = clanManager.getClanFromName(clanName);

                    if(clan.getInteger("slots") <= clan.getList("member").size() + 1){
                        player.sendMessage(traxFight.getPrefix() + "Der Clan ist voll, deine Claneinladung wird jetzt gelöscht.");
                        invites.get(clanName.toLowerCase()).remove(player.getName());
                        return false;
                    }

                    clan.getList("member").add(player.getUniqueId().toString());
                    user.setInteger("clanid", clan.getInteger("clanid"));

                    clanManager.sendToAllClanMembers(clan, traxFight.getPrefix() + player.getName() + " ist dem Clan beigetreten, willkommen.");

                    return false;
                }

                if(args[0].equalsIgnoreCase("deny")){

                    if(isInclan(user)){
                        player.sendMessage(traxFight.getPrefix() + "Du bist bereits in einem Clan.");
                        return false;
                    }

                    final String clanName = args[1];

                    if(invites.get(clanName.toLowerCase()) == null || !invites.get(clanName.toLowerCase()).contains(player.getName())){
                        player.sendMessage(traxFight.getPrefix() + "Du hast keine Claneinladung von " + clanName + " erhalten.");
                        return false;
                    }

                    if (!clanManager.isLoaded(clanName)) {
                        player.sendMessage(traxFight.getPrefix() + "Bitte versuche es erneut wenn ein Clanmitglied online ist.");
                        return false;
                    }

                    invites.get(clanName.toLowerCase()).remove(player.getName());
                    player.sendMessage(traxFight.getPrefix() + "Du hast die Claneinladung von " + clanName + " abgelehnt.");

                    return false;
                }

                if(args[0].equalsIgnoreCase("pay")){
                    if(!isInclan(user))
                        return false;

                    try {
                        final long amount = Long.parseLong(args[1]);

                        if(amount < 0){
                            player.sendMessage(traxFight.getPrefix() + "Du musst mehr als 0 Münzen einzahlen.");
                            return false;
                        }

                        if(!traxFight.hasEnoughtLong(user, "coins", amount))
                            return false;

                        user.removeLong("coins", amount);
                        user.getClan().addLong("bank", amount);
                        player.playSound(player.getLocation(), Sound.GLASS, 0.4f, 0.4f);
                        traxFight.getClanManager().sendToAllClanMembers(user.getClan(), traxFight.getPrefix() + player.getName() + " hat §e" + MathUtil.getLongWithDots(amount) + "§7 Münzen in die Clanbank gezahlt.");

                        return false;

                    }catch (NumberFormatException exception){
                        player.sendMessage(traxFight.getPrefix() + "Bitte gebe eine gültige Zahl an.");
                        return false;
                    }
                }

                if(args[0].equalsIgnoreCase("kick")){

                    if(!isInclan(user))
                        return false;

                    final Clan clan = user.getClan();

                    if(!clan.getList("mods").contains(player.getUniqueId().toString()) && !clan.getString("leaderuuid").equalsIgnoreCase(player.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + "Du kannst keine Member aus dem Clan kicken.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                    if(target == null){
                        player.sendMessage(traxFight.getPrefix() + "Der Spieler" + args[1] + " spielt nicht auf unserem Server.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    if(clan.getList("mods").contains(target.getUniqueId().toString()) || clan.getString("leaderuuid").equalsIgnoreCase(target.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + "Du kannst " + args[1] + " nicht aus dem Clan kicken.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    if(!clan.getList("member").contains(target.getUniqueId().toString())){
                        player.sendMessage(traxFight.getPrefix() + args[1] + " ist nicht in deinem Clan.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    clan.getList("member").remove(target.getUniqueId().toString());

                    final User targetUser = traxFight.getUserManager().getUserFromOfflinePlayer(target);

                    traxFight.getExecutorService().submit(() -> {
                       traxFight.getSimpleMySQL().execute("UPDATE userclans SET clanid='-1' where id='" + targetUser.getInteger("id") + "'");
                       traxFight.getSimpleMySQL().execute("UPDATE userclans SET claname='" + null + "' where id='" + targetUser.getInteger("id") + "'");
                    });

                    targetUser.setClan(null);
                    targetUser.setInteger("clanid", -1);

                    traxFight.getClanManager().sendToAllClanMembers(clan, traxFight.getPrefix() + args[1] + " wurde von " + player.getName() + " aus dem Clan gekickt.");

                    return false;
                }

                sendSyntax(player);

                return false;
            }

            if(args.length == 3){
                if(args[0].equalsIgnoreCase("create")){

                    if(user.getClan() != null){
                        player.sendMessage(traxFight.getPrefix() + "Du bist bereits in einem Clan.");
                        player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
                        return false;
                    }

                    if(!traxFight.hasEnoughtLong(user, "coins", 5000))
                        return false;

                    user.removeLong("coins", 5000);

                    final String clanName = args[1];
                    final String clanTag = args[2];

                    if(!isFreeClanNameAndTag(player, clanName, clanTag))
                        return false;

                    player.sendMessage(traxFight.getPrefix() + "Erstelle Clan...");

                    traxFight.getLoader().addClanCount();

                    final int clanId = traxFight.getLoader().getClancount();
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm / dd-MM-yyyy");

                    traxFight.getExecutorService().submit(() -> {
                        mysql.execute("INSERT INTO " +
                                "clanstats(ID, NAME, TAG, LEADERUUID, CREATED, KILLS, DEATHS, SLOTS, BANK, CLANCOLOR) " +
                                "VALUES('" + clanId + "', '" + clanName + "', '" + clanTag + "', '" + player.getUniqueId().toString() + "', '" + dateFormat.format(new Date())+ "', '0', '0', '4', '0', '&7')");
                    });

                    user.setInteger("clanid", clanId);

                    traxFight.getClanManager().loadClanByUser(user);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final Clan clan = traxFight.getClanManager().getClanFromUser(user);
                            final ArrayList<String> uuidList = new ArrayList<>();

                            clan.setList("mods", uuidList);

                            user.setClan(clan);

                            uuidList.add(player.getUniqueId().toString());

                            clan.setList("member", uuidList);

                            player.sendMessage(traxFight.getPrefix() + "Dein Clan " + clanName + "§8 (§7" + clanTag + "§8) §7wurde erfolgreich erstellt.");
                            player.sendMessage(traxFight.getPrefix() + "Du kannst deine Freunde einladen, nutze dafür einfach /clan invite <name>.");
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.4f, 0.4f);
                        }
                    }.runTaskLater(traxFight, 40);



                    return false;
                }
            }

            return sendSyntax(player);
        }
        return false;
    }

    private boolean isFreeClanNameAndTag(Player player, String name, String tag){
        final SimpleMySQL mySQL = traxFight.getSimpleMySQL();

        // check for letters and length soon

        final Pattern pattern = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        boolean invalid = matcher.find();

        if(invalid){
            player.sendMessage(traxFight.getPrefix() + "Der Clanname " + name + " beinhaltet ein ungültiges Zeichen.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        matcher = pattern.matcher(tag);
        invalid = matcher.find();

        if(invalid){
            player.sendMessage(traxFight.getPrefix() + "Der Clantag " + tag + " beinhaltet ein ungültiges Zeichen.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        final int tagSize = tag.toCharArray().length;
        final int clanSize = tag.toCharArray().length;

        if(clanSize > 12){
            player.sendMessage(traxFight.getPrefix() + "Der Clanname " + name + " ist zu lang. §8(§7Maximal 12 Zeichen§8)");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        if(tagSize > 5){
            player.sendMessage(traxFight.getPrefix() + "Der Clantag " + name + " ist zu lang. §8(§7Maximal 5 Zeichen§8)");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        int id = mySQL.getInt("clanstats", "id", "name", name.toLowerCase());

        if(id > 0){
            player.sendMessage(traxFight.getPrefix() + "Der Clanname " + name + " ist bereits vergeben.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        if(id == -1){
            player.sendMessage(traxFight.getPrefix() + "Der Clanname " + name + " ist blockiert.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        id = mySQL.getInt("clanstats", "id", "tag", tag.toLowerCase());

        if(id > 0){
            player.sendMessage(traxFight.getPrefix() + "Der Clantag " + name + " ist bereits vergeben oder blockiert.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        if(id == -1){
            player.sendMessage(traxFight.getPrefix() + "Der Clantag " + name + " ist blockiert.");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }


        return true;
    }

    private void sendStatsMessage(Player player, String clanname){
        player.sendMessage("");
        player.sendMessage("§8§l§m---------------------------------------");
        player.sendMessage("§8                          §6§lCLAN");
        player.sendMessage("");

        try {
            if(clanManager.isLoaded(clanname)){
                final Clan clan = clanManager.getClanFromId(clanManager.getNameClans().get(clanname.toLowerCase()));

                player.sendMessage("§8» §7Name§8:§e " + clan.getString("name") + "§8 (" + clan.getString("clancolor") + clan.getString("tag") + "§8) §8┃ » §7ID§8:§e " + clan.getInteger("clanid"));
                player.sendMessage("§8» §7Kills§8: §a" + MathUtil.getLongWithDots(clan.getInteger("kills")) + " §8┃ » §7Tode§8: §c" + MathUtil.getLongWithDots(clan.getInteger("deaths")) + " §8┃ » §7Bank§8: §e" + MathUtil.getLongWithDots(clan.getLong("bank")));
                player.sendMessage("§8» §7Erstellt§8: §e" + clan.getString("created") + " §8┃ » §7Mitglieder§8: §e" + clan.getList("member").size() + " §8┃ » §7Slots§8: §e" + clan.getInteger("slots"));
                player.sendMessage("");
                player.sendMessage("§8§l§m---------------------------------------");

                return;
            }

            final int clanID = mysql.getInt("clanstats", "id", "name", clanname);

            if(clanID < 0){
                player.sendMessage("§7Der Clan §8'§e" + clanname + "§8'§7 existiert nicht.");
                player.sendMessage("");
                player.sendMessage("§8§l§m---------------------------------------");
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
                return;
            }

            clanManager.loadClanById(clanID);

            final Clan clan = clanManager.getClanFromId(clanID);

            traxFight.getExecutorService().submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}

                player.sendMessage("§8» §7Name§8:§e " + clan.getString("name") + "§8 (" + clan.getString("clancolor") + clan.getString("tag") + "§8) §8┃ » §7ID§8:§e " + clan.getInteger("clanid"));
                player.sendMessage("§8» §7Kills§8: §a" + MathUtil.getLongWithDots(clan.getInteger("kills")) + " §8┃ » §7Tode§8: §c" + MathUtil.getLongWithDots(clan.getInteger("deaths")) + " §8┃ » §7Bank§8: §e" + MathUtil.getLongWithDots(clan.getLong("bank")));
                player.sendMessage("§8» §7Erstellt§8: §e" + clan.getString("created") + " §8┃ » §7Mitglieder§8: §e" + clan.getList("member").size() + " §8┃ » §7Slots§8: §e" + clan.getInteger("slots"));
                player.sendMessage("");
                player.sendMessage("§8§l§m---------------------------------------");
            });
        }catch (NullPointerException ex){
            player.sendMessage("§7Der Clan §8'§e" + clanname + "§8'§7 existiert nicht.");
            player.sendMessage("");
            player.sendMessage("§8§l§m---------------------------------------");
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 0.4f, 0.4f);
            ex.printStackTrace();
        }
    }

    private boolean sendSyntax(Player player){

        player.sendMessage("");
        player.sendMessage("§8§l§m---------------------------------------");
        player.sendMessage("§8                                §6§lCLAN");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan create <name> <tag> §8| §7Erstelle deinen eigenen Clan. §8(§7Kostet 5.000 Münzen    §8)"); //
        player.sendMessage("");
        player.sendMessage("§8- §7/clan delete §8| §7Lösche deinen Clan. §8(§4§lClanleader§8)"); //
        player.sendMessage("");
        player.sendMessage("§8- §7/clan stats §8| §7Rufe deine Clanstatistiken auf.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan stats <name> §8| §7Rufe die Clanstatistiken eines Clans auf.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan list §8| §7Liste alle Mitglieder deines Clans auf.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan list <name> §8| §7Liste alle Mitglieder deines Clans auf.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan pay <menge> §8| §7Zahle Münzen in die Clanbank ein.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan invite <spieler> §8| §7Sende einem Spieler eine Claneinladung. §8(§5§lClanmod§8)");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan accept <clan> §8| §7Akzeptiere eine Claneinladung.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan deny <clan> §8| §7Lehne eine Claneinladung ab.");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan promote <spieler> §8| §7Promote ein Clanmitglied zum Clanmod. §8(§4§lClanleader§8)");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan demote <spieler> §8| §7Demote einen Clanmod zum Clanmitglied. §8(§4§lClanleader§8)");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan kick <spieler> §8| §7Kicke ein Clanmitglied aus dem Clan. §8(§5§lMod§8)");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan overwrite <spieler> §8| §7Überschreibe den Clan an ein Clanmitglied. §8(§4§lClanleader§8)");
        player.sendMessage("");
        player.sendMessage("§8- §7/clan leave  §8| §7Verlasse deinen Clan.");
        player.sendMessage("");
        player.sendMessage("§8§l§m---------------------------------------");
        player.sendMessage("");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player){
            final ArrayList<String> returns = new ArrayList<>();

            if(args.length == 1) {
                returns.add("create");
                returns.add("accept");
                returns.add("deny");
                returns.add("invite");
                returns.add("list");
                returns.add("stats");
                returns.add("pay");
                returns.add("bank");
                returns.add("promote");
                returns.add("demote");
                returns.add("kick");
                returns.add("delete");
                returns.add("overwrite");
                returns.add("leave");

                return returns;
            }
        }
        return null;
    }

    private boolean isInclan(User user){
        if(user.getClan() == null){
            final Player player = user.getPlayer();
            player.sendMessage(traxFight.getPrefix() + "Du bist in keinem Clan :c");
            player.playSound(player.getLocation(), Sound.EXPLODE, 0.4f, 0.4f);
            return false;
        }

        return true;
    }
}
