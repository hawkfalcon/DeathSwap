package com.hawkfalcon.deathswap.game;

import com.hawkfalcon.deathswap.DeathSwap;
import org.bukkit.entity.Player;

public class Leave {

    public DeathSwap plugin;

    public Leave(DeathSwap ds) {
        this.plugin = ds;
    }

    public void leave(Player player, boolean died) {
        String name = player.getName();
        if (plugin.lobby.contains(name)) {
            plugin.utility.broadcastLobby(name + " left the game!");
            plugin.lobby.remove(name);
            plugin.utility.teleport(player, 1);
        }
        if (plugin.game.contains(name)) {
            plugin.utility.restorePlayer(player);
            plugin.utility.teleport(player, 1);
        }
    }
}
