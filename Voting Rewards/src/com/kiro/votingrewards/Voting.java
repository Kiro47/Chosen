package com.kiro.votingrewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;



public class Voting extends JavaPlugin implements Listener {
	
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	
	public void onVote(VotifierEvent e) {
		
		
		Vote v = e.getVote();
	
		Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + v.getUsername() + ChatColor.BLUE + " Has Voted on " + v.getServiceName() + "!");
		
		Player p = Bukkit.getServer().getPlayer(v.getUsername());
		if (p == null) {
			return;
		}
		String uname = v.getUsername().toLowerCase();
		
		if ((uname.length() > 20) || (uname.contains("porn")) || (uname.contains(".")) || (uname.contains("http")) || (uname.contains(" "))) {
			return;
		}
			
			
		p.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + v.getUsername() + " 100");
		p.setLevel(p.getLevel() + 4);
		
		}

	}