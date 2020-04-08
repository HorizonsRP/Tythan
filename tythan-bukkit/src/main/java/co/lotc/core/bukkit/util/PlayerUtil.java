package co.lotc.core.bukkit.util;

import co.lotc.core.util.MojangCommunicator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtil {

	/**
	 * Checks via Bukkit method first for online players, then resorts to MojangCommunicator if not found.
	 * @param name Username of the player you wish to find.
	 * @return UUID if found, otherwise returns null.
	 */
	public UUID getPlayerUUID(String name) {
		Player p = Bukkit.getPlayer(name);
		UUID uuid = null;
		if (p == null) {
			try {
				uuid = MojangCommunicator.requestPlayerUUID(name);
			} catch (Exception ignored) {}
		} else {
			uuid = p.getUniqueId();
		}

		return uuid;
	}

}
