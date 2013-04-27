package com.hawkfalcon.deathswap;

import org.bukkit.entity.Player;

public class Stop {

    public DeathSwap p;

    public Stop(DeathSwap m) {
        this.p = m;
    }

    public void dealWithLeftoverGames(String n, boolean died) {
        if(p.match.containsKey(n)) {
            String other = p.match.get(n);
            Player o = p.getServer().getPlayer(other);
            if(died) {
                p.u.message(n + " died, you win!", p.match.get(n));
            } else {
                p.u.message(n + " has left the game, you win!", p.match.get(n));
            }
            p.game.remove(other);
            o.getInventory().clear();
            p.u.clearArmor(o);
            p.u.teleport(other, 1);
            p.match.remove(n);
        }
        else if(p.match.containsValue(n)) {
            String pkey = "nobody";
            for(String key:p.match.keySet()) {
                if(p.match.get(key).equals(n)) {
                    pkey = key;
                }
            }
            Player o = p.getServer().getPlayer(pkey);
            if(died) {
                p.u.message(n + " died, you win!", pkey);
            } else {
                p.u.message(n + " has left the game, you win!", pkey);
            }
            p.game.remove(pkey);
            o.getInventory().clear();
            p.u.clearArmor(o);
            p.u.teleport(pkey, 1);
            p.match.remove(pkey);
        }
    }
}