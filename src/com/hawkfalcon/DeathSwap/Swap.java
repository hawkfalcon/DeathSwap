package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Swap {

    public DeathSwap p;

    public Swap(DeathSwap m) {
        this.p = m;
    }

    public void switchPlayers() {
        for (String n : p.match.keySet()) {
            final String n_one = n;
            final String n_two = p.match.get(n);
            //skips the first swap 
            if (!p.startgame.contains(n_one)) {
                final Player pone = p.getServer().getPlayer(n_one);
                final Player ptwo = p.getServer().getPlayer(n_two);
                switchUtil(n_one, pone);
                switchUtil(n_two, ptwo);
                final Location locone = pone.getLocation();
                final Location loctwo = ptwo.getLocation();
                int task = p.getServer().getScheduler().scheduleSyncDelayedTask(this.p, new Runnable() {
                    public void run() {
                        pone.teleport(loctwo);
                        ptwo.teleport(locone);
                        if (p.getConfig().getBoolean("swap_sound")) {
                            p.u.swapEffects(locone);
                            p.u.swapEffects(loctwo);
                        }
                        protect();
                    }
                }, 1L);
            } else {
                p.startgame.remove(n_one);
            }
        }

    }

    public void switchUtil(String n, Player player) {
        p.u.message(ChatColor.BOLD + "Commencing swap!", n);
        if (player.getVehicle() != null) {
            player.leaveVehicle();
        }
    }

    public void protect() {
        p.protect = true;
        int task = p.getServer().getScheduler().scheduleSyncDelayedTask(this.p, new Runnable() {
            public void run() {
                p.protect = false;
            }
        }, 20L * 5);
    }
}
