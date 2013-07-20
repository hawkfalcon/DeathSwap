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
        if (!(sender instanceof Player))
            return false;
        String name = sender.getName();
        Player player = (Player) sender;
        if ((cmd.getName().equalsIgnoreCase("ds")) || (cmd.getName().equalsIgnoreCase("deathswap"))) {
            if (args.length == 0) {
                plugin.utility.message("Join with /ds join", player);
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("join") && player.hasPermission("deathswap.join")) {
                    plugin.join.join(player);
            }
            if (args[0].equalsIgnoreCase("leave") && player.hasPermission("deathswap.leave")) {
                plugin.leave.leave(player);
            }
            if (args[0].equalsIgnoreCase("accept") && player.hasPermission("deathswap.accept")) {
                if (plugin.accept.containsKey(name)) {
                    plugin.newGame.newGame(plugin.getServer().getPlayerExact(plugin.accept.get(name)), player);
                    plugin.accept.remove(name);
                }
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") && player.hasPermission("deathswap.set")) {
                Location loc = player.getLocation();
                String locs = player.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
                if (args[1].equals("lobby")) {
                    plugin.getConfig().set("lobby_spawn", locs);
                } else if (args[1].equals("end")) {
                    plugin.getConfig().set("end_spawn", locs);
                } else {
                    return false;
                }
                plugin.saveConfig();
                plugin.utility.message(ChatColor.GOLD + "Spawn point set!", player);
            }
            if (args[0].equalsIgnoreCase("duel") && player.hasPermission("deathswap.duel")) {
                Player accepter = plugin.getServer().getPlayer(args[1]);
                if (accepter == null || accepter.getName().equals(name) || plugin.game.contains(accepter.getName())) {
                    sender.sendMessage("Invalid Player");
                } else {
                    startAccept(player, accepter);
                }
            }
        }
        return false;
    }

    public void startAccept(final Player requester, Player accepter) {
        plugin.utility.message(requester.getName() + " wishes to play DeathSwap with you, type /ds accept to play!", accepter);
        plugin.utility.message("Request to play sent!", requester);
        plugin.accept.put(accepter.getName(), requester.getName());
        new BukkitRunnable() {

            @Override
            public void run() {
                plugin.accept.remove(requester.getName());
            }

        }.runTaskLater(plugin, 200L);
    }
}
