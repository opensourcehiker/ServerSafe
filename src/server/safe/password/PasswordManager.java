package server.safe.password;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import message.Send;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import server.safe.Core;

public class PasswordManager {
	private Core core = Core.core;
	private Map<UUID,String> passwords = new HashMap<UUID,String>();
	private Map<UUID,Boolean> login = new HashMap<UUID,Boolean>();
	
	public PasswordManager() {
		this.loadPasswords();
		for (Player player : core.getServer().getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			login.put(uuid, true);
		}
		for (OfflinePlayer player : core.getServer().getOfflinePlayers()) {
			UUID uuid = player.getUniqueId();
			login.put(uuid, false);
		}
	}

	public Map<UUID,String> getPasswords() {
		return passwords;
	}

	public void setPasswords(Map<UUID,String> passwords) {
		this.passwords = passwords;
	}
	
	public void setPassword(UUID uuid, String password) {
			passwords.put(uuid,password);
	}
	
	public void removePassword(UUID uuid) {
		passwords.remove(uuid);
	}
	
	public String getPassword(UUID uuid) {
		return passwords.get(uuid);
	}
	
	public boolean hasPassword(UUID uuid) {
		return passwords.containsKey(uuid);
	}
	
	public void loadPasswords() {
		try {
		File file =  new File(core.getDataFolder() + "/passwords.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			try {
				core.getDataFolder().mkdirs();
				file.createNewFile();
				cfg.save(file);
				return;
			} catch (IOException e) {
				Send.errorToConsole("Error creating new password file.", e);
				return;
			}
		}
		if (cfg.contains("passwords")) {
		for (String uuidString : cfg.getConfigurationSection("passwords").getKeys(false)) {
			UUID uuid = UUID.fromString(uuidString);
			String password = this.decrypt(cfg.getString("passwords." + uuidString));
			this.setPassword(uuid, password);
		}
		}
		}catch (Exception e) {
			Send.errorToConsole("Error loading passwords.", e);
		}
	}
	
	public void savePasswords() {
		try {
			File file =  new File(core.getDataFolder() + "/passwords.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Send.errorToConsole("Error creating new password file.", e);
					return;
				}
			}
			for (UUID uuid : this.getPasswords().keySet()) {
				String uuidString = uuid.toString();
				String password = this.getPassword(uuid);
				cfg.set("passwords." + uuidString, this.encrypt(password));
			}
			cfg.save(file);
			}catch (Exception e) {
				Send.errorToConsole("Error saving passwords.", e);
			}
		}

	public Map<UUID,Boolean> getLogins() {
		return login;
	}

	public void setLogins(Map<UUID,Boolean> login) {
		this.login = login;
	}
	
	public void setLoginStatus(UUID uuid, boolean status) {
		login.put(uuid, status);
	}
	
	public void removeLoginStatus(UUID uuid) {
		login.remove(uuid);
	}
	
	public boolean isLoggedIn(UUID uuid) {
		return login.get(uuid);
	}
	
	public String encrypt(String code) {
		String encrypt = "";
		Random r = new Random();
		for (int i = 0; i < code.length(); i++) {
			int w = r.nextInt(8) + 1;
			encrypt = (encrypt + String.valueOf(w));
			for (int x = 1; x <= w; x++) {
				encrypt = (encrypt + this.generateRandomKey());
			}
			encrypt = (encrypt + String.valueOf(code.charAt(i)));
		}
		return encrypt;
}
	
	public String decrypt(String code) {
		String fin = "";
		String dc = code;
		while(dc.length() > 0) {
			int w = Integer.parseInt(String.valueOf(dc.charAt(0)));
			dc = dc.substring((w + 1), dc.length());
			fin = (fin + String.valueOf(dc.charAt(0)));
			dc = dc.substring(1, dc.length());
		}
		return fin;
	}
	
	public String generateRandomKey() {
		String idk = null;
		Random r = new Random();
		int type = r.nextInt(2) + 1;
		switch(type) {
		case 1:
			String alph = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
			int w = r.nextInt(51) + 1;
			char letter = alph.charAt(w);
			idk = String.valueOf(letter);
			 break;
		case 2:
			String single = "0123456789";
			int w1 = r.nextInt(9) + 1;
			char numb = single.charAt(w1);
			idk = String.valueOf(numb);
			break;
		}
		return idk;
	}
}
