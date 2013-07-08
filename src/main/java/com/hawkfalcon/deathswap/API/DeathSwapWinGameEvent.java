package com.hawkfalcon.deathswap.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapWinGameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player winner;
    private Player loser;
    
    public DeathSwapWinGameEvent(Player winner, Player loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getWinner() {
        return this.winner;
    }

    public Player getLoser() {
        return this.loser;
    }
}
