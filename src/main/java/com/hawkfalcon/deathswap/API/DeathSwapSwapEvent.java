package com.hawkfalcon.deathswap.API;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeathSwapSwapEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Set<Player> players = new HashSet<Player>();

    public DeathSwapSwapEvent(Player one, Player two) {
        players.add(one);
        players.add(two);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
