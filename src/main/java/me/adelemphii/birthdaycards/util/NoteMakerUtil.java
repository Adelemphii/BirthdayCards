package me.adelemphii.birthdaycards.util;

import me.adelemphii.birthdaycards.BirthdayCards;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteMakerUtil {

    BirthdayCards plugin;
    public NoteMakerUtil(BirthdayCards plugin) {
        this.plugin = plugin;
    }

    public void spawnNote(Player player) {
        ItemStack note = new ItemStack(Material.PAPER);
        ItemMeta noteMeta = note.getItemMeta();
        PersistentDataContainer container = noteMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(BirthdayCards.getPlugin(BirthdayCards.class), "BirthdayNote");

        noteMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        container.set(key, PersistentDataType.STRING, "From_" + player.getUniqueId());
        note.setItemMeta(noteMeta);

        if(player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Your inventory is full! Please dump before trying to spawn a note!");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Note spawned! Use /birthdaynote to see all the commands you can use to modify this!");
        player.getInventory().addItem(note);
    }

    public void setNoteName(Player player, String name) {

        ItemStack item = player.getInventory().getItemInMainHand();
        NamespacedKey key = new NamespacedKey(BirthdayCards.getPlugin(BirthdayCards.class), "BirthdayNote");

        if(item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if(container.get(key, PersistentDataType.STRING).equalsIgnoreCase("from_" + player.getUniqueId())) {
                ItemMeta itemMeta = item.getItemMeta();

                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                item.setItemMeta(itemMeta);
                player.sendMessage(ChatColor.GREEN + "Note renamed to " + name);
            } else {
                player.sendMessage(ChatColor.RED + "This is not your note, please use the command on a note you own!");
            }
        }
    }

    public void setNoteDescription(Player player, String description) {

        ItemStack item = player.getInventory().getItemInMainHand();
        NamespacedKey key = new NamespacedKey(BirthdayCards.getPlugin(BirthdayCards.class), "BirthdayNote");

        if(item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if(container.get(key, PersistentDataType.STRING).equalsIgnoreCase("from_" + player.getUniqueId())) {
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = new ArrayList<>();
                String[] descLines = description.split("_");

                for(String i : descLines) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&r&7" + i));
                }
                lore.add("");
                lore.add(ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "From: " + player.getName() + ChatColor.DARK_GRAY + ")");
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.sendMessage(ChatColor.GREEN + "Description set!");
            } else {
                player.sendMessage(ChatColor.RED + "This is not your note, please use the command on a note you own!");
            }
        }
    }

    public void signRecipient(UUID uuid, Player player) {

        ItemStack item = player.getInventory().getItemInMainHand();
        NamespacedKey key = new NamespacedKey(BirthdayCards.getPlugin(BirthdayCards.class), "BirthdayNote");

        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if (container.get(key, PersistentDataType.STRING).equalsIgnoreCase("from_" + player.getUniqueId())) {
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = new ArrayList<>();

                if(itemMeta.getLore() == null) {
                    player.sendMessage(ChatColor.RED + "You need to write a description before defining a recipient!");
                    return;
                }
                for(String existingLore : itemMeta.getLore()) {
                    lore.add(existingLore);
                }

                lore.add("");
                lore.add("█" + uuid.toString());

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCard signed!" +
                        " Make sure to note that you can not edit this after you send it. " +
                        "&cWhen you're ready, hold the note in your off-hand and shift + left-click the air!"));
            } else {
                player.sendMessage(ChatColor.RED + "This is not your note, please use the command on a note you own!");
            }
        }
    }

    public boolean checkBirthday(UUID uuid) {

        if(plugin.config.getConfig().contains("players." + uuid)) {

            // Prints year-month-day
            String date = plugin.config.getConfig().getString("players." + uuid + ".birthday");
            LocalDate localDate = LocalDate.now();

            String[] dateArray = date.split("-");

            int dateMonth = Integer.parseInt(dateArray[1]);
            int dateDay = Integer.parseInt(dateArray[2]);

            int localDateMonth = LocalDate.now().getMonthValue();
            int localDateDay = LocalDate.now().getDayOfMonth();

            if((dateMonth == localDateMonth) && (dateDay == localDateDay)) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public ArrayList<ItemStack> checkNotes(Player player) {
        String uuid = player.getUniqueId().toString();

                /*

                notes:
                  recipient_uuid: UUID
                    item_name:
                      itemstack

                 */

        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        if(plugin.config.getConfig().getConfigurationSection("notes." + uuid) == null) return null;

        for (String s : plugin.config.getConfig().getConfigurationSection("notes." + uuid).getKeys(false)) {
            ItemStack itemStack = plugin.config.getConfig().getItemStack("notes." + uuid + "." + s);
            itemStacks.add(itemStack);
        }
        return itemStacks;
    }

    public void clearNotes(Player player) {
        String uuid = player.getUniqueId().toString();

        plugin.config.getConfig().set("notes." + uuid, null);
        plugin.config.saveConfig();
        player.sendMessage(ChatColor.RED + "You have cleared all your birthday notes!");
    }

}
