package com.hawkfalcon.DeathSwap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Switch {
	public Main p;
	public Switch(Main m) {
		this.p = m;
	}
	public void switchPlayers() {
		for (String n : p.match.keySet()){
			String pone = n;
			String ptwo = p.match.get(n);	
			p.u.message("Commencing swap!", pone);	
			p.u.message("Commencing swap!", ptwo);
			Player playerone = p.getServer().getPlayer(pone);
			Player playertwo = p.getServer().getPlayer(ptwo);
			Location locone = playerone.getLocation();
			Location loctwo = playertwo.getLocation();
			protect();
			playerone.teleport(loctwo);
			playertwo.teleport(locone);
		}
	}
	public void protect() {
		p.protect = true;
		int task = p.getServer().getScheduler().scheduleSyncDelayedTask(this.p, new Runnable()  {
			public void run() {	
				p.protect = false;
			}			
		},20L*5);
	}
}