package me.adelemphii.birthdaycards.commands;

import me.adelemphii.birthdaycards.BirthdayCards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;

public class SetBirthdayCommand implements CommandExecutor {

    BirthdayCards plugin;
    public SetBirthdayCommand(BirthdayCards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("bclear")) {
            plugin.config.deleteConfig();
            player.sendMessage(ChatColor.GREEN + "Database cleared!");
        }

        if(cmd.getName().equalsIgnoreCase("set")) {
            if(args.length < 3) {
                return false;
            }

            if(isNum(args[0]) && isNum(args[1]) && isNum(args[2])) {

                int month = Integer.parseInt(args[0]);
                int day = Integer.parseInt(args[1]);
                int year = Integer.parseInt(args[2]);

                String birthDate = new StringBuilder().append(year).append('-').append(month).append('-').append(day).toString();

                if((month <= 12 && month >= 1) && (day <= 30 && day >= 1)
                && (year <= LocalDate.now().getYear() && year >= 1900)) {

                    plugin.config.getConfig().set("players." + player.getUniqueId() + ".birthday", birthDate);
                    plugin.config.saveConfig();

                    sender.sendMessage(ChatColor.GREEN + "Set your birthday to " + ChatColor.DARK_GREEN + "" + ChatColor.ITALIC
                    + month + "/" + day + "/" + year + ChatColor.GREEN + "!");
                    return true;
                }

            }

        }

        return false;
    }

    // Check if input is a number
    public boolean isNum(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
