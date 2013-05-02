package com.hawkfalcon.DeathSwap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    public Commands command = new Commands(this);
    public Utility u = new Utility(this);
    public Loc loc = new Loc(this);
    public Start start = new Start(this);
    public Stop stop = new Stop(this);
    public Swap sw = new Swap(this);

    public CommandExecutor Commands = new Commands(this);
    public Listener Lobby = new Protect(this);
    public Listener Death = new Death(this);
    public Listener Auto = new Auto(this);

    HashMap<String, String> match = new HashMap<String, String>();
    HashMap<String, String> accept = new HashMap<String, String>();
    ArrayList<String> game = new ArrayList<String>();
    ArrayList<String> lobby = new ArrayList<String>();
    ArrayList<String> loggedoff = new ArrayList<String>();
    ArrayList<String> startgame = new ArrayList<String>();


    public boolean protect = false;
    public int min;
    public int max;

    public void onEnable() {
        final File f = new File(getDataFolder(), "config.yml");
        if (!f.exists()) {
            saveDefaultConfig();
        }
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            System.out.println("Error Submitting stats!");
        }
        getServer().getPluginManager().registerEvents(Lobby, this);
        getServer().getPluginManager().registerEvents(Death, this);
        getServer().getPluginManager().registerEvents(Auto, this);
        getCommand("ds").setExecutor(Commands);
        getCommand("deathswap").setExecutor(Commands);
        startTimer();
        min = getConfig().getInt("min_time");
        max = getConfig().getInt("max_time");
    }

    public int randNum;

    public void startTimer() {
        int task = getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                Random rand = new Random();
                randNum = rand.nextInt(max - min + 1) + min;
                sw.switchPlayers();
                startTimer();
            }
        }, randNum * 5L);
    }

}
