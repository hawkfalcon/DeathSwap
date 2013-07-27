package com.hawkfalcon.deathswap;

import com.hawkfalcon.deathswap.game.*;
import com.hawkfalcon.deathswap.utilities.Loc;
import com.hawkfalcon.deathswap.utilities.MetricsLite;
import com.hawkfalcon.deathswap.utilities.Utility;
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
            for (String name : game) {
                Player player = getServer().getPlayerExact(name);
                utility.teleport(player, 1);
                utility.restorePlayer(player);
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
