package com.kiro.trading;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TradePlayer {


	private final Player player;
	private Inventory inventory;
	private int expOffer;
	private int moneyOffer;
	private boolean accepted;
	
	public TradePlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getName() {
		return player.getName();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void setExpOffer(int expOffer) {
		this.expOffer = expOffer;
	}
	
	public void incrementExpOffer() {
		expOffer++;
	}
	
	public int getExpOffer() {
		return expOffer;
	}
	
	public void setMoneyOffer(int moneyOffer) {
		this.moneyOffer = moneyOffer;
	}
	
	public void addMoneyToOffer(int money) {
		this.moneyOffer += money;
	}
	
	public int getMoneyOffer() {
		return moneyOffer;
	}
	
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
	public boolean hasAccepted() {
		return accepted;
	}
}
