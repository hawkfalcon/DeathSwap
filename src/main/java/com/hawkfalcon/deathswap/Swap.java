package com.hawkfalcon.deathswap;

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
        for (String n : plugin.match.keySet()) {
            final String n_one = n;
            final String n_two = plugin.match.get(n);
            // skips the first swap
            if (!plugin.startgame.contains(n_one)) {
                final Player pone = plugin.getServer().getPlayer(n_one);
                final Player ptwo = plugin.getServer().getPlayer(n_two);
                switchUtil(n_one, pone);
                switchUtil(n_two, ptwo);
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
                plugin.startgame.remove(n_one);
            }
        }

    }

    public void switchUtil(String n, Player player) {
        plugin.utility.message(ChatColor.BOLD + "Commencing swap!", n);
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
