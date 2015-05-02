package com.kiro.trading;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;
import com.kiro.trading.config.MessageConfiguration;
import com.kiro.trading.config.Messages;



public class CommandTrade implements CommandExecutor{

private SimpleTrading main;
	
	public CommandTrade(SimpleTrading main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("tra")) {
			// This command doesn't belong to us
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("The console cannot initiate a trade");
			return true;
		}
		
		Player player = (Player) sender;
		TradeFactory factory = main.getFactory();
		MessageConfiguration messageConfig = main.getMessageConfiguration();
		
		if (args.length < 1) {
			player.sendMessage(ChatColor.RED + "Usage: /tra <player>");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("accept")) {
			if (!player.hasPermission(Permissions.TRADE.getPermission())) {
				player.sendMessage(ChatColor.RED + "You don't have permission!");
				return true;
			}
			
			Trade trade = factory.getTrade(player);
			if (trade == null || trade.getPartner().getPlayer() != player) {
				player.sendMessage(messageConfig.getMessage(Messages.NO_PENDING_REQUESTS, player.getName()));
				return true;
			}
			
			Player other = trade.getInitiator().getPlayer() == player ? trade.getPartner().getPlayer() : trade.getInitiator().getPlayer();
			
			int maxDistance = main.getConfiguration().getMaximumTradeDistance();
			if (player.getWorld() != other.getWorld() || player.getLocation().distanceSquared(other.getLocation()) > maxDistance * 2) {
				player.sendMessage(ChatColor.RED + "Your partner is too far away!");
				return true;
			}
			
			factory.acceptTrade(player);
		} else if (args[0].equalsIgnoreCase("decline")) {
			Trade trade = factory.getTrade(player);
			if (trade == null || trade.getPartner().getPlayer() != player) {
				player.sendMessage(messageConfig.getMessage(Messages.NO_PENDING_REQUESTS, player.getName()));
				return true;
			}
			
			factory.declineTrade(player);
			trade.getInitiator().getPlayer().sendMessage(messageConfig.getMessage(Messages.DECLINE_REQUEST_MESSAGE, player.getName()));
			player.sendMessage(messageConfig.getMessage(Messages.DECLINE_MESSAGE, trade.getInitiator().getName()));
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!player.hasPermission(Permissions.RELOAD.getPermission())) {
				player.sendMessage(ChatColor.RED + "You don't have permission!");
				return true;
			}
			
			main.reload();
			player.sendMessage(ChatColor.GRAY + "Plugin configurations reloaded!");
		} else if (args[0].equalsIgnoreCase("sign")) {
			if (!player.hasPermission(Permissions.SIGN.getPermission())) {
				player.sendMessage(ChatColor.RED + "You don't have permission!");
				return true;
			}
			
			int loreIndex = 0;
			
			if (args.length > 1) {
				try {
					loreIndex = Integer.parseInt(args[1]) - 1;
				} catch (NumberFormatException nfe) {
					player.sendMessage(ChatColor.RED + args[1] + " is not a number!");
					return true;
				}
			}
			
			List<String> lores = main.getConfiguration().getItemControlLoreList();
			if (loreIndex >= lores.size()) {
				player.sendMessage(ChatColor.RED + "There is no lore with number " + (loreIndex + 1) + "!");
				return true;
			}
			
			String lore = lores.get(loreIndex);
			ItemStack stack = player.getItemInHand();
			
			if (stack == null) {
				player.sendMessage(ChatColor.RED + "There is no item in your hand!");
				return true;
			}
			
			ItemMeta meta = stack.getItemMeta();
			List<String> itemLore = meta.getLore();
			if (itemLore == null) {
				itemLore = Lists.newArrayList();
			}
			
			itemLore.add(lore);
			meta.setLore(itemLore);
			
			stack.setItemMeta(meta);
			player.setItemInHand(stack);
			player.sendMessage(ChatColor.GRAY + "Lore has been applied to the item in your hand.");
		} else {
			if (!player.hasPermission(Permissions.TRADE.getPermission())) {
				player.sendMessage(ChatColor.RED + "You don't have permission!");
				return true;
			}
			
			String partnerName = args[0];
			Player tradePartner = Bukkit.getPlayer(partnerName);
			if (tradePartner == null) {
				player.sendMessage(ChatColor.RED + "Player with name \'" + partnerName + "\' could not be found.");
				return true;
			}
			
			if (tradePartner == player) {
				player.sendMessage(ChatColor.RED + "You cannot trade with yourself!");
				return true;
			}
			
			if (!main.getConfiguration().allowsCreativeTrading() && (player.getGameMode() == GameMode.CREATIVE || tradePartner.getGameMode() == GameMode.CREATIVE)) {
				player.sendMessage(messageConfig.getMessage(Messages.CREATIVE, tradePartner.getName()));
				return true;
			}
			
			int maxDistance = main.getConfiguration().getMaximumTradeDistance();
			if (player.getWorld() != tradePartner.getWorld() || player.getLocation().distanceSquared(tradePartner.getLocation()) > maxDistance * 2 ) {
				player.sendMessage(ChatColor.RED + "Your partner is too far away!");
				return true;
			}
			
			factory.initiateTrade(player, tradePartner);
		}
		
		return true;
	}
}
