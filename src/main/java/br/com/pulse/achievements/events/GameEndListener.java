package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.gameplay.GameEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEndListener implements Listener {

    private final AchievementsManager achievementManager;

    public GameEndListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        if (event.getTeamWinner() != null) {
            event.getTeamWinner().getMembers().forEach(player ->
                    achievementManager.updateAchievement(player, "game_win",
                            achievementManager.getProgress(player, "game_win") + 1));
        }
        event.getLosers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                achievementManager.updateAchievement(player, "game_defeat",
                        achievementManager.getProgress(player, "game_defeat") + 1);
            }
        });
    }
}
