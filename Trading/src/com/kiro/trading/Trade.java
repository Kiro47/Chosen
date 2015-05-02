package com.kiro.trading;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Trade {

	public TradePlayer getInitiator();
	
	public TradePlayer getPartner();
	
	public TradeState getState();
	
	public void setState(TradeState state);
	
	public void accept();
	
	public void stop(StopCause cause, TradePlayer who);
	
	public void onInventoryClick(InventoryClickEvent event);
	
	public enum StopCause {
		
		INVENTORY_CLOSE,
		QUIT,
		DEATH,
		MOVE,
		LEFT_WORLD, 
		TIMEOUT,
		SERVER_SHUTDOWN;
		
	}

	
	
}
