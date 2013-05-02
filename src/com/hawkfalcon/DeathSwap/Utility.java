package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
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
        for (String sender : p.lobby) {
            p.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + ChatColor.GREEN + message);
        }
    }

    // 0=lobby 1=arena
    public void teleport(String n, int spawn) {
        if (spawn == 0) {
            String cloc = p.getConfig().getString("lobby_spawn");
            runTp(n, cloc);
        }
        if (spawn == 1) {
            String cloc = p.getConfig().getString("end_spawn");
            runTp(n, cloc);
        }
    }

    public void runTp(String n, String cloc) {
        if (!(cloc.equals("world,0,0,0"))) {
            p.getServer().getPlayer(n).teleport(p.loc.getLocation(cloc));
        } else {
            p.u.message(ChatColor.RED + "You must set spawn points with /ds set <lobby/end> first!", n);
            p.u.broadcastLobby(n + " left the game!");
            p.lobby.remove(n);
        }
    }

    public void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void swapEffects(Location loc) {
        World w = loc.getWorld();
        w.playEffect(loc, Effect.GHAST_SHRIEK, 0);
        for (int x = 5; x > 1; x--) {
            w.playEffect(loc, Effect.ENDER_SIGNAL, 0);
        }
    }

    public void playerReset(Player pl) {
        pl.setHealth(pl.getMaxHealth());
        pl.setFoodLevel(20);
        clearArmor(pl);
        pl.getInventory().clear();
        pl.setGameMode(GameMode.SURVIVAL);
    }

    public void checkForStart() {
        int t = p.lobby.size();
        if (t > 1) {
            String playerone = p.lobby.get(0);
            String playertwo = p.lobby.get(1);
            p.start.newGame(playerone, playertwo);
        }
    }
}
