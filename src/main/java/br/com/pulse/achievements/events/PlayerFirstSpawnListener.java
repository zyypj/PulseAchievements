package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.player.PlayerFirstSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerFirstSpawnListener implements Listener {

    private final AchievementsManager achievementManager;

    public PlayerFirstSpawnListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onPlayerFirstSpawn(PlayerFirstSpawnEvent event) {
        achievementManager.updateAchievement(event.getPlayer(), "games_played",
                achievementManager.getProgress(event.getPlayer(), "games_played") + 1);
    }
}
