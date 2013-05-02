package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class Protect implements Listener {

    public DeathSwap p;

    public Protect(DeathSwap m) {
        this.p = m;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (p.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (p.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (p.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noCommands(PlayerCommandPreprocessEvent event) {
        String n = event.getPlayer().getName();
        if (p.game.contains(n)) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                if (!event.getMessage().toLowerCase().startsWith("/ds")) {
                    event.setCancelled(true);
                    p.u.message(ChatColor.RED + "You can't use commands while playing!", n);
                }
            }
        }
    }
}
