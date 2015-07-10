package message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import server.safe.Core;


public class Send {
	private static Core core = Core.core;
	
	public static void info(Player player, String message) {
		String info = ChatColor.DARK_AQUA + "Info> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void warn(Player player, String message) {
		String info = ChatColor.GOLD + "Warn> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void query(Player player, String message) {
		String info = ChatColor.DARK_PURPLE + "Query> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void error(Player player, String message) {
		String info = ChatColor.RED + "Error> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void status(Player player, String message) {
		String info = ChatColor.YELLOW + "Status> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void success(Player player, String message) {
		String info = ChatColor.GREEN + "Success> " + ChatColor.WHITE;
		player.sendMessage(info + message);
	}
	
	public static void command(Player player, String command, String desc) {
		player.sendMessage(ChatColor.DARK_PURPLE + "t " + command + ChatColor.WHITE + " " + desc);
	}
	
	public static void broadcastInfo(String message) {
		String info = ChatColor.DARK_AQUA + "Info> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastWarn(String message) {
		String info = ChatColor.GOLD + "Warn> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastQuery(String message) {
		String info = ChatColor.DARK_PURPLE + "Query> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastError(String message) {
		String info = ChatColor.RED + "Error> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastStatus(String message) {
		String info = ChatColor.YELLOW + "Status> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastSuccess(String message) {
		String info = ChatColor.GREEN + "Success> " + ChatColor.WHITE;
		Bukkit.broadcastMessage(info + message);
	}
	
	public static void broadcastCommand(Player player, String command, String desc) {
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "t " + command + ChatColor.WHITE + " " + desc);
	}
	
	public static void infoToConsole(String message) {
		String info = ChatColor.DARK_AQUA + "Info> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void warnToConsole(String message) {
		String info = ChatColor.GOLD + "Warn> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void queryToConsole(String message) {
		String info = ChatColor.DARK_PURPLE + "Query> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void errorToConsole(String message) {
		String info = ChatColor.RED + "Error> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void errorToConsole(String message,Exception e) {
		String info = ChatColor.RED + "Error> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
		e.printStackTrace();
	}
	
	public static void statusToConsole(String message) {
		String info = ChatColor.YELLOW + "Status> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void successToConsole(String message) {
		String info = ChatColor.GREEN + "Success> " + ChatColor.WHITE;
		core.getServer().getConsoleSender().sendMessage(info + message);
	}
	
	public static void commandToConsole(Player player, String command, String desc) {
		core.getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "t " + command + ChatColor.WHITE + " " + desc);
	}
	
}
