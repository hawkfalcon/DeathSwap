package com.hawkfalcon.deathswap.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapNewGameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player playerone;
    private Player playertwo;

    public DeathSwapNewGameEvent(Player playerone, Player playertwo) {
        this.playerone = playerone;
        this.playertwo = playertwo;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayerOne() {
        return this.playerone;
    }

    public Player getPlayerTwo() {
        return this.playertwo;
    }
}
