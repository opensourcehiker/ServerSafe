package server.safe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import message.Send;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import server.safe.password.PasswordManager;
import server.safe.settings.Settings;
import server.safe.swear.SwearManager;

public class Core extends JavaPlugin{
	public static Core core;
	private PasswordManager passwordManage;
	private SwearManager swearManage;
	private Settings settings;
	
	public static void main(String[] args) {
		
	}
	
	public void onEnable() {
		core = this;
		passwordManage = new PasswordManager();
		swearManage = new SwearManager();
		settings = new Settings();
		this.getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	public void onDisable() {
		passwordManage.savePasswords();
		swearManage.saveSwears();
		settings.saveSettings();
	}

	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (label.equalsIgnoreCase("pset")) {
			if (settings.hasPasswordProtection()) {
			if (args.length == 1) {
				if (!passwordManage.hasPassword(player.getUniqueId())) {
					String password = args[0];
					if (password.length() < 8) {
					Send.warn(player, "The password must be greater than 7 characters.");
					return true;
					}
					passwordManage.setPassword(player.getUniqueId(), password);
					passwordManage.setLoginStatus(player.getUniqueId(), true);
					Send.status(player, "You are logged in.");
					core.log(player.getName() + " logged in");
					Send.success(player, "Your new password is \"" + password + "\".");
				}else{
					Send.warn(player, "You must type your current password followed by the new password.");
				}
			}else if (args.length == 2) {
				if (passwordManage.hasPassword(player.getUniqueId())) {
					String password = args[0];
					String newPassword = args[1];
					if (passwordManage.getPassword(player.getUniqueId()).equals(password)) {
					if (newPassword.length() < 8) {
					Send.warn(player, "The new password must be greater than 7 characters.");
					return true;
					}
					passwordManage.setPassword(player.getUniqueId(), newPassword);
					Send.success(player, "Your new password is \"" + newPassword + "\".");
					}else{
						Send.error(player, "Incorrect password.");
					}
				}else{
					Send.warn(player, "You don't have a password set yet. Simply type your new password.");
				}
			}else{
				Send.warn(player, "Incorrect number of arguments.");
			}
			}else{
				Send.status(player, "Password protection is not enabled.");
			}
			}
		if (label.equalsIgnoreCase("login")) {
			if (core.getSettings().hasPasswordProtection()) {
			if (!passwordManage.isLoggedIn(player.getUniqueId())) {
			if (args.length == 1) {
			if (passwordManage.hasPassword(player.getUniqueId())) {
				String password = passwordManage.getPassword(player.getUniqueId());
				String guess = args[0];
				if (guess.equals(password)) {
					passwordManage.setLoginStatus(player.getUniqueId(), true);
					Send.status(player, "You are logged in.");
					core.log(player.getName() + " logged in");
				}else{
					Send.error(player, "Incorrect password.");
				}
			}else{
				Send.warn(player, "You do not have a password set. Type /pset [password] to set one.");
			}
			}else{
				Send.warn(player, "Incorrect number of arguments.");
			}
			}else{
				Send.error(player, "You are already logged in.");
			}
		}else{
			Send.status(player, "Password protection is not enabled.");
		}
		}
		if (label.equalsIgnoreCase("swear")) {
			if (settings.hasSwearProtection()) {
					if (args.length == 1) {
						String newSwear = args[0];
						swearManage.addSwear(newSwear);
						Send.success(player, "Successfully added the swear \"" + newSwear + "\" to the swear list.");
					}else{
						Send.warn(player, "Incorrect number of arguments.");
					}
			}else{
				Send.status(player, "Swear protection is not enabled.");
			}
		}
		}
		return false;
	}
	
	public PasswordManager getPasswordManager() {
		return passwordManage;
	}
	
	public void log(String logString) {
		this.getServer().getConsoleSender().sendMessage("[NoSwear] " + logString);
		File file = new File(this.getDataFolder() + "/log.txt");
		if (!file.exists()) {
			this.getDataFolder().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				this.getServer().getConsoleSender().sendMessage("[NoSwear] " + ChatColor.RED + "Failed to created new log file.");
			}
		}
			try {
				BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
				String format = new SimpleDateFormat().format(new Date());
				output.append("[" + format + "] " + logString + "\n");
				output.close();
			} catch (IOException e) {
				this.getServer().getConsoleSender().sendMessage("[NoSwear] " + ChatColor.RED + "Error while logging: " + logString);
			}
	}

	public SwearManager getSwearManage() {
		return swearManage;
	}
	
	public Settings getSettings() {
		return settings;
	}

}
