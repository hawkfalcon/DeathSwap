package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Signs implements Listener {

    DeathSwap plugin;

    public Signs(DeathSwap ds) {
        plugin = ds;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if(sign.getLine(0).equalsIgnoreCase("[DeathSwap]") && sign.getLine(1).equalsIgnoreCase("join")) {
                    Player player = event.getPlayer();
                    if(player.hasPermission("deathswap.join")) {
                        String name = player.getName();
                        if(!plugin.game.contains(name) && !plugin.lobby.contains(name)) {
                            plugin.utility.message("You joined the game!", name);
                            plugin.utility.broadcastLobby(name + " joined the game!");
                            // mark as in lobby
                            plugin.lobby.add(name);
                            // teleport to lobby
                            plugin.utility.teleport(name, 0);
                            plugin.utility.checkForStart();
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
                    }
                }
            }
        }
    }
}
