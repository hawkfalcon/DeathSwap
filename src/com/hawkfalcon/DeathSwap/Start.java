package com.hawkfalcon.DeathSwap;

import org.bukkit.entity.Player;

public class Start {

    public DeathSwap p;

    public Start(DeathSwap m) {
        this.p = m;
    }

    public void newGame(String playerone, String playertwo) {
        Player one = p.getServer().getPlayer(playerone);
        Player two = p.getServer().getPlayer(playertwo);
        p.u.broadcast("Game started with " + playerone + " and " + playertwo + "!");
        p.match.put(playerone, playertwo);
        p.lobby.remove(playerone);
        p.lobby.remove(playertwo);
        p.game.add(playerone);
        p.game.add(playertwo);
        one.setHealth(one.getMaxHealth());
        two.setHealth(two.getMaxHealth());
        one.setFoodLevel(20);
        two.setFoodLevel(20);
        one.getInventory().clear();
        two.getInventory().clear();
        p.loc.randomTeleport(playerone, playertwo);
    }
}