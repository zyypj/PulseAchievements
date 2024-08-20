package br.com.pulse.achievements.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        // Verifica se o inventário é o menu de conquistas
        if (inventory.getTitle().equals("Conquistas do Bed Wars")) {
            event.setCancelled(true); // Cancela qualquer interação no menu
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