package com.hawkfalcon.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.hawkfalcon.deathswap.API.DeathSwapNewGameEvent;

public class Start {

    public DeathSwap plugin;

    public Start(DeathSwap ds) {
        this.plugin = ds;
    }

    public void newGame(String n_one, String n_two) {
        DeathSwapNewGameEvent dsnge = new DeathSwapNewGameEvent(n_one, n_two);
        Bukkit.getServer().getPluginManager().callEvent(dsnge);
        plugin.utility.broadcast(ChatColor.DARK_AQUA + "Game started with " + n_one + " and " + n_two + "!");
        plugin.match.put(n_one, n_two);
        plugin.loc.randomTeleport(n_one, n_two);
        newGameUtils(n_one);
        newGameUtils(n_two);
        plugin.startgame.add(n_one);
    }

    public void newGameUtils(String n) {
        Player pl = plugin.getServer().getPlayer(n);
        plugin.lobby.remove(n);
        plugin.game.add(n);
        plugin.utility.playerReset(pl);
    }
}
