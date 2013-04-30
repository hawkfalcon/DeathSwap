package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Stop {

    public DeathSwap p;

    public Stop(DeathSwap m) {
        this.p = m;
    }

    public void dealWithLeftoverGames(String n, boolean died) {
        if(p.match.containsKey(n)) {
            String o = p.match.get(n);
            cleanUp(n, o, died);
        }
        else if(p.match.containsValue(n)) {
            String pkey = "nobody";
            for(String key:p.match.keySet()) {
                if(p.match.get(key).equals(n)) {
                    pkey = key;
                }
            }
            cleanUp(n, pkey, died);
        }
    }
    public void cleanUp(String n, String o, boolean died){
         Player other = p.getServer().getPlayer(o);
    	 p.u.broadcast(ChatColor.DARK_AQUA + o + " has won against " + n +"!");
         if(died) {
             p.u.message(n + " died, you win!", o);
         } else {
             p.u.message(n + " has left the game, you win!", o);
         }
         p.game.remove(o);
         other.getInventory().clear();
         p.u.clearArmor(other);
         p.u.teleport(o, 1);
         p.match.remove(n);
         p.startgame.remove(n);
    }
}