package co.lotc.core.bukkit.util;

import co.lotc.core.bukkit.TythanBukkit;
import co.lotc.core.util.MojangCommunicator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtil {

	private static final int REFRESH_TIME = 6000;
	private Map<String, UUID> checkedUUIDs = new HashMap<>();

	/**
	 * Checks via Bukkit method first for online players, then resorts to MojangCommunicator if not found.
	 * Stores UUIDs locally for 5 mins to prevent contacting Mojang for data we recently grabbed.
	 * @param name Username of the player you wish to find.
	 * @return UUID if found, otherwise returns null.
	 */
	public UUID getPlayerUUID(String name) {
		UUID uuid = null;
		for (String key : checkedUUIDs.keySet()) {
			if (key.equalsIgnoreCase(name)) {
				uuid = checkedUUIDs.get(key);
				break;
			}
		}

		if (uuid == null) {
			Player p = Bukkit.getPlayer(name);
			if (p == null) {
				try {
					uuid = MojangCommunicator.requestPlayerUUID(name);
				} catch (Exception ignored) {
				}
			} else {
				uuid = p.getUniqueId();
			}
		}

		if (uuid != null) {
			checkedUUIDs.put(name, uuid);
			removeForRefresh(name).runTaskLater(TythanBukkit.get(), REFRESH_TIME);
		}

		return uuid;
	}

	private BukkitRunnable removeForRefresh(String name) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				checkedUUIDs.remove(name);
			}
		};
	}

}
