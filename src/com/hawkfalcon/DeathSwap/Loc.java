package com.hawkfalcon.DeathSwap;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class Loc {
	public Main p;
	public Loc(Main m) {
		this.p = m;
	}
	public Location getLocation(String cloc) {
		String[] loc = cloc.split("\\,");
		World w = Bukkit.getWorld(loc[0]);
		Double x = Double.parseDouble(loc[1]);
		Double y = Double.parseDouble(loc[2]);
		Double z = Double.parseDouble(loc[3]);
		Location location = new Location(w, x, y, z);
		return location;
	}
	public void randomTeleport(final String playerone, final String playertwo) {
		Location lobby = getLocation(p.getConfig().getString("lobby_spawn"));
		Location locone = randomLoc(lobby);
		Location loctwo = randomLoc(lobby);
		p.u.message("Teleporting, be ready!", playerone);
		p.u.message("Teleporting, be ready!", playertwo);
		locone.getBlock().getRelative(BlockFace.DOWN).setTypeId(7);
		loctwo.getBlock().getRelative(BlockFace.DOWN).setTypeId(7);
		p.getServer().getPlayer(playerone).teleport(locone);
		p.getServer().getPlayer(playertwo).teleport(loctwo);
	}
	public Location randomLoc(Location center){
		World w = center.getWorld();
		Random rand = new Random();
		int min = 100;
		int max = p.getConfig().getInt("random_spawn_radius");
		double x = 0;
		double y = 0;
		double z = 0;
		Material below = null; 	
		Material above = null; 	
		while(true) {
			if(below == null || below == Material.LAVA || below == Material.WATER || below == Material.STATIONARY_WATER || above != Material.AIR) {
				x = rand.nextInt(max-min) + min;
				z = rand.nextInt(max-min) + min;
				y = w.getHighestBlockAt((int)x,(int)z).getY();	
				below = w.getBlockAt((int)x, (int)y-1, (int)z).getType();
				above = w.getBlockAt((int)x, (int)y+1, (int)z).getType();

			} else
				break;		   
		}

		Location loc = new Location(w, x, y, z);
		return loc;		
	}
}