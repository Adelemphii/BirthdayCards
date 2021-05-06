package me.adelemphii.birthdaycards.events;

import me.adelemphii.birthdaycards.BirthdayCards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class BirthdayEvents implements Listener {

    public BirthdayCards plugin;
    public BirthdayEvents(BirthdayCards plugin) {
        this.plugin = plugin;
    }

    HashSet<UUID> birthdayFolk = new HashSet<>();

    String recipient;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(plugin.config.getConfig().contains("players." + uuid)) {

            // Prints year-month-day
            String date = plugin.config.getConfig().getString("players." + uuid + ".birthday");

            String[] dateArray = date.split("-");

            int dateMonth = Integer.parseInt(dateArray[1]);
            int dateDay = Integer.parseInt(dateArray[2]);

            int localDateMonth = LocalDate.now().getMonthValue();
            int localDateDay = LocalDate.now().getDayOfMonth();

            if((dateMonth == localDateMonth) && (dateDay == localDateDay)) {
                birthdayFolk.add(player.getUniqueId());
            } else {
                if(birthdayFolk.remove(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + "I hope you had a great birthday!");
                }
            }

        }
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        if(birthdayFolk.contains(event.getPlayer().getUniqueId())) {
            event.setFormat(ChatColor.LIGHT_PURPLE + "[BDAY] %s: %s");
        }
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getHand() == EquipmentSlot.OFF_HAND) return;

        if(player.getInventory().getItemInOffHand().getType() == Material.AIR)
            return;
        ItemStack item = player.getInventory().getItemInOffHand();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        if(event.getAction() == Action.LEFT_CLICK_AIR) {
            if(player.isSneaking()) {

                for(String loreExisting : lore) {
                    if(loreExisting.contains("█")) {
                        recipient = loreExisting.replace("█", "");
                    }
                }

                plugin.config.getConfig().set("notes." + recipient + "." + item.getItemMeta().getDisplayName(), item);
                plugin.config.saveConfig();

                player.getInventory().setItemInOffHand(null);
                player.sendMessage(ChatColor.GREEN + "Card sent to " + Bukkit.getPlayer(UUID.fromString(recipient)).getDisplayName());
            }
        }
    }
}
