package me.adelemphii.birthdaycards.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.CycleButton;
import me.adelemphii.birthdaycards.BirthdayCards;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NoteGUI {

    BirthdayCards plugin;
    public NoteGUI(BirthdayCards plugin) {
        this.plugin = plugin;
    }

    public void openNavigator(Player player, boolean isBirthday) {
        ChestGui gui = new ChestGui(3, ChatColor.DARK_GREEN + "Navigator");
        PaginatedPane pagePane = notesPane(player);

        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navigationPane = new OutlinePane(3, 1, 3, 1);
        pagePane.addPane(0, background);
        pagePane.addPane(0, navigationPane);


        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        List<String> barrierLore = new ArrayList<>();

        barrierMeta.setDisplayName(ChatColor.RED + "Clear Notes!");
        barrierLore.add(ChatColor.RED + "Clear every note you have, deleting them all until next time!");
        if(!isBirthday) {
            barrierLore.add(ChatColor.RED + "You can only do this on your birthday!");
        }
        barrierLore.add("");
        barrierMeta.setLore(barrierLore);

        barrier.setItemMeta(barrierMeta);

        navigationPane.addItem(new GuiItem(barrier, event -> {
            if(isBirthday) {
                plugin.nmu.clearNotes(player);
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.RED + "You can only clear those when it's your birthday!");
            }
        }));

        ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta greenGlassItemMeta = greenGlass.getItemMeta();

        greenGlass.setItemMeta(greenGlassItemMeta);
        navigationPane.addItem(new GuiItem(greenGlass));


        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        List<String> paperLore = new ArrayList<>();

        if(!isBirthday) {
            paperLore.add(ChatColor.RED + "You can only access this on your birthday.");
            paperLore.add(ChatColor.RED + "Be sure to set your birthday using /set MM/DD/YYYY");
        } else {
            paperLore.add(ChatColor.GREEN + "Happy Birthday!");
        }

        paperMeta.setLore(paperLore);
        paperMeta.setDisplayName("Show Notes");
        paper.setItemMeta(paperMeta);

        navigationPane.addItem(new GuiItem(paper, event -> {

            if(isBirthday) {
                pagePane.setPage(1);
                gui.setTitle(ChatColor.DARK_GREEN + "Birthday Notes!");
                gui.update();
            } else {
                player.sendMessage(ChatColor.RED + "It is not your birthday! You can't view these yet!");
            }

        }));

        gui.addPane(pagePane);
        gui.show(player);

    }

    public PaginatedPane notesPane(Player player) {

        PaginatedPane pagePane = new PaginatedPane(0, 0, 9, 3);

        OutlinePane pageOne = new OutlinePane(0, 0, 9, 3);


                /*

                notes:
                  recipient_uuid: UUID
                    item_name:
                      itemstack

                 */

        ArrayList<ItemStack> itemStacks = plugin.nmu.checkNotes(player);

        if(itemStacks != null) {
            for (ItemStack item : itemStacks) {
                pageOne.addItem(new GuiItem(item));
            }
        } else {
            ItemStack sowwy = new ItemStack(Material.PAPER);
            ItemMeta sowwyMeta = sowwy.getItemMeta();
            List<String> sowwyLore = new ArrayList<>();

            sowwyMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "A Gift");
            sowwyLore.add(ChatColor.GREEN + "Happy Birthday!");
            sowwyLore.add(ChatColor.GREEN + "From the BirthdayCards Developer, Adelemphii!");
            sowwyMeta.setLore(sowwyLore);
            sowwy.setItemMeta(sowwyMeta);
            pageOne.addItem(new GuiItem(sowwy));
        }
        pagePane.addPane(1, pageOne);
        return pagePane;
    }

}
