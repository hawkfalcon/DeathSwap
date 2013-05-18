package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Stop {

    public DeathSwap plugin;

    public Stop(DeathSwap ds) {
        this.plugin = ds;
    }

    public void dealWithLeftoverGames(String n, boolean died) {
        if (plugin.match.containsKey(n)) {
            String o = plugin.match.get(n);
            cleanUp(n, o, died);
        } else if (plugin.match.containsValue(n)) {
            String pkey = "nobody";
            for (String key : plugin.match.keySet()) {
                if (plugin.match.get(key).equals(n)) {
                    pkey = key;
                }
            }
            cleanUp(n, pkey, died);
        }
    }

    public void cleanUp(String n, String o, boolean died) {
        Player other = plugin.getServer().getPlayer(o);
        plugin.utility.broadcast(ChatColor.DARK_AQUA + o + " has won against " + n + "!");
        if (died) {
            plugin.utility.message(n + " died, you win!", o);
        } else {
            plugin.utility.message(n + " has left the game, you win!", o);
        }
        plugin.game.remove(o);
        plugin.utility.playerReset(other);
        plugin.utility.teleport(o, 1);
        plugin.match.remove(n);
        plugin.startgame.remove(n);
        plugin.match.remove(o);
        plugin.startgame.remove(o);
    }
}
