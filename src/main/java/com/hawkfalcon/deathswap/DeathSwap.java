package com.hawkfalcon.deathswap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathSwap extends JavaPlugin {

    public Utility utility = new Utility(this);
    public Loc loc = new Loc(this);
    public Start start = new Start(this);
    public Stop stop = new Stop(this);
    public Swap swap = new Swap(this);

    HashMap<String, String> match = new HashMap<String, String>();
    HashMap<String, String> accept = new HashMap<String, String>();
    ArrayList<String> game = new ArrayList<String>();
    ArrayList<String> lobby = new ArrayList<String>();
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
        } catch(IOException e) {
            getLogger().warning("Error Submitting stats!");
        }
        getServer().getPluginManager().registerEvents(new Protect(this), this);
        getServer().getPluginManager().registerEvents(new Death(this), this);
        getServer().getPluginManager().registerEvents(new Auto(this), this);
        getCommand("ds").setExecutor(new DSCommand(this));
        startTimer();
        min = getConfig().getInt("min_time");
        max = getConfig().getInt("max_time");
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

        }.runTaskLater(this, randNum * 5L);
    }

}
