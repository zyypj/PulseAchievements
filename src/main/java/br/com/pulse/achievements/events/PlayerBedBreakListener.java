package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.player.PlayerBedBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerBedBreakListener implements Listener {

    private final AchievementsManager achievementManager;

    public PlayerBedBreakListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onPlayerBedBreak(PlayerBedBreakEvent event) {
        achievementManager.updateAchievement(event.getPlayer(), "bed_broken",
                achievementManager.getProgress(event.getPlayer(), "bed_broken") + 1);
    }
}