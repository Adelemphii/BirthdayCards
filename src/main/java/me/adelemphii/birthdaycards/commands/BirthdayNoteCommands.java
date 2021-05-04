package me.adelemphii.birthdaycards.commands;

import me.adelemphii.birthdaycards.BirthdayCards;
import me.adelemphii.birthdaycards.gui.NoteGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class BirthdayNoteCommands implements CommandExecutor {

    BirthdayCards plugin;
    public BirthdayNoteCommands(BirthdayCards plugin) {
        this.plugin = plugin;
    }

    NoteGUI noteGUI = new NoteGUI(JavaPlugin.getPlugin(BirthdayCards.class));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) { return false; }
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("birthdaynote")) {
            if(args.length == 0) {
                // Display fancy help menu
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +
                        "&8-------&dBirthdayCards&8-------\n" +
                        "&7/set &8- &7Set your birthday in the MM/DD/YYYY format!\n" +
                        "&7/birthdaynote &8- &7Shows you this menu!\n" +
                        "&7/birthdaynote give &8- &7Spawns a note for you to run commands on!\n" +
                        "&7/birthdaynote name &8- &7Lets you rename the note! (It has to be yours!)\n" +
                        "&7/birthdaynote desc &8- &7Lets you add a description to your note! " +
                        "P.S. Using underscores ('_') will let you add new lines!\n" +
                        "&7/birthdaynote send &8- &7Lets you tell the plugin who you're sending the note to!\n" +
                        "&7/birthdaynote open &8- &7Opens the BirthdayCard menu which allows you to see all the notes" +
                        " which you have been sent!"));
                return true;
            }

            if(args.length >= 1) {
                switch (args[0]) {
                    case "give":
                        plugin.nmu.spawnNote(player);
                        return true;
                    case "name":
                        StringBuilder name = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            if(args[i] != null) {
                                name.append(args[i] + " ");
                            }
                        }
                        plugin.nmu.setNoteName(player, name.toString());
                        return true;
                    case "desc":
                        StringBuilder desc = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            desc.append(args[i] + " ");
                        }
                        plugin.nmu.setNoteDescription(player, desc.toString());
                        return true;
                    case "send":
                        UUID uuid;

                        try {
                            uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                        } catch(NullPointerException e) {
                            try {
                                uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                            } catch(NullPointerException ex) {
                                player.sendMessage("That player hasn't been online in 30 days.");
                                uuid = null;
                                return false;
                            }
                        }
                        plugin.nmu.signRecipient(uuid, player);
                        return true;
                    case "open":
                        noteGUI.openNavigator(player, plugin.nmu.checkBirthday(player.getUniqueId()));
                    case "check":

                    default:
                        return false;
                }
            }
        }
        return false;
    }
}
