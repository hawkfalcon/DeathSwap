package com.hawkfalcon.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.hawkfalcon.deathswap.API.DeathSwapWinEvent;

public class Stop {

    public DeathSwap plugin;

    public Stop(DeathSwap ds) {
        this.plugin = ds;
    }

    public void dealWithLeftoverGames(String loser, boolean died) {
        if (plugin.match.containsKey(loser)) {
            String winner = plugin.match.get(loser);
            cleanUp(loser, winner, died);
        } else if (plugin.match.containsValue(loser)) {
            String pkey = "nobody";
            for (String key : plugin.match.keySet()) {
                if (plugin.match.get(key).equals(loser)) {
                    pkey = key;
                }
            }
            cleanUp(loser, pkey, died);
        }
    }

    public void cleanUp(String loser, String winner, boolean died) {
        DeathSwapWinEvent dswe = new DeathSwapWinEvent(winner, loser);
        Bukkit.getServer().getPluginManager().callEvent(dswe);
        Player other = plugin.getServer().getPlayer(winner);
        plugin.utility.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gameend").replace("%WINNER%", winner).replace("%LOSER%", loser)));
        if (died) {
            plugin.utility.message(loser + " died, you win!", winner);
        } else {
            plugin.utility.message(loser + " has left the game, you win!", winner);
        }
        plugin.game.remove(winner);
        plugin.utility.playerReset(other);
        plugin.utility.teleport(winner, 1);
        plugin.match.remove(loser);
        plugin.startgame.remove(loser);
        plugin.match.remove(winner);
        plugin.startgame.remove(winner);
    }
}
