package me.neznamy.tab.platforms.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.neznamy.tab.api.TabPlayer;

/**
 * Bukkit event that is called when player is successfully loaded after joining. This also includes plugin reloading.
 */
public class TabPlayerLoadEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private TabPlayer player;
	
	public TabPlayerLoadEvent(TabPlayer player) {
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers(){
		return getHandlerList();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public TabPlayer getPlayer() {
		return player;
	}
}