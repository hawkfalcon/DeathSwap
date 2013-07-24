package com.hawkfalcon.deathswap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.hawkfalcon.deathswap.Game.Join;
import com.hawkfalcon.deathswap.Game.Leave;
import com.hawkfalcon.deathswap.Game.NewGame;
import com.hawkfalcon.deathswap.Game.Swap;
import com.hawkfalcon.deathswap.Game.WinGame;
import com.hawkfalcon.deathswap.Utilities.Loc;
import com.hawkfalcon.deathswap.Utilities.MetricsLite;
import com.hawkfalcon.deathswap.Utilities.Utility;

public class DeathSwap extends JavaPlugin {

    public Utility utility = new Utility(this);
    public Loc loc = new Loc(this);
    public NewGame newGame = new NewGame(this);
    public WinGame winGame = new WinGame(this);
    public Join join = new Join(this);
    public Leave leave = new Leave(this);
    public Swap swap = new Swap(this);

    public HashMap<String, String> match = new HashMap<String, String>();
    public HashMap<String, String> accept = new HashMap<String, String>();
    public ArrayList<String> game = new ArrayList<String>();
    public ArrayList<String> lobby = new ArrayList<String>();
    public ArrayList<String> startgame = new ArrayList<String>();

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
            for (String name:game) {
                utility.teleport(getServer().getPlayerExact(name), 1);
            }
        }
    }

    public int randNum;

    /**
     * Starts the swap timer
     */
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
}
