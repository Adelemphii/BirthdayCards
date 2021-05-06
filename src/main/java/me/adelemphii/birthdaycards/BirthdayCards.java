package me.adelemphii.birthdaycards;

import me.adelemphii.birthdaycards.commands.BirthdayNoteCommands;
import me.adelemphii.birthdaycards.commands.SetBirthdayCommand;
import me.adelemphii.birthdaycards.events.BirthdayEvents;
import me.adelemphii.birthdaycards.util.ConfigManager;
import me.adelemphii.birthdaycards.util.NoteMakerUtil;
import me.adelemphii.birthdaycards.util.TabCompletionUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BirthdayCards extends JavaPlugin {

    public ConfigManager config;

    public NoteMakerUtil nmu;

    @Override
    public void onEnable() {
        config = new ConfigManager(this);
        nmu = new NoteMakerUtil(this);

        registerCE();
    }

    public void onDisable() {
    }

    // Register commands and events here
    public void registerCE() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new BirthdayEvents(this), this);

        getCommand("set").setExecutor(new SetBirthdayCommand(this));
        getCommand("bclear").setExecutor(new SetBirthdayCommand(this));
        getCommand("birthdaynote").setExecutor(new BirthdayNoteCommands(this));
        getCommand("birthdaynote").setTabCompleter(new TabCompletionUtil());
    }


}
