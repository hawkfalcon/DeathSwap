package com.hawkfalcon.deathswap;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (loc.length != 4) {
            float yaw = Float.parseFloat(loc[4]);
            float pitch = Float.parseFloat(loc[5]);
            Location location = new Location(w, x, y, z, yaw, pitch);
            return location;
        }
        Location location = new Location(w, x, y, z);
        return location;
    }

    public void randomTeleport(String n_one, String n_two) {
        Location lobby = getLocation(plugin.getConfig().getString("lobby_spawn"));
        Location loc_one = randomLoc(lobby);
        Location loc_two = randomLoc(lobby);
        loadLoc(loc_one);
        loadLoc(loc_two);
        loc_one.setY(loc_one.getY() + 2);
        loc_two.setY(loc_two.getY() + 2);
        if (plugin.getConfig().getBoolean("countdown")) {
            countdown(10, loc_one, loc_two, n_one, n_two);
        } else {
            tpPlayer(loc_one, n_one);
            tpPlayer(loc_two, n_two);
        }
    }

    public void loadLoc(Location loc) {
        loc.getBlock().getRelative(BlockFace.DOWN).setTypeId(7);
        loc.getChunk().load();
    }

    public void tpPlayer(Location loc, String n) {
        plugin.utility.message("Teleporting, be ready!", n);
        plugin.getServer().getPlayer(n).teleport(loc);
    }

    public int count;

    public void countdown(int time, final Location loc_one, final Location loc_two, final String n_one, final String n_two) {
        count = time;
        new BukkitRunnable() {

            @Override
            public void run() {
                if (count < 11 && count > 0) {
                    plugin.utility.message("Teleportation commencing in " + count + " seconds!", n_one);
                    plugin.utility.message("Teleportation commencing in " + count + " seconds!", n_two);
                }
                count--;
                if (count == 0) {
                    this.cancel();
                    tpPlayer(loc_one, n_one);
                    tpPlayer(loc_two, n_two);
                }
            }

        }.runTaskTimer(plugin, 0, 20L);
    }

    public Location randomLoc(Location center) {
        World world = plugin.getServer().getWorld(plugin.getConfig().getString("world"));
        if (world == null) {
            world = center.getWorld();
        }
        Random rand = new Random();
        int rad = plugin.getConfig().getInt("random_spawn_radius");
        int min = -(rad / 100);
        int max = rad / 100;
        double x = 0;
        double y = 0;
        double z = 0;
        Material below = null;
        Material above = null;
        while (true) {
            if (below == null || below == Material.LAVA || below == Material.WATER || below == Material.STATIONARY_WATER || below == Material.BEDROCK || above != Material.AIR) {
                x = (rand.nextInt(max - min + 1) + min) * 100;
                z = (rand.nextInt(max - min + 1) + min) * 100;
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
