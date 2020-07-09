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
	private static Map<String, UUID> checkedUUIDs = new HashMap<>();
	private static Map<UUID, String> checkedTextures = new HashMap<>();

	/**
	 * Checks via Bukkit method first for online players, then resorts to MojangCommunicator if not found.
	 * Stores UUIDs locally for 5 mins to prevent contacting Mojang for data we recently grabbed.
	 * @param name Username of the player you wish to find.
	 * @return UUID if found, otherwise returns null.
	 */
	public static UUID getPlayerUUID(String name) {
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

			if (uuid != null) {
				checkedUUIDs.put(name, uuid);
				removeUUIDsForRefresh(name).runTaskLater(TythanBukkit.get(), REFRESH_TIME);
			}
		}

		return uuid;
	}

	private static BukkitRunnable removeUUIDsForRefresh(String name) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				checkedUUIDs.remove(name);
			}
		};
	}
	private static BukkitRunnable removeTexturesForRefresh(UUID uuid) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				checkedTextures.remove(uuid);
			}
		};
	}

	/**
	 * Checks stored textures first then resorts to MojangCommunicator if not found.
	 * Stores textures locally for 5 mins to prevent contacting Mojang for data we recently grabbed.
	 * @param uuid UUID of the player in question.
	 * @return Texture if found, otherwise null.
	 */
	public static String getPlayerTexture(UUID uuid) {
		String texture = checkedTextures.get(uuid);

		if (texture == null) {
			try {
				texture = MojangCommunicator.requestSkinValue(uuid);
			} catch (Exception ignored) {
			}
		}

		if (texture != null) {
			checkedTextures.put(uuid, texture);
			removeTexturesForRefresh(uuid).runTaskLater(TythanBukkit.get(), REFRESH_TIME);
		}

		return texture;
	}

	/**
	 * Checks stored Username to UUID maps first, otherwise gets the player from the UUID if
	 * Bukkit can find it, or as a last resort request the username from Mojang directly.
	 * Afterwards we run a timer to remove the local data after 5 mins.
	 * @param uuid UUID of the player you wish to get a proper username from.
	 * @return The username, if found, otherwise null.
	 */
	public static String getPlayerName(UUID uuid) {
		String name = null;
		if (checkedUUIDs.containsValue(uuid)) {
			for (String key : checkedUUIDs.keySet()) {
				if (checkedUUIDs.get(key).equals(uuid)) {
					name = key;
					break;
				}
			}
		}

		if (name == null) {
			Player p = Bukkit.getPlayer(uuid);
			if (p != null) {
				name = p.getName();
			}
		}

		if (name == null) {
			try {
				name = MojangCommunicator.requestCurrentUsername(uuid);
			} catch (Exception ignored) {
			}
		}

		if (name != null) {
			checkedUUIDs.put(name, uuid);
			removeUUIDsForRefresh(name).runTaskLater(TythanBukkit.get(), REFRESH_TIME);
		}

		return name;
	}

}
