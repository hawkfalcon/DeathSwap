package com.hawkfalcon.deathswap;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Utility {

    public DeathSwap plugin;

    public Utility(DeathSwap ds) {
        this.plugin = ds;
    }

    public void message(String message, Player player) {
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
        player.setGameMode(GameMode.SURVIVAL);
        clearArmor(player);
        player.getInventory().clear();
    }

    public void checkForStart() {
        int size = plugin.lobby.size();
        if (size > 1) {
            Player playerone = plugin.getServer().getPlayerExact(plugin.lobby.get(0));
            Player playertwo = plugin.getServer().getPlayerExact(plugin.lobby.get(1));
            plugin.start.newGame(playerone, playertwo);
        }
    }

    public static String inventoryToString(Inventory invInventory) {
        String serialization = invInventory.getSize() + ";";
        for (int i = 0; i < invInventory.getSize(); i++) {
            ItemStack is = invInventory.getItem(i);
            if (is != null) {
                String serializedItemStack = new String();

                String isType = String.valueOf(is.getType().getId());
                serializedItemStack += "t@" + isType;

                if (is.getDurability() != 0) {
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }

                if (is.getAmount() != 1) {
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }

                Map<Enchantment, Integer> isEnch = is.getEnchantments();
                if (isEnch.size() > 0) {
                    for (Map.Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                        serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                    }
                }

                serialization += i + "#" + serializedItemStack + ";";
            }
        }
        return serialization;
    }

    public static Inventory stringToInventory(String invString) {
        String[] serializedBlocks = invString.split(";");
        String invInfo = serializedBlocks[0];
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

        for (int i = 1; i < serializedBlocks.length; i++) {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);

            if (stackPosition >= deserializedInventory.getSize()) {
                continue;
            }

            ItemStack is = null;
            Boolean createdItemStack = false;

            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack) {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t")) {
                    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
                    createdItemStack = true;
                } else if (itemAttribute[0].equals("d") && createdItemStack) {
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("a") && createdItemStack) {
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("e") && createdItemStack) {
                    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                }
            }
            deserializedInventory.setItem(stackPosition, is);
        }

        return deserializedInventory;
    }
}
