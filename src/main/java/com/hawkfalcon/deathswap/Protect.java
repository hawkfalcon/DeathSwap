package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class Protect implements Listener {

    public DeathSwap plugin;

    public Protect(DeathSwap ds) {
        this.plugin = ds;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if(plugin.lobby.contains(event.getPlayer().getName())) {
            if(!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(plugin.lobby.contains(event.getPlayer().getName())) {
            if(!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(plugin.lobby.contains(event.getPlayer().getName())) {
            if(!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noCommands(PlayerCommandPreprocessEvent event) {
        String n = event.getPlayer().getName();
        if(plugin.game.contains(n)) {
            if(!event.getPlayer().hasPermission("deathswap.bypass")) {
                if(!event.getMessage().toLowerCase().startsWith("/ds")) {
                    event.setCancelled(true);
                    plugin.utility.message(ChatColor.RED + "You can't use commands while playing!", n);
                }
            }
        }
    }
}
