package ru.Plasticable.VipJoinAlert;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin implements Listener
{
	private FileConfiguration c;
	private String message;
	private HashMap<String, Sound> groups = new HashMap<String, Sound>();
	private boolean onlyOwn = false;

	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);

		saveDefaultConfig();
		c = getConfig();
		message = getConfig().getString("message");
		onlyOwn = getConfig().getBoolean("only_own");
		for (String k : c.getConfigurationSection("groups").getKeys(false))
		{
			// System.out.println("Key: " + k);

			Sound s = Sound.valueOf(c.getString("groups." + k));
			if (s == null) continue;
			groups.put(k, s);

		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		PermissionUser u = PermissionsEx.getUser(p);
		String prefix = u.getPrefix(getName());
		
		for (String s : u.getGroupsNames())
			if (groups.containsKey(s))
			{
				Bukkit.broadcastMessage(message.replace("{PREFIX}", prefix)
						.replace("{NICK}", p.getName()).replace("&", "§"));
				if(onlyOwn)
					p.playSound(p.getLocation(), groups.get(s), 1.0f, 1.0f);
				else
					broadcastSound(groups.get(s));
			}
	}

	private void broadcastSound(Sound s)
	{
		for (Player p : Bukkit.getOnlinePlayers())
			p.playSound(p.getLocation(), s, 1.0f, 1.0f);
	}
}
