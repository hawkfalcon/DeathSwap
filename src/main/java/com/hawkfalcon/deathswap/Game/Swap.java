package com.hawkfalcon.deathswap.Game;

import com.hawkfalcon.deathswap.API.DeathSwapSwapEvent;
import com.hawkfalcon.deathswap.DeathSwap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Swap {

    public DeathSwap plugin;

    public Swap(DeathSwap ds) {
        this.plugin = ds;
    }

    public void switchPlayers() {
        DeathSwapSwapEvent dsse = new DeathSwapSwapEvent();
        Bukkit.getServer().getPluginManager().callEvent(dsse);
        for (String name : plugin.match.keySet()) {
            final Player pone = plugin.getServer().getPlayer(name);
            final Player ptwo = plugin.getServer().getPlayer(plugin.match.get(name));
            // skips the first swap
            if (!plugin.startgame.contains(name)) {
                switchUtil(pone);
                switchUtil(ptwo);
                final Location locone = pone.getLocation();
                final Location loctwo = ptwo.getLocation();
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        pone.teleport(loctwo);
                        ptwo.teleport(locone);
                        if (plugin.getConfig().getBoolean("swap_sound")) {
                            plugin.utility.swapEffects(locone);
                            plugin.utility.swapEffects(loctwo);
                        }
                        protect();
                    }
                }.runTaskLater(plugin, 1L);
            } else {
                plugin.startgame.remove(name);
            }
        }

    }

    public void switchUtil(Player player) {
        plugin.utility.message(ChatColor.BOLD + "Commencing swap!", player);
        if (player.getVehicle() != null) {
            player.leaveVehicle();
        }
    }

    public void protect() {
        plugin.protect = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                plugin.protect = false;
            }
        }.runTaskLater(plugin, 20L * 5);
    }
}
