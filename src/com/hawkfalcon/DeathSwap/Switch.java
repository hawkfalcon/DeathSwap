package com.hawkfalcon.DeathSwap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

public class Switch {

    public DeathSwap p;

    public Switch(DeathSwap m) {
        this.p = m;
    }

    public void switchPlayers() {
        for(String n:p.match.keySet()) {
            String pone = n;
            String ptwo = p.match.get(n);
            p.u.message("Commencing swap!", pone);
            p.u.message("Commencing swap!", ptwo);
            final Player playerone = p.getServer().getPlayer(pone);
            final Player playertwo = p.getServer().getPlayer(ptwo);
            final Location locone = playerone.getLocation();
            final Location loctwo = playertwo.getLocation();
            if (playerone.getVehicle() != null) {
               playerone.leaveVehicle();
            }
            if (playertwo.getVehicle() != null) {
                playertwo.leaveVehicle();
            }
            int task = p.getServer().getScheduler().scheduleSyncDelayedTask(this.p, new Runnable() {
                public void run() {
                    playerone.teleport(loctwo);
                    playertwo.teleport(locone);
                    protect();                
                    }
            }, 1L);
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