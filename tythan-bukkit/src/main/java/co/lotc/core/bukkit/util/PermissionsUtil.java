package co.lotc.core.bukkit.util;

import co.lotc.core.bukkit.TythanBukkit;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PermissionsUtil {

	/**
	 * @deprecated Only returns the given defaultAmount. Please update to using AtomicInteger.
	 */
	public static int getMaxPermission(UUID player, String permission, int defaultAmount) {
		return defaultAmount;
	}

	/**
	 * @deprecated Only returns the given defaultAmount. Please update to using AtomicInteger.
	 */
	public static int getMinPermission(UUID player, String permission, int defaultAmount) {
		return defaultAmount;
	}

	/**
	 * Gets the highest numerical representation for a given permission node. permission.*, *, and permission.unlimited grant max value.
	 * @param base An AtomicInteger that is used as the default number and the integer to check when complete.
	 * @param player UUID of the player in question.
	 * @param permission Start of the permission node to check. ex: tythan.permission will look for the highest x in tythan.permission.x
	 * @return An AtomicBoolean that will update to true when the check is complete.
	 */
	public static AtomicBoolean getMaxPermission(AtomicInteger base, UUID player, String permission) {
		return getMinMaxPermission(base, player, permission, false);
	}

	/**
	 * Gets the lowest numerical representation for a given permission node. permission.*, *, and permission.unlimited grant max value.
	 * @param base An AtomicInteger that is used as the default number and the integer to check when complete.
	 * @param player UUID of the player in question.
	 * @param permission Start of the permission node to check. ex: tythan.permission will look for the highest x in tythan.permission.x
	 * @return An AtomicBoolean that will update to true when the check is complete.
	 */
	public static AtomicBoolean getMinPermission(AtomicInteger base, UUID player, String permission) {
		return getMinMaxPermission(base, player, permission, true);
	}

	private static AtomicBoolean getMinMaxPermission(AtomicInteger base, UUID player, String permission, boolean min) {
		AtomicBoolean finished = new AtomicBoolean(false);
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			LuckPerms api = provider.getProvider();
			CompletableFuture<User> future = api.getUserManager().loadUser(player);
			future.thenAccept(user -> {
				if (user != null) {
					for (Node node : user.data().toCollection()) {
						String key = node.getKey();
						if (key.startsWith(permission)) {
							String[] split = key.replace(".", " ").split(" ");
							try {
								String data = split[split.length - 1];
								if (data.equalsIgnoreCase("*") || data.equalsIgnoreCase("unlimited")) {
									base.set(Integer.MAX_VALUE);
									break;
								} else {
									int value = Integer.parseInt(data);
									if (min) {
										if (value < base.get()) {
											base.set(value);
										}
									} else {
										if (value > base.get()) {
											base.set(value);
										}
									}
									base.addAndGet(value);
								}
							} catch (Exception e) {
								TythanBukkit.get().getLogger().warning("Node failed to parse for min/max permission: " + key);
								e.printStackTrace();
							}
						}
					}
				}
				finished.set(true);
			});
		}
		return finished;
	}

	public static AtomicBoolean getTotalPermission(AtomicInteger base, UUID player, String permission) {
		AtomicBoolean finished = new AtomicBoolean(false);
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			LuckPerms api = provider.getProvider();
			CompletableFuture<User> future = api.getUserManager().loadUser(player);
			future.thenAccept(user -> {
				if (user != null) {
					Map<String, Boolean> permissionsMap = user.getCachedData().getPermissionData(QueryOptions.defaultContextualOptions()).getPermissionMap();
					for (String key : permissionsMap.keySet()) {
						if (key.startsWith(permission) && permissionsMap.get(key)) {
							String[] split = key.replace(".", " ").split(" ");
							try {
								String data = split[split.length - 1];
								if (data.equalsIgnoreCase("*") || data.equalsIgnoreCase("unlimited")) {
									base.set(Integer.MAX_VALUE);
									break;
								} else {
									int value = Integer.parseInt(data);
									base.addAndGet(value);
								}
							} catch (Exception e) {
								TythanBukkit.get().getLogger().warning("Node failed to parse for total permission: " + key);
								e.printStackTrace();
							}
						}
					}
				}
				finished.set(true);
			});
		}
		return finished;
	}

}
