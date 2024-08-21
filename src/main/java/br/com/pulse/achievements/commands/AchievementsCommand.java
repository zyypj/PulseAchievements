package br.com.pulse.achievements.commands;

import br.com.pulse.achievements.menus.AchievementsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementsCommand implements CommandExecutor {

    private final AchievementsMenu achievementsMenu;

    public AchievementsCommand(AchievementsMenu achievementsMenu) {
        this.achievementsMenu = achievementsMenu;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                achievementsMenu.openMenu(player);
                return true;
            } else {
                sender.sendMessage("§cComando não encontrado ou você não tem permissão!");
            }
        }
        sender.sendMessage("Este comando só pode ser usado por jogadores.");
        return false;
    }
}