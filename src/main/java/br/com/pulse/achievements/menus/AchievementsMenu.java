package br.com.pulse.achievements.menus;

import br.com.pulse.achievements.AchievementsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AchievementsMenu {

    private final AchievementsManager achievementsManager;

    public AchievementsMenu(AchievementsManager achievementsManager) {
        this.achievementsManager = achievementsManager;
    }

    public void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Conquistas do Bed Wars");

        // Definindo slots específicos para as conquistas
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        List<ItemStack> completedItems = new ArrayList<>();
        List<ItemStack> pendingItems = new ArrayList<>();

        for (String achievementId : achievementsManager.getAchievements().keySet()) {
            Map<String, Object> tiers = achievementsManager.getAchievements().get(achievementId);
            for (int tier = 0; tier < tiers.size(); tier++) {
                ItemStack item = createAchievementItem(player, achievementId, tier);
                if (achievementsManager.getProgress(player, achievementId) >= achievementsManager.getAchievementGoal(achievementId, tier)) {
                    // Tier completado
                    completedItems.add(item);
                } else {
                    // Tier não completado
                    pendingItems.add(item);
                }
            }
        }

        // Colocando os itens completados no início da lista
        Collections.reverse(completedItems); // Inverte a ordem para que o Tier I fique antes do Tier II, etc.
        List<ItemStack> allItems = new ArrayList<>(completedItems);
        allItems.addAll(pendingItems);

        for (int i = 0; i < allItems.size() && i < slots.length; i++) {
            menu.setItem(slots[i], allItems.get(i));
        }

        player.openInventory(menu);
    }

    private ItemStack createAchievementItem(Player player, String achievementId, int tier) {
        // Obtém o progresso atual do jogador para a conquista
        int progress = achievementsManager.getProgress(player, achievementId);
        int goal = achievementsManager.getAchievementGoal(achievementId, tier);

        Material material = achievementsManager.getAchievementMaterial(achievementId, tier);
        String name = achievementsManager.getAchievementName(achievementId, tier);
        List<String> lore = achievementsManager.getAchievementLore(player, achievementId, tier);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        meta.setLore(lore);

        // Encanta o item se o tier foi completado
        if (progress >= goal) {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }
}