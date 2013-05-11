package com.hawkfalcon.deathswap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Auto implements Listener {

    public DeathSwap plugin;

    public Auto(DeathSwap ds) {
        this.plugin = ds;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        String n = event.getPlayer().getName();
        if(plugin.getConfig().getBoolean("auto_join")) {
            plugin.utility.message("You joined the game!", n);
            plugin.utility.broadcastLobby(n + " joined the game!");
            // mark as in lobby
            plugin.lobby.add(n);
            // teleport to lobby
            plugin.utility.teleport(n, 0);
            plugin.utility.checkForStart();
        }
    }
}
