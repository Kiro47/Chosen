package com.kiro.trading;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.kiro.trading.Trade.StopCause;
import com.kiro.trading.config.TradeConfiguration;

public class MoveCheckerRunnable implements Runnable {
	
	private final TradeConfiguration config;
	private final TradeFactory factory;
	private final Map<Player, Location> lastLocationMap;
	
	public MoveCheckerRunnable(TradeFactory factory, TradeConfiguration config) {
		this.factory = factory;
		this.lastLocationMap = Maps.newHashMap();
		this.config = config;
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Trade trade = factory.getTrade(player);
			
			if (trade == null || trade.getState() != TradeState.TRADING) {
				if (lastLocationMap.containsKey(player)) {
					lastLocationMap.remove(player);
				}
				
				continue;
			}
			
			if (!lastLocationMap.containsKey(player)) {
				lastLocationMap.put(player, player.getLocation());
			} else {
				Location last = lastLocationMap.get(player);
				Location now = player.getLocation();
				
				if (last.getWorld() != now.getWorld()) {
					factory.stopTrade(trade, StopCause.LEFT_WORLD, player);
				} else {
					double distanceSquared = trade.getInitiator().getPlayer().getLocation()
							.distanceSquared(trade.getPartner().getPlayer().getLocation());
					
					final int maxDistance = config.getMaximumTradeDistance();
					if (distanceSquared > Math.pow(maxDistance, 2)) {
						factory.stopTrade(trade, StopCause.MOVE, player);
					}
				}
			}
		}
	}

}
