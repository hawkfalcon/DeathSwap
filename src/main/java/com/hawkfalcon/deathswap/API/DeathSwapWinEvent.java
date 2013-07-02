package com.hawkfalcon.deathswap.API;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String winner;
    private String loser;

    public DeathSwapWinEvent(String winner, String loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getWinner() {
        return this.winner;
    }

    public String getLoser() {
        return this.loser;
    }
}
