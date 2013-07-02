package com.hawkfalcon.deathswap.API;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapNewGameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String name1;
    private String name2;

    public DeathSwapNewGameEvent(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getNameOne() {
        return this.name1;
    }

    public String getNameTwo() {
        return this.name2;
    }
}
