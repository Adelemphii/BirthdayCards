package me.adelemphii.birthdaycards.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletionUtil implements TabCompleter {
    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("give");
            arguments.add("name");
            arguments.add("desc");
            arguments.add("send");
            arguments.add("open");
        }
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String arg : this.arguments) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(arg);
            }
            return result;
        }
        return null;
    }
}

