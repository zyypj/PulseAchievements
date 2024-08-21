package br.com.pulse.achievements.menus;

import br.com.pulse.achievements.AchievementsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AchievementsMenu {

    private final AchievementsManager achievementsManager;

    public AchievementsMenu(AchievementsManager achievementsManager) {
        this.achievementsManager = achievementsManager;
    }

    public void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "Conquistas do Bed Wars");

        // Definindo slots específicos para as conquistas
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        int index = 0;
        for (String achievementId : achievementsManager.getAchievements().keySet()) {
            if (index >= slots.length) break;

            ItemStack item = createAchievementItem(player, achievementId);
            menu.setItem(slots[index], item);

            index++;
        }

        player.openInventory(menu);
    }

    private ItemStack createAchievementItem(Player player, String achievementId) {
        // Obtém o progresso atual do jogador para a conquista
        int progress = achievementsManager.getProgress(player, achievementId);

        // Determina o tier baseado no progresso
        int tier = 0;
        while (tier + 1 < achievementsManager.getAchievements().get(achievementId).size() &&
                progress >= achievementsManager.getAchievementGoal(achievementId, tier)) {
            tier++;
        }

        Material material = achievementsManager.getAchievementMaterial(achievementId, tier);
        String name = achievementsManager.getAchievementName(achievementId, tier);
        List<String> lore = achievementsManager.getAchievementLore(player, achievementId, tier);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}