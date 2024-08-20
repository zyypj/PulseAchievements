package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.player.PlayerKillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerKillListener implements Listener {

    private final AchievementsManager achievementManager;

    public PlayerKillListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent event) {
        achievementManager.updateAchievement(event.getKiller(), "kill",
                achievementManager.getProgress(event.getKiller(), "kill") + 1);

        if (event.getCause().isFinalKill()) {
            achievementManager.updateAchievement(event.getKiller(), "final_kill",
                    achievementManager.getProgress(event.getKiller(), "final_kill") + 1);
        }
    }
}
