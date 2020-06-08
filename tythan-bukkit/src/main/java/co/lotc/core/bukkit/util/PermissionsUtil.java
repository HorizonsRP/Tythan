package co.lotc.core.bukkit.util;

import co.lotc.core.bukkit.TythanBukkit;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PermissionsUtil {

	/**
	 * Gets the highest numerical representation for a given permission node. permission.*, *, and permission.unlimited grant max value.
	 * @param player UUID of the player in question.
	 * @param permission Start of the permission node to check. ex: tythan.permission will look for the highest x in tythan.permission.x
	 * @return Highest permission node value.
	 */
	public static int getMaxPermission(UUID player, String permission, int defaultAmount) {
		return getMinMaxPermission(player, permission, defaultAmount, false);
	}

	/**
	 * Gets the lowest numerical representation for a given permission node. permission.*, *, and permission.unlimited grant max value.
	 * @param player UUID of the player in question.
	 * @param permission Start of the permission node to check. ex: tythan.permission will look for the highest x in tythan.permission.x
	 * @return Lowest permission node value.
	 */
	public static int getMinPermission(UUID player, String permission, int defaultAmount) {
		return getMinMaxPermission(player, permission, defaultAmount, true);
	}

	private static int getMinMaxPermission(UUID player, String permission, int defaultAmount, boolean min) {
		int output = defaultAmount;
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			LuckPerms api = provider.getProvider();
			CompletableFuture<User> future = api.getUserManager().loadUser(player);
			future.complete(api.getUserManager().getUser(player));
			User user = api.getUserManager().getUser(player);
			if (user != null) {
				for (Node node : user.getDistinctNodes()) {
					String key = node.getKey();
					if (key.startsWith(permission)) {
						String[] split = key.split("\\.");
						try {
							int value = Integer.parseInt(split[split.length - 1]);
							if (min) {
								if (value < output) {
									output = value;
								}
							} else {
								if (value > output) {
									output = value;
								}
							}
						} catch (Exception e) {
							TythanBukkit.get().getLogger().warning("Node failed to parse for total permission: " + key);
							e.printStackTrace();
						}
					}
				}
			}
		}
		return output;
	}

	public int getTotalPermission(UUID player, String permission) {
		int total = 0;
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			LuckPerms api = provider.getProvider();
			CompletableFuture<User> future = api.getUserManager().loadUser(player);
			future.complete(api.getUserManager().getUser(player));
			User user = api.getUserManager().getUser(player);
			if (user != null) {
				for (Node node : user.getNodes()) {
					String key = node.getKey();
					if (key.startsWith(permission)) {
						String[] split = key.split("\\.");
						try {
							int value = Integer.parseInt(split[split.length - 1]);
							total += value;
						} catch (Exception e) {
							TythanBukkit.get().getLogger().warning("Node failed to parse for total permission: " + key);
							e.printStackTrace();
						}
					}
				}
			}
		}
		return total;
	}

}
