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

    public void newGame(Player playerone, Player playertwo) {
        String nameone = playerone.getName();
        String nametwo = playertwo.getName();
        DeathSwapNewGameEvent dsnge = new DeathSwapNewGameEvent(playerone, playertwo);
        Bukkit.getServer().getPluginManager().callEvent(dsnge);
        plugin.utility.broadcast(ChatColor.DARK_AQUA + "Game started with " + nameone + " and " + nametwo + "!");
        plugin.match.put(nameone, nametwo);
        plugin.loc.randomTeleport(playerone, playertwo);
        newGameUtils(playerone);
        newGameUtils(playertwo);
        plugin.startgame.add(nameone);
    }

    public void newGameUtils(Player player) {
        String name = player.getName();
        plugin.lobby.remove(name);
        plugin.game.add(name);
        plugin.utility.playerReset(player);
    }
}
