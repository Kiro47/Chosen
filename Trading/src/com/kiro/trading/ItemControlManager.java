package com.kiro.trading;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kiro.trading.config.TradeConfiguration;
import com.kiro.trading.config.TradeConfiguration.ItemStackData;

public class ItemControlManager {

	private ItemControlMode mode;
	private List<ItemStackData> items;
	private List<String> lores;
	
	public ItemControlManager(ItemControlMode mode, List<ItemStackData> items, List<String> lores) {
		this.mode = mode;
		this.items = items;
		this.lores = lores;
	}
	
	public ItemControlManager(TradeConfiguration config) {
		updateValues(config);
	}
	
	public void updateValues(TradeConfiguration config) {
		this.mode = config.getItemControlMode();
		this.items = config.getItemControlList();
		this.lores = config.getItemControlLoreList();
	}

	public boolean isTradeable(ItemStack stack) {
		boolean allowedBlacklist = isAllowedBlacklist(stack);
		
		return mode == ItemControlMode.BLACKLIST ? allowedBlacklist : !allowedBlacklist;
	}
	
	@SuppressWarnings("deprecation")
	private boolean isAllowedBlacklist(ItemStack stack) {
		for (ItemStackData data : items) {
			if (data.getMaterial() == stack.getType() && data.getData() == stack.getData().getData()) {
				return false;
			}
		}
		
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = meta.getLore();
		
		if (lore != null) {
			for (String loreStr : lores) {	
				loreStr = ChatColor.stripColor(loreStr);
				
				for (String itemLoreStr : lore) {
					itemLoreStr = ChatColor.stripColor(itemLoreStr);
					
					if (loreStr.equals(itemLoreStr)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}

	public enum ItemControlMode {
		
		BLACKLIST,
		WHITELIST;
		
		public static ItemControlMode getMode(String str, ItemControlMode def) {
			str = str.toUpperCase();
			
			for (ItemControlMode mode : values()) {
				if (mode.name().equals(str)) {
					return mode;
				}
			}
			
			if (def != null) {
				return def;
			} else throw new IllegalArgumentException("No enum constant \"" + str + "\" defined");
		}
		
	}
}
