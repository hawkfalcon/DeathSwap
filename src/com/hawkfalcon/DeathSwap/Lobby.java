package com.hawkfalcon.DeathSwap;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class Lobby implements Listener {
	public Main p;
	public Lobby(Main m) {
		this.p = m;
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void BlockBreak(BlockBreakEvent event) {
		if (p.lobby.contains(event.getPlayer().getName())){
			if(!event.getPlayer().hasPermission("deathswap.bypass")){
			event.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlace(BlockPlaceEvent event){
		if (p.lobby.contains(event.getPlayer().getName())){
			if(!event.getPlayer().hasPermission("deathswap.bypass")){
				event.setCancelled(true);
				}
		}
	}	
	@EventHandler
	public void pickup(PlayerPickupItemEvent event){
		if (p.lobby.contains(event.getPlayer().getName())){
			if(!event.getPlayer().hasPermission("deathswap.bypass")){
			event.setCancelled(true);
		}
		}
	}
}
