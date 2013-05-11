package com.hawkfalcon.deathswap;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class Loc {

    public DeathSwap plugin;

    public Loc(DeathSwap ds) {
        this.plugin = ds;
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

    public void randomTeleport(String n) {
        Location lobby = getLocation(plugin.getConfig().getString("lobby_spawn"));
        Location loc = randomLoc(lobby);
        plugin.utility.message("Teleporting, be ready!", n);
        loc.getBlock().getRelative(BlockFace.DOWN).setTypeId(7);
        plugin.getServer().getPlayer(n).teleport(loc);
    }

    public Location randomLoc(Location center) {
        World world = plugin.getServer().getWorld(plugin.getConfig().getString("world"));
        if(world == null) {
            world = center.getWorld();
        }
        Random rand = new Random();
        int min = 1;
        int max = (plugin.getConfig().getInt("random_spawn_radius")) / 100;
        double x = 0;
        double y = 0;
        double z = 0;
        Material below = null;
        Material above = null;
        while (true) {
            if(below == null || below == Material.LAVA || below == Material.WATER || below == Material.STATIONARY_WATER || below == Material.BEDROCK || above != Material.AIR) {
                x = (rand.nextInt(max - min) + min) * 100;
                z = (rand.nextInt(max - min) + min) * 100;
                y = world.getHighestBlockAt((int) x, (int) z).getY();
                below = world.getBlockAt((int) x, (int) y - 1, (int) z).getType();
                above = world.getBlockAt((int) x, (int) y + 1, (int) z).getType();
            } else {
                break;
            }
        }
        return new Location(world, x, y, z);
    }
}
