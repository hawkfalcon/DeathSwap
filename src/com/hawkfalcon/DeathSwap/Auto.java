package com.hawkfalcon.DeathSwap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Auto implements Listener {

    public DeathSwap p;

    public Auto(DeathSwap m) {
        this.p = m;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        String n = event.getPlayer().getName();
        if (p.getConfig().getBoolean("auto_join")) {
                p.u.message("You joined the game!", n);
                p.u.broadcastLobby(n + " joined the game!");
                // mark as in lobby
                p.lobby.add(n);
                // teleport to lobby
                p.u.teleport(n, 0);
                p.u.checkForStart();
        }
    }
}
