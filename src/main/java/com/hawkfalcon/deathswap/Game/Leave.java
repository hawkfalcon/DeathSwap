package com.hawkfalcon.deathswap.Game;

import com.hawkfalcon.deathswap.DeathSwap;
import org.bukkit.entity.Player;

public class Leave {

    public DeathSwap plugin;

    public Leave(DeathSwap ds) {
        this.plugin = ds;
    }
    public void leave(Player player) {
        String name = player.getName();
        if (plugin.lobby.contains(name)) {
            plugin.utility.message("You left the game!", player);
            plugin.utility.broadcastLobby(name + " left the game!");
            plugin.lobby.remove(name);
            plugin.utility.teleport(player, 1);
        }
        if (plugin.game.contains(name)) {
            plugin.game.remove(name);
            plugin.winGame.winGame(player, false);
            player.getInventory().clear();
            plugin.utility.clearArmor(player);
            plugin.utility.teleport(player, 1);
        }
    }
}
