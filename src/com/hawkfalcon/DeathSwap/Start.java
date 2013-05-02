package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Start {

    public DeathSwap p;

    public Start(DeathSwap m) {
        this.p = m;
    }

    public void newGame(String n_one, String n_two) {
        p.u.broadcast(ChatColor.DARK_AQUA + "Game started with " + n_one + " and " + n_two + "!");
        p.match.put(n_one, n_two);
        newGameUtils(n_one);
        newGameUtils(n_two);
        p.startgame.add(n_one);
    }

    public void newGameUtils(String n) {
        Player pl = p.getServer().getPlayer(n);
        p.lobby.remove(n);
        p.game.add(n);
        p.u.playerReset(pl);
        p.loc.randomTeleport(n);
    }
}
