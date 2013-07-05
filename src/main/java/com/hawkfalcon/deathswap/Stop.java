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

    public void dealWithLeftoverGames(Player loser, boolean died) {
        if (plugin.match.containsKey(loser.getName())) {
            Player winner = plugin.getServer().getPlayerExact(plugin.match.get(loser.getName()));
            cleanUp(loser, winner, died);
        } else if (plugin.match.containsValue(loser.getName())) {
            Player winner = null;
            for (String key : plugin.match.keySet()) {
                if (plugin.match.get(key).equals(loser.getName())) {
                    winner = plugin.getServer().getPlayerExact(key);
                }
            }
            cleanUp(loser, winner, died);
        }
    }

    public void cleanUp(Player loser, Player winner, boolean died) {
        DeathSwapWinEvent dswe = new DeathSwapWinEvent(winner, loser);
        Bukkit.getServer().getPluginManager().callEvent(dswe);
        //JOPHESTUS IS AWESOME
        plugin.utility.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gameend").replace("%WINNER%", winner.getName()).replace("%LOSER%", loser.getName())));
        if (died) {
            plugin.utility.message(loser + " died, you win!", winner);
        } else {
            plugin.utility.message(loser + " has left the game, you win!", winner);
        }
        plugin.game.remove(winner);
        plugin.utility.playerReset(winner);
        plugin.utility.teleport(winner, 1);
        plugin.match.remove(loser);
        plugin.startgame.remove(loser);
        plugin.match.remove(winner);
        plugin.startgame.remove(winner);
    }
}
