package server.safe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import message.Send;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener{
	private Core core = Core.core;
	private Map<UUID,Long> cooldown = new HashMap<UUID,Long>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (core.getPasswordManager().hasPassword(player.getUniqueId())) {
		Send.info(player, "Type /login [password] to login.");
		}else{
		Send.warn(player, "You do not have a password set. Type /pset [password] to set one.");
		}
		}
		
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		core.getPasswordManager().setLoginStatus(player.getUniqueId(), false);
		core.log(player.getName() + " logged out");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			if (!e.getMessage().startsWith("/login") && !e.getMessage().startsWith("/pset")) {
			e.setCancelled(true);
			Send.status(player, "You are not logged in. Type /login [password] to login.");
		}
		}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			player.teleport(e.getFrom());
			Send.status(player, "You are not logged in. Type /login [password] to login.");
		}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e)  {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			e.setCancelled(true);
			Send.status(player, "You are not logged in. Type /login [password] to login.");
		}
		}
		if (core.getSettings().getSpawnProtection() > 0) {
			Location spawn = e.getBlock().getWorld().getSpawnLocation().getBlock().getLocation();
			Location block = e.getBlock().getLocation();
			int protect = core.getSettings().getSpawnProtection();
			if (block.getBlockX() <= (spawn.getBlockX() + protect) && block.getBlockZ() <= (spawn.getBlockZ() + protect)
					&& block.getBlockX() >= (spawn.getBlockX() - protect) && block.getBlockZ() >= (spawn.getBlockZ() - protect)) {
				e.setCancelled(true);
				Send.warn(player, "You must walk at least " + protect + " blocks away from spawn before editing the terrain.");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e)  {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			e.setCancelled(true);
			Send.status(player, "You are not logged in. Type /login [password] to login.");
		}
		}
		if (core.getSettings().getSpawnProtection() > 0) {
			Location spawn = e.getBlock().getWorld().getSpawnLocation().getBlock().getLocation();
			Location block = e.getBlock().getLocation();
			int protect = core.getSettings().getSpawnProtection();
			if (block.getBlockX() <= (spawn.getBlockX() + protect) && block.getBlockZ() <= (spawn.getBlockZ() + protect)
			&& block.getBlockX() >= (spawn.getBlockX() - protect) && block.getBlockZ() >= (spawn.getBlockZ() - protect)) {
				e.setCancelled(true);
				Send.warn(player, "You must walk at least " + protect + " blocks away from spawn before editing the terrain.");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e)  {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			e.setCancelled(true);
			Send.status(player, "You are not logged in. Type /login [password] to login.");
		}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (core.getSettings().hasPasswordProtection()) {
			if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			e.setCancelled(true);
			Send.status(player, "You are not logged in. Type /login [password] to login.");
			}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (core.getSettings().hasPasswordProtection()) {
			Player player = (Player) e.getEntity();
			if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
			e.setCancelled(true);
			}
			if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
			if (!core.getPasswordManager().isLoggedIn(damager.getUniqueId())) {
			e.setCancelled(true);
			Send.status(damager, "You are not logged in. Type /login [password] to login.");
			}
			
		}
		}
			if (core.getSettings().getSpawnProtection() > 0) {
				if (e.getEntity() instanceof Player) {
				Location player = e.getEntity().getLocation();
				Location spawn = player.getWorld().getSpawnLocation().getBlock().getLocation();
				int protect = core.getSettings().getSpawnProtection();
				if (player.getBlockX() <= (spawn.getBlockX() + protect) && player.getBlockZ() <= (spawn.getBlockZ() + protect)
						&& player.getBlockX() >= (spawn.getBlockX() - protect) && player.getBlockZ() >= (spawn.getBlockZ() - protect)) {
					e.setCancelled(true);
					if (e.getDamager() instanceof Player) {
						Player damager = (Player) e.getDamager();
						Send.warn(damager, "That player is in the spawn.");
					}
				}
			}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if (core.getSettings().hasPasswordProtection()) {
		if (!core.getPasswordManager().isLoggedIn(player.getUniqueId())) {
		e.setCancelled(true);
		Send.status(player, "You are not logged in. Type /login [password] to login.");
		return;
		}
		}
		UUID uuid = e.getPlayer().getUniqueId();
		if (core.getSettings().hasSpamProtection()) {
		if (cooldown.containsKey(uuid)) {
			long previousTime = cooldown.get(uuid);
			if (System.currentTimeMillis() > (previousTime + 3000)) {
				core.log("[" + player.getName() + "] " + e.getMessage());
				cooldown.put(uuid, System.currentTimeMillis());
			}else{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "You spoke to fast!");
				return;
			}
	}else{
		core.log("[" + player.getName() + "] " + e.getMessage());
		cooldown.put(uuid, System.currentTimeMillis());
	}
	}
		if (core.getSettings().hasSwearProtection()) {
			boolean swares = core.getSwearManage().searchMessage(e);
			if (swares == true) {
			e.getPlayer().sendMessage(ChatColor.RED + "Please refrain from using vulgar language. Further attempts will result in a ban.");
			e.setCancelled(true);
		}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTNTExplode(EntityExplodeEvent e) {
		Location loc = e.getLocation();
		Location spawn = loc.getWorld().getSpawnLocation();
		int protect = core.getSettings().getSpawnProtection();
		if (loc.getBlockX() <= (spawn.getBlockX() + (protect + 20)) && loc.getBlockZ() <= (spawn.getBlockZ() + (protect + 20))
				&& loc.getBlockX() >= (spawn.getBlockX() - protect - 20) && loc.getBlockZ() >= (spawn.getBlockZ() - protect - 20)) {
			e.blockList().clear();
			e.setYield(0);
		}
	}

}
