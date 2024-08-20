package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.upgrades.UpgradeBuyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpgradeBuyListener implements Listener {

    private final AchievementsManager achievementManager;

    public UpgradeBuyListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onUpgradeBuy(UpgradeBuyEvent event) {
        achievementManager.updateAchievement(event.getPlayer(), "upgrade_buy",
                achievementManager.getProgress(event.getPlayer(), "upgrade_buy") + 1);
    }
}