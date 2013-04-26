package com.hawkfalcon.DeathSwap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public Main p;
	public Commands(Main m) {
		this.p = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player))return false;
		String n = sender.getName();
		Player player = (Player)sender;
		if ((cmd.getName().equalsIgnoreCase("ds")) || (cmd.getName().equalsIgnoreCase("deathswap"))) {
			if (args.length == 0) {
				p.u.message("Join with /ds join", n);
			}
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("join") && player.hasPermission("deathswap.join")){
				if (!p.game.contains(n) && !p.lobby.contains(n)) {
					p.u.message("You joined the game!", n);
					p.u.broadcastLobby(n + " joined the game!");
					//mark as in lobby
					p.lobby.add(n);
					//teleport to lobby
					p.u.teleport(n, 0);
					checkForStart();
				}				
			}
			if (args[0].equalsIgnoreCase("leave") && player.hasPermission("deathswap.leave")){
				if (p.lobby.contains(n)) {
					p.u.message("You left the game!", n);
					p.u.broadcastLobby(n + " left the game!");
					p.lobby.remove(n);
					p.u.teleport(n, 1);
				}
				if (p.game.contains(n)){
					p.game.remove(n);
					p.stop.dealWithLeftoverGames(n, false);
					p.getServer().getPlayer(n).getInventory().clear();
					p.u.clearArmor(p.getServer().getPlayer(n));
					p.u.teleport(n, 1);
				}						
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set") && player.hasPermission("deathswap.set")){
				Location loc = p.getServer().getPlayer(n).getLocation();
				String locs = p.getServer().getPlayer(n).getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
				if (args[1].equals("lobby")){
					p.getConfig().set("lobby_spawn", locs);
				}
				else if (args[1].equals("end")){
					p.getConfig().set("end_spawn", locs);
				} else {
					return false;
				}
				p.saveConfig();
				p.u.message(ChatColor.GOLD + "Spawn point set!", n);
			}
		}
		return false;
	}
	public void checkForStart() {
		int t = p.lobby.size();
		if (t > 1){
			p.start.newGame();
		}
	}

}
