package server.safe.settings;

import java.io.File;
import java.io.IOException;

import message.Send;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import server.safe.Core;

public class Settings {
	private Core core = Core.core;
	private boolean spamProtection = true;
	private boolean swearProtection = true;
	private boolean passwordProtection = true;
	private int spawnProtection = 16;
	
	public Settings() {
		this.loadSettings();
	}
	//TODO: ALWAYS BLOCKING AT SPAWN!!! 
	//THIS AND THAT AND WHATEVER. WHY? OH YEAH, DO THE IF STATEMENTS SEPERATELY!!!
	public boolean hasSpamProtection() {
		return spamProtection;
	}
	public void setSpamProtection(boolean spamProtection) {
		this.spamProtection = spamProtection;
	}
	public boolean hasSwearProtection() {
		return swearProtection;
	}
	public void setSwearProtection(boolean swearProtection) {
		this.swearProtection = swearProtection;
	}
	public boolean hasPasswordProtection() {
		return passwordProtection;
	}
	public void setPasswordProtection(boolean passwordProtection) {
		this.passwordProtection = passwordProtection;
	}
	public int getSpawnProtection() {
		return spawnProtection;
	}
	public void setSpawnProtection(int spawnProtection) {
		this.spawnProtection = spawnProtection;
	}
	
	public void loadSettings() {
		File file =  new File(core.getDataFolder() + "/settings.yml");
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
		if (cfg.contains("spamProtection")) {
			boolean spam = cfg.getBoolean("spamProtection");
			this.setSpamProtection(spam);
		}
		if (cfg.contains("spawnProtection")) {
			int spawn = cfg.getInt("spawnProtection");
			this.setSpawnProtection(spawn);
		}
		if (cfg.contains("passwordProtection")) {
			boolean password = cfg.getBoolean("passwordProtection");
			this.setSpamProtection(password);
		}
		if (cfg.contains("swearProtection")) {
			boolean swear = cfg.getBoolean("swearProtection");
			this.setSpamProtection(swear);
		}
	}
	
	public void saveSettings() {
		File file =  new File(core.getDataFolder() + "/settings.yml");
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
		cfg.set("spamProtection", this.hasSpamProtection());
		cfg.set("swearProtection", this.hasSwearProtection());
		cfg.set("passwordProtection", this.hasPasswordProtection());
		cfg.set("spawnProtection", this.getSpawnProtection());
		try {
			cfg.save(file);
		} catch (IOException e) {
			Send.errorToConsole("Error creating saving settings.", e);
			return;
		}
	}
	

}
