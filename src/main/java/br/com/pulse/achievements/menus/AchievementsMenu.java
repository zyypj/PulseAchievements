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
    private static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42};
    private static final int MENU_SIZE = 54;
    private static final int NEXT_PAGE_SLOT = MENU_SIZE - 1;  // Último slot do inventário
    private static final int PREVIOUS_PAGE_SLOT = 45;  // Primeiro slot da segunda linha

    public AchievementsMenu(AchievementsManager achievementsManager) {
        this.achievementsManager = achievementsManager;
    }

    public void openMenu(Player player, int page) {
        Inventory menu = Bukkit.createInventory(null, MENU_SIZE, "Conquistas do Bed Wars");

        // Obtem os itens das conquistas baseados na página e tier desbloqueado
        List<ItemStack> items = getAchievementItems(player);

        // Calcula o índice de início e fim para a página atual
        int start = (page - 1) * SLOTS.length;
        int end = Math.min(start + SLOTS.length, items.size());

        // Adiciona os itens da página ao menu
        for (int i = start; i < end; i++) {
            menu.setItem(SLOTS[i - start], items.get(i));
        }

        // Adiciona a seta de navegação se houver mais páginas
        if (end < items.size()) {
            menu.setItem(NEXT_PAGE_SLOT, createNavigationItem("Próxima Página", Material.ARROW));
        }

        if (page > 1) {
            menu.setItem(PREVIOUS_PAGE_SLOT, createNavigationItem("Página Anterior", Material.ARROW));
        }

        player.openInventory(menu);
    }

    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        item.setItemMeta(meta);
        return item;
    }

    private List<ItemStack> getAchievementItems(Player player) {
        List<ItemStack> items = new ArrayList<>();

        for (String achievementId : achievementsManager.getAchievements().keySet()) {
            Map<String, Object> tiers = achievementsManager.getAchievements().get(achievementId);
            int maxTierUnlocked = getMaxTierUnlocked(player, achievementId);

            // Adiciona o tier atual e os anteriores completados
            for (int tier = 0; tier <= maxTierUnlocked; tier++) {
                ItemStack item = createAchievementItem(player, achievementId, tier);
                items.add(item);
            }

            // Adiciona o próximo tier desbloqueável, se existir
            if (maxTierUnlocked + 1 < tiers.size()) {
                ItemStack nextTierItem = createAchievementItem(player, achievementId, maxTierUnlocked + 1);
                items.add(nextTierItem);
            }
        }

        return items;
    }

    private int getMaxTierUnlocked(Player player, String achievementId) {
        int progress = achievementsManager.getProgress(player, achievementId);
        int maxTierUnlocked = 0;

        // Itera sobre os tiers para encontrar o máximo desbloqueado
        for (int tier = 0; tier < achievementsManager.getAchievements().get(achievementId).size(); tier++) {
            int goal = achievementsManager.getAchievementGoal(achievementId, tier);
            if (progress >= goal) {
                maxTierUnlocked = tier;
            } else {
                break;
            }
        }

        return maxTierUnlocked;
    }

    private ItemStack createAchievementItem(Player player, String achievementId, int tier) {
        int progress = achievementsManager.getProgress(player, achievementId);
        int goal = achievementsManager.getAchievementGoal(achievementId, tier);

        Material material = achievementsManager.getAchievementMaterial(achievementId, tier);
        String name = achievementsManager.getAchievementName(achievementId, tier);
        List<String> lore = achievementsManager.getAchievementLore(player, achievementId, tier);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        meta.setLore(lore);

        if (progress >= goal) {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }
}