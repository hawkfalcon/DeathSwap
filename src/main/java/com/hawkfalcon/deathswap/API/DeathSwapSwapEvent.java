package com.hawkfalcon.deathswap.API;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapSwapEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public DeathSwapSwapEvent() {}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
