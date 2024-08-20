package br.com.pulse.achievements.events;

import br.com.pulse.achievements.AchievementsManager;
import br.com.pulse.achievements.PulseAchievements;
import com.tomkeuper.bedwars.api.events.shop.ShopBuyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopBuyListener implements Listener {

    private final AchievementsManager achievementManager;

    public ShopBuyListener(PulseAchievements plugin) {
        this.achievementManager = plugin.getAchievementManager();
    }

    @EventHandler
    public void onShopBuy(ShopBuyEvent event) {
        achievementManager.updateAchievement(event.getBuyer(), "shop_buy",
                achievementManager.getProgress(event.getBuyer(), "shop_buy") + 1);
    }
}