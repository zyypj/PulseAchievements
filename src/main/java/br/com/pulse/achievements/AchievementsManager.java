package br.com.pulse.achievements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AchievementsManager {

    private final PulseAchievements plugin;
    private final Map<UUID, Map<String, Integer>> playerAchievements = new HashMap<>();
    private File databaseFile;
    private FileConfiguration databaseConfig;

    public AchievementsManager(PulseAchievements plugin) {
        this.plugin = plugin;
        loadDatabase();
    }

    // Carrega o arquivo database.yml
    private void loadDatabase() {
        databaseFile = new File(plugin.getDataFolder(), "database.yml");
        if (!databaseFile.exists()) {
            try {
                databaseFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);
        loadPlayerData();
    }

    // Carrega os dados dos jogadores do database.yml
    private void loadPlayerData() {
        for (String uuidStr : databaseConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            Map<String, Integer> achievements = new HashMap<>();
            for (String achievementType : databaseConfig.getConfigurationSection(uuidStr).getKeys(false)) {
                int progress = databaseConfig.getInt(uuidStr + "." + achievementType);
                achievements.put(achievementType, progress);
            }
            playerAchievements.put(uuid, achievements);
        }
    }

    // Salva o progresso dos jogadores no database.yml
    private void saveDatabase() {
        for (Map.Entry<UUID, Map<String, Integer>> entry : playerAchievements.entrySet()) {
            UUID uuid = entry.getKey();
            for (Map.Entry<String, Integer> achievement : entry.getValue().entrySet()) {
                databaseConfig.set(uuid.toString() + "." + achievement.getKey(), achievement.getValue());
            }
        }
        try {
            databaseConfig.save(databaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Atualiza o progresso de uma conquista
    public void updateAchievement(Player player, String type, int progress) {
        UUID uuid = player.getUniqueId();
        Map<String, Integer> achievements = playerAchievements.getOrDefault(uuid, new HashMap<>());

        // Verifica se o jogador já completou o objetivo
        if (achievements.containsKey(type)) {
            int currentProgress = achievements.get(type);
            if (currentProgress >= progress) {
                return;
            }
        }

        // Atualiza o progresso
        achievements.put(type, progress);
        playerAchievements.put(uuid, achievements);
        saveDatabase();

        // Verifica se a conquista foi completada
        checkForCompletion(player, type, progress);
    }

    // Verifica se uma conquista foi completada
    private void checkForCompletion(Player player, String type, int progress) {
        FileConfiguration config = plugin.getConfig();
        List<Map<?, ?>> achievements = config.getMapList("achievements." + type);

        for (int i = 0; i < achievements.size(); i++) {
            Map<?, ?> achievementConfig = achievements.get(i);
            int goal = (int) achievementConfig.get("goal");

            if (progress >= goal) {
                // Avisa o jogador que ele completou a conquista
                String name = (String) achievementConfig.get("name");
                player.sendMessage("§aVocê completou a conquista: " + name + "!");

                // Verifica se existe uma próxima conquista do mesmo tipo
                if (i + 1 < achievements.size()) {
                    Map<?, ?> nextAchievementConfig = achievements.get(i + 1);
                    String nextName = (String) nextAchievementConfig.get("name");
                    player.sendMessage("§ePróxima conquista: " + nextName);
                } else {
                    player.sendMessage("§aVocê completou todas as conquistas deste tipo!");
                }

                // Atualiza o banco de dados
                playerAchievements.get(player.getUniqueId()).put(type, progress);
                saveDatabase();
                return;
            }
        }
    }

    // Obtém o progresso atual de uma conquista
    public int getProgress(Player player, String type) {
        return playerAchievements.getOrDefault(player.getUniqueId(), new HashMap<>()).getOrDefault(type, 0);
    }

    // Obtém o material configurado para uma conquista
    public Material getAchievementMaterial(String type, int tier) {
        FileConfiguration config = plugin.getConfig();
        List<Map<?, ?>> achievements = config.getMapList("achievements." + type);
        if (tier >= 0 && tier < achievements.size()) {
            String materialName = (String) achievements.get(tier).get("material");
            try {
                return Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                // Retorna um material padrão se o material não for encontrado
                return Material.BARRIER;
            }
        }
        return Material.BARRIER; // Material padrão se o tier estiver fora do intervalo
    }

    // Obtém o nome de uma conquista
    public String getAchievementName(String type, int tier) {
        FileConfiguration config = plugin.getConfig();
        List<Map<?, ?>> achievements = config.getMapList("achievements." + type);
        if (tier >= 0 && tier < achievements.size()) {
            return (String) achievements.get(tier).get("name");
        }
        return "Desconhecido"; // Nome padrão se o tier estiver fora do intervalo
    }

    public List<String> getAchievementLore(Player player, String achievementId, int tier) {
        List<String> lore = getAchievementConfigLore(achievementId, tier);
        int progress = getProgress(player, achievementId);
        int goal = getAchievementGoal(achievementId, tier);

        // Substituindo placeholders
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = line.replace("{progress}", formatProgress(progress, goal));
            line = line.replace("{status}", getAchievementStatus(progress, goal));
            lore.set(i, line);
        }

        return lore;
    }


    private String formatProgress(int progress, int goal) {
        return progress >= goal
                ? ChatColor.GREEN + "FEITO!" + ChatColor.GRAY + " (" + ChatColor.GREEN + progress + ChatColor.GRAY + ")"
                : ChatColor.GREEN + "" + progress + ChatColor.GRAY + "/" + ChatColor.GREEN + goal;
    }

    private String getAchievementStatus(int progress, int goal) {
        return progress >= goal ? ChatColor.GREEN + "Nível Concluído!" : ChatColor.RED + "Nível em andamento";
    }

    public Map<String, Map<String, Object>> getAchievements() {
        FileConfiguration config = plugin.getConfig();
        Map<String, Map<String, Object>> achievementsMap = new HashMap<>();

        for (String key : config.getConfigurationSection("achievements").getKeys(false)) {
            Map<String, Object> achievementData = new HashMap<>();
            List<Map<?, ?>> tiers = config.getMapList("achievements." + key);
            for (int i = 0; i < tiers.size(); i++) {
                achievementData.put(String.valueOf(i), tiers.get(i));
            }
            achievementsMap.put(key, achievementData);
        }

        return achievementsMap;
    }

    public List<String> getAchievementConfigLore(String achievementId, int tier) {
        FileConfiguration config = plugin.getConfig();
        return config.getStringList("achievements." + achievementId + "." + tier + ".lore");
    }

    public int getAchievementGoal(String achievementId, int tier) {
        FileConfiguration config = plugin.getConfig();
        return config.getInt("achievements." + achievementId + ".tiers." + tier + ".goal");
    }
}