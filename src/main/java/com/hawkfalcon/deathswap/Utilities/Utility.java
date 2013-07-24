package com.hawkfalcon.deathswap.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.hawkfalcon.deathswap.DeathSwap;

public class Utility {

    public DeathSwap plugin;

    public Utility(DeathSwap ds) {
        this.plugin = ds;
    }

    public void message(String message, CommandSender player) {
        player.sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }

    public void broadcast(String message) {
        plugin.getServer().broadcastMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }

    public void broadcastLobby(String message) {
        for (String sender : plugin.lobby) {
            plugin.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + ChatColor.GREEN + message);
        }
    }

    // 0=lobby 1=arena
    public void teleport(Player player, int spawn) {
        if (spawn == 0) {
            String cloc = plugin.getConfig().getString("lobby_spawn");
            runTp(player, cloc);
        }
        if (spawn == 1) {
            String cloc = plugin.getConfig().getString("end_spawn");
            runTp(player, cloc);
        }
    }

    public void runTp(Player player, String cloc) {
        if (!(cloc.equals("world,0,0,0,0,0"))) {
            player.teleport(plugin.loc.getLocation(cloc));
        } else {
            plugin.utility.message(ChatColor.RED + "You must set spawn points with /ds set <lobby/end> first!", player);
            plugin.utility.broadcastLobby(player.getName() + " left the game!");
            plugin.lobby.remove(player.getName());
        }
    }

    public void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void swapEffects(Location loc) {
        World world = loc.getWorld();
        world.playEffect(loc, Effect.GHAST_SHRIEK, 0);
        for (int x = 5; x > 1; x--) {
            world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
        }
    }

    public void playerReset(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        clearArmor(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFireTicks(0);
        player.getActivePotionEffects().clear();
        player.setExp(0);
    }

    public void checkForStart() {
        int size = plugin.lobby.size();
        if (size > 1) {
            Player playerone = plugin.getServer().getPlayerExact(plugin.lobby.get(0));
            Player playertwo = plugin.getServer().getPlayerExact(plugin.lobby.get(1));
            plugin.newGame.newGame(playerone, playertwo);
        }
    }

    public void restorePlayer(Player player) {
        String name = player.getName();
        plugin.utility.playerReset(player);
        plugin.match.remove(name);
        plugin.game.remove(name);
        plugin.startgame.remove(name);
        if (plugin.getConfig().getBoolean("save_inventory")) {
            Inventory i = plugin.inventory.stringToInventory(plugin.inventorystorage.get(name));
            player.getInventory().setContents(i.getContents());
        }
    }


}
