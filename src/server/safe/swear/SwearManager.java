package server.safe.swear;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import message.Send;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import server.safe.Core;

public class SwearManager {
	private Core core = Core.core;
	private List<String> swears = new ArrayList<String>();
	private long lastUpdate = 0;
	
	public SwearManager() {
		this.loadSwears();
	}

	public List<String> getSwears() {
		return swears;
	}

	public void setSwears(List<String> swears) {
		this.swears = swears;
	}
	
	public void addSwear(String swear) {
		swears.add(swear);
	}
	
	public void removeSwear(String swear) {
		swears.remove(swear);
	}
	
	public void loadSwears() {
		File file =  new File(core.getDataFolder() + "/swears.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			try {
				file.createNewFile();
				cfg.save(file);
				return;
			} catch (IOException e) {
				Send.errorToConsole("Error creating new swear file.", e);
				return;
			}
		}
		if (cfg.contains("lastUpdate")) {
			long lastUpdate = cfg.getLong("lastUpdate");
			if (lastUpdate > 0) {
			this.setLastUpdate(lastUpdate);
			}else{
			this.setLastUpdate(System.currentTimeMillis());
			}
			}
		if (cfg.contains("swears")) {
			List<String> swears = cfg.getStringList("swears");
			this.swears.addAll(swears);
		}
		if ((lastUpdate + 86400000) < System.currentTimeMillis()) {
			this.update();
			this.setLastUpdate(System.currentTimeMillis());
		}
		core.log("Successfully updated swear list from the repository.");
	}
	
	public void saveSwears() {
		File file = new File(core.getDataFolder() + "/swears.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				core.log("Failed to create new swear data file.");
			}
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("swears", this.swears);
		cfg.set("lastUpdate", lastUpdate);
		try {
			cfg.save(file);
		} catch (IOException e) {
			core.log("Failed to save swears.");
		}
	}
	
	public void update() {
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		for (char letter : alphabet) {
			try {
				Connection connect = Jsoup.connect("http://www.noswearing.com/dictionary/" + letter + "/").userAgent("Mozilla").timeout(0);
				Document doc = connect.get();
				Elements swearsParent = doc.getElementsByAttributeValue("valign", "top");
				Elements swears = swearsParent.get(0).children();
				for (Element swearElement : swears) {
		String swear = swearElement.attr("name");
		if (!swear.equalsIgnoreCase("top")) {
		String reverse = "";
		 int length = swear.length();
	      for ( int i = length - 1 ; i >= 0 ; i-- )
	         reverse = reverse + swear.charAt(i);
	      if (swear.matches("[a-zA-Z]+")) {
	      this.addSwear(swear);
	      this.addSwear(reverse);
	      }
		}
				}
		}catch (IOException e) {
			core.log(ChatColor.RED + "Error while attempting to connect to swear repository" + letter + ".");
			e.printStackTrace();
			return;
		}
		}
	}
	
	public boolean searchMessage(AsyncPlayerChatEvent e) {
		String stripped = e.getMessage().replaceAll(" ", "").replaceAll("_", "").replaceAll("[^\\p{L}\\p{Nd}]+", "").toLowerCase();
		String noExtension = "";
		for (int i = 0; i < stripped.length(); i++) {
			char letter = stripped.charAt(i);
			if (i > 0) {
			char previous = stripped.charAt(i - 1);
			if (letter != previous) {
				noExtension = noExtension + letter;
			}
			}else{
				noExtension = noExtension + letter;
			}
		}
		String newMessage = "";
		for (char letter : noExtension.toCharArray()) {
			char newLetter = letter;
			if (letter == 'o') {
				newLetter = 'u';
			}
			if (letter == 'j') {
				newLetter = 'g';
			}
			newMessage = newMessage + newLetter;
		}
		newMessage = newMessage.replaceAll("ph", "f");
		for (String swear : this.getSwears()) {
			if (e.getMessage().contains(swear) || stripped.contains(swear) || newMessage.contains(swear) || noExtension.contains(swear)) {
				core.log("BLOCKED MESSAGE: [" + e.getPlayer().getName() + "] " + e.getMessage());
				return true;
			}
		}
		return false;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
