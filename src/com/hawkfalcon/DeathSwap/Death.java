package com.hawkfalcon.DeathSwap;

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

    public DeathSwap p;

    public Death(DeathSwap m) {
        this.p = m;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String n = ((Player) event.getEntity()).getName();
        if (p.game.contains(n)) {
            p.u.message("You died, you lose!", n);
            p.stop.dealWithLeftoverGames(n, true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player pl = event.getPlayer();
        String n = pl.getName();
        if (p.lobby.contains(n)) {
            Location cloc = p.loc.getLocation(p.getConfig().getString("lobby_spawn"));
            event.setRespawnLocation(cloc);
        }
        if (p.game.contains(n)) {
            p.game.remove(n);
            p.u.playerReset(pl);
            Location cloc = p.loc.getLocation(p.getConfig().getString("end_spawn"));
            event.setRespawnLocation(cloc);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String n = event.getPlayer().getName();
        p.loggedoff.add(n);
        if (p.lobby.contains(n)) {
            p.u.message("You left the game!", n);
            p.u.broadcastLobby(n + " left the game!");
            p.lobby.remove(n);
        }
        if (p.game.contains(n)) {
            p.stop.dealWithLeftoverGames(n, false);
            p.lobby.remove(n);
            p.game.remove(n);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String n = event.getPlayer().getName();
        if (p.loggedoff.contains(n)) {
            player.getInventory().clear();
            p.u.clearArmor(player);
            p.u.teleport(n, 0);
        }
        p.loggedoff.remove(n);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String n = event.getPlayer().getName();
        if (p.game.contains(n)) {
            if (p.getConfig().getBoolean("chat_prefix")) {
                event.setFormat("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] <" + n + "> " + message);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            String n = ((Player) event.getEntity()).getName();
            if (p.lobby.contains(n)) {
                event.setCancelled(true);
            }
            if (p.game.contains(n) && p.protect) {
                event.setCancelled(true);
            }
        }
    }
}
