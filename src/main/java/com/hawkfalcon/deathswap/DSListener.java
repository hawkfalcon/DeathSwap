package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DSListener implements Listener {

    DeathSwap plugin;

    public DSListener(DeathSwap ds) {
        this.plugin = ds;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign.getLine(0).equalsIgnoreCase("[DeathSwap]") && sign.getLine(1).equalsIgnoreCase("join")) {
                    Player player = event.getPlayer();
                    plugin.join(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (plugin.lobby.contains(event.getPlayer().getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noCommands(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.game.contains(player.getName())) {
            if (!event.getPlayer().hasPermission("deathswap.bypass")) {
                if (!event.getMessage().toLowerCase().startsWith("/ds")) {
                    event.setCancelled(true);
                    plugin.utility.message(ChatColor.RED + "You can't use commands while playing!", player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();
        if (plugin.game.contains(player.getName())) {
            plugin.utility.message("You died, you lose!", player);
            plugin.stop.dealWithLeftoverGames(player, true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (plugin.lobby.contains(name)) {
            Location cloc = plugin.loc.getLocation(plugin.getConfig().getString("lobby_spawn"));
            event.setRespawnLocation(cloc);
        }
        if (plugin.game.contains(name)) {
            plugin.game.remove(name);
            plugin.utility.playerReset(player);
            Location cloc = plugin.loc.getLocation(plugin.getConfig().getString("end_spawn"));
            event.setRespawnLocation(cloc);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (plugin.lobby.contains(name)) {
            plugin.loggedoff.add(name);
            plugin.utility.message("You left the game!", player);
            plugin.utility.broadcastLobby(name + " left the game!");
            plugin.lobby.remove(name);
        }
        if (plugin.game.contains(name)) {
            plugin.loggedoff.add(name);
            plugin.stop.dealWithLeftoverGames(player, false);
            plugin.lobby.remove(name);
            plugin.game.remove(name);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = event.getPlayer().getName();
        if (plugin.loggedoff.contains(name)) {
            player.getInventory().clear();
            plugin.utility.clearArmor(player);
            plugin.utility.teleport(player, 0);
        }
        plugin.loggedoff.remove(name);
        //
        if (plugin.getConfig().getBoolean("auto_join")) {
            plugin.utility.message("You joined the game!", player);
            plugin.utility.broadcastLobby(name + " joined the game!");
            // mark as in lobby
            plugin.lobby.add(name);
            // teleport to lobby
            plugin.utility.teleport(player, 0);
            plugin.utility.checkForStart();
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String name = event.getPlayer().getName();
        if (plugin.game.contains(name)) {
            if (plugin.getConfig().getBoolean("chat_prefix")) {
                event.setFormat(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "<" + name + ">" + message));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            String name = ((Player) event.getEntity()).getName();
            if (plugin.lobby.contains(name)) {
                event.setCancelled(true);
            }
            if (plugin.game.contains(name) && plugin.protect) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntitysDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            String name = ((Player) event.getEntity()).getName();
            if (plugin.lobby.contains(name)) {
                event.setCancelled(true);
            }
            if (plugin.game.contains(name) && plugin.protect) {
                event.setCancelled(true);
            }
        }
    }
}
