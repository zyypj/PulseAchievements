package br.com.pulse.achievements;

import br.com.pulse.achievements.commands.AchievementsCommand;
import br.com.pulse.achievements.events.*;
import br.com.pulse.achievements.listeners.MenuListener;
import br.com.pulse.achievements.menus.AchievementsMenu;
import org.bukkit.plugin.java.JavaPlugin;

public class PulseAchievements extends JavaPlugin {

    private AchievementsManager achievementManager;
    private AchievementsMenu achievementsMenu;

    @Override
    public void onEnable() {
        // Carregar configuração e inicializar sistema de conquistas
        saveDefaultConfig();
        achievementManager = new AchievementsManager(this);
        achievementsMenu = new AchievementsMenu(achievementManager);
        getServer().getPluginManager().registerEvents(new MenuListener(achievementsMenu), this);

        // Registrar eventos de conquistas
        getServer().getPluginManager().registerEvents(new GameEndListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerBedBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFirstSpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getServer().getPluginManager().registerEvents(new ShopBuyListener(this), this);
        getServer().getPluginManager().registerEvents(new UpgradeBuyListener(this), this);

        getCommand("achievements").setExecutor(new AchievementsCommand(achievementsMenu));
    }

    public AchievementsManager getAchievementManager() {
        return achievementManager;
    }

    public AchievementsMenu getAchievementsMenu() {
        return achievementsMenu;
    }
}