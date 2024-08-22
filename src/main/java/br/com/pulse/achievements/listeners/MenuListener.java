package br.com.pulse.achievements.listeners;

import br.com.pulse.achievements.menus.AchievementsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private final AchievementsMenu achievementsMenu;

    public MenuListener(AchievementsMenu achievementsMenu) {
        this.achievementsMenu = achievementsMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (inventory.getTitle().equals("Conquistas do Bed Wars")) {
            event.setCancelled(true);

            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            String itemName = clickedItem.getItemMeta().getDisplayName();
            if (itemName.equals("§aPróxima Página")) {
                achievementsMenu.openMenu(player, 2);
            } else if (itemName.equals("§aPágina Anterior")) {
                achievementsMenu.openMenu(player, 1);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        // Verifica se o inventário é o menu de conquistas
        if (inventory.getTitle().equals("Conquistas do Bed Wars")) {
            event.setCancelled(true); // Cancela qualquer arraste de itens no menu
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        Inventory inventory = event.getInventory();

        // Verifica se o inventário é o menu de conquistas
        if (inventory.getTitle().equals("Conquistas do Bed Wars")) {
            event.setCancelled(true); // Cancela qualquer outra interação no menu
        }
    }
}