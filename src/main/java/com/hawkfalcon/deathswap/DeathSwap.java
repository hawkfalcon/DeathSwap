package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class DeathSwap extends JavaPlugin {

    public Utility utility = new Utility(this);
    public Loc loc = new Loc(this);
    public Start start = new Start(this);
    public Stop stop = new Stop(this);
    public Swap swap = new Swap(this);

    HashMap<String, String> match = new HashMap<String, String>();
    HashMap<String, String> accept = new HashMap<String, String>();
    HashMap<String, String> inventory = new HashMap<String, String>();
    public ArrayList<String> game = new ArrayList<String>();
    public ArrayList<String> lobby = new ArrayList<String>();
    ArrayList<String> loggedoff = new ArrayList<String>();
    ArrayList<String> startgame = new ArrayList<String>();

    Random rand = new Random();

    public boolean protect = false;
    public int min;
    public int max;

    public void onEnable() {
        saveDefaultConfig();
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Error Submitting stats!");
        }
        getServer().getPluginManager().registerEvents(new DSListener(this), this);
        getCommand("ds").setExecutor(new DSCommand(this));
        startTimer();
        min = getConfig().getInt("min_time");
        max = getConfig().getInt("max_time");
    }

    public void onDisable() {
        if (!game.isEmpty()) {
            for (String name : game) {
                utility.teleport(getServer().getPlayerExact(name), 1);
            }
        }
    }

    public int randNum;

    public void startTimer() {
        new BukkitRunnable() {

            @Override
            public void run() {
                randNum = rand.nextInt(max - min + 1) + min;
                swap.switchPlayers();
                startTimer();
            }

        }.runTaskLater(this, randNum * 20L);
    }

    public void join(Player player) {
        if (player.hasPermission("deathswap.join")) {
            String name = player.getName();
            if (!game.contains(name) && !lobby.contains(name)) {
                utility.message("You joined the game!", player);
                utility.broadcastLobby(name + " joined the game!");
                // mark as in lobby
                lobby.add(name);
                // teleport to lobby
                utility.teleport(player, 0);
                utility.checkForStart();
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
        }
    }
}
