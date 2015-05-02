package com.kiro.trading.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class MessageConfiguration {

private static final String PLAYERNAME_PLACEHOLDER = "@p";
	
	private Map<String, String> messages;
	
	public MessageConfiguration(Configuration config) {
		messages = new LinkedHashMap<String, String>();
		
		loadByConfiguration(config);
	}
	
	public void loadByConfiguration(Configuration config) {
		Set<String> messageKeys = config.getKeys(false);
		for (String key : messageKeys) {
			String message = config.getString(key);
			
			messages.put(key, message);
		}
	}
	
	public String getMessage(String key, String nameReplacement) {
		String message = messages.get(key);
		if (message != null) {
			message = message.replace(PLAYERNAME_PLACEHOLDER, nameReplacement);
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		return message;
	}

}
