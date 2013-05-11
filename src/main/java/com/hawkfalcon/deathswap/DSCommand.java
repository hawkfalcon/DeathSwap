package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DSCommand implements CommandExecutor {

    public DeathSwap plugin;

    public DSCommand(DeathSwap m) {
        this.plugin = m;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(!(sender instanceof Player))
            return false;
        String name = sender.getName();
        Player player = (Player) sender;
        if((cmd.getName().equalsIgnoreCase("ds")) || (cmd.getName().equalsIgnoreCase("deathswap"))) {
            if(args.length == 0) {
                plugin.utility.message("Join with /ds join", name);
            }
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("join") && player.hasPermission("deathswap.join")) {
                if(!plugin.game.contains(name) && !plugin.lobby.contains(name)) {
                    plugin.utility.message("You joined the game!", name);
                    plugin.utility.broadcastLobby(name + " joined the game!");
                    // mark as in lobby
                    plugin.lobby.add(name);
                    // teleport to lobby
                    plugin.utility.teleport(name, 0);
                    plugin.utility.checkForStart();
                }
            }
            if(args[0].equalsIgnoreCase("leave") && player.hasPermission("deathswap.leave")) {
                if(plugin.lobby.contains(name)) {
                    plugin.utility.message("You left the game!", name);
                    plugin.utility.broadcastLobby(name + " left the game!");
                    plugin.lobby.remove(name);
                    plugin.utility.teleport(name, 1);
                }
                if(plugin.game.contains(name)) {
                    plugin.game.remove(name);
                    plugin.stop.dealWithLeftoverGames(name, false);
                    player.getInventory().clear();
                    plugin.utility.clearArmor(player);
                    plugin.utility.teleport(name, 1);
                }
            }
            if(args[0].equalsIgnoreCase("accept") && player.hasPermission("deathswap.accept")) {
                if(plugin.accept.containsKey(name)) {
                    plugin.start.newGame(plugin.accept.get(name), name);
                    plugin.accept.remove(name);
                }
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("set") && player.hasPermission("deathswap.set")) {
                Location loc = player.getLocation();
                String locs = player.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
                if(args[1].equals("lobby")) {
                    plugin.getConfig().set("lobby_spawn", locs);
                } else if(args[1].equals("end")) {
                    plugin.getConfig().set("end_spawn", locs);
                } else {
                    return false;
                }
                plugin.saveConfig();
                plugin.utility.message(ChatColor.GOLD + "Spawn point set!", name);
            }
            if(args[0].equalsIgnoreCase("duel") && player.hasPermission("deathswap.duel")) {
                Player pl = plugin.getServer().getPlayer(args[1]);
                String o = args[1];
                if(pl == null || pl.getName() == name || plugin.game.contains(pl.getName())) {
                    sender.sendMessage("Invalid Player");
                } else {
                    startAccept(name, o);
                }
            }
        }
        return false;
    }

    public void startAccept(final String n, String o) {
        plugin.utility.message(n + " wishes to play DeathSwap with you, type /ds accept to play!", o);
        plugin.utility.message("Request to play sent!", n);
        plugin.accept.put(o, n);
        new BukkitRunnable() {

            @Override
            public void run() {
                plugin.accept.remove(n);
            }

        }.runTaskLater(plugin, 200L);
    }
}
