package com.hawkfalcon.deathswap.utilities;

import com.hawkfalcon.deathswap.DeathSwap;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    public DeathSwap plugin;

    Map<String, PlayerInventory> inventories = new HashMap<String, PlayerInventory>();

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

    public void broadcastGame(String message) {
        for (String sender : plugin.game) {
            plugin.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + ChatColor.GREEN + message);
        }
    }

    /**
     * @param spawn Lobby = 0, Arena = 1
     */
    public void teleport(Player player, int spawn) {
        String cloc = null;
        if (spawn == 0) {
            cloc = plugin.getConfig().getString("lobby_spawn");
        } else {
            cloc = plugin.getConfig().getString("end_spawn");
        }
        runTp(player, cloc);
    }

    public void runTp(Player player, String cloc) {
        if (!cloc.equals("world,0,0,0,0,0")) {
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
            PlayerInventory i = inventories.get(name);
            inventories.remove(name);
            player.getInventory().setContents(i.getContents());
            player.getInventory().setArmorContents(i.getArmorContents());
        }
    }

    public void saveInventory(Player player) {
        inventories.put(player.getName(), player.getInventory());
    }

}
