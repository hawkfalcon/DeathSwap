package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utility {

    public DeathSwap p;

    public Utility(DeathSwap m) {
        this.p = m;
    }

    public void message(String message, String sender) {
        p.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }
    public void broadcast(String message) {
        p.getServer().broadcastMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }
    public void broadcastLobby(String message) {
        for(String sender:p.lobby) {
            p.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + ChatColor.GREEN + message);
        }
    }

    // 0=lobby 1=arena
    public void teleport(String n, int spawn) {
        if(spawn == 0) {
            String cloc = p.getConfig().getString("lobby_spawn");
            p.getServer().getPlayer(n).teleport(p.loc.getLocation(cloc));
        }
        if(spawn == 1) {
            String cloc = p.getConfig().getString("end_spawn");
            p.getServer().getPlayer(n).teleport(p.loc.getLocation(cloc));
        }
    }

    public void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }
}