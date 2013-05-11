package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Death implements Listener {

    public DeathSwap plugin;

    public Death(DeathSwap ds) {
        this.plugin = ds;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String n = ((Player) event.getEntity()).getName();
        if(plugin.game.contains(n)) {
            plugin.utility.message("You died, you lose!", n);
            plugin.stop.dealWithLeftoverGames(n, true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if(plugin.lobby.contains(name)) {
            Location cloc = plugin.loc.getLocation(plugin.getConfig().getString("lobby_spawn"));
            event.setRespawnLocation(cloc);
        }
        if(plugin.game.contains(name)) {
            plugin.game.remove(name);
            plugin.utility.playerReset(player);
            Location cloc = plugin.loc.getLocation(plugin.getConfig().getString("end_spawn"));
            event.setRespawnLocation(cloc);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        plugin.loggedoff.add(name);
        if(plugin.lobby.contains(name)) {
            plugin.utility.message("You left the game!", name);
            plugin.utility.broadcastLobby(name + " left the game!");
            plugin.lobby.remove(name);
        }
        if(plugin.game.contains(name)) {
            plugin.stop.dealWithLeftoverGames(name, false);
            plugin.lobby.remove(name);
            plugin.game.remove(name);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = event.getPlayer().getName();
        if(plugin.loggedoff.contains(name)) {
            player.getInventory().clear();
            plugin.utility.clearArmor(player);
            plugin.utility.teleport(name, 0);
        }
        plugin.loggedoff.remove(name);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String name = event.getPlayer().getName();
        if(plugin.game.contains(name)) {
            if(plugin.getConfig().getBoolean("chat_prefix")) {
                event.setFormat("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] <" + name + "> " + message);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            String name = ((Player) event.getEntity()).getName();
            if(plugin.lobby.contains(name)) {
                event.setCancelled(true);
            }
            if(plugin.game.contains(name) && plugin.protect) {
                event.setCancelled(true);
            }
        }
    }
}
