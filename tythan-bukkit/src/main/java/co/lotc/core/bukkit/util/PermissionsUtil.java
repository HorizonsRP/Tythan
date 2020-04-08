package co.lotc.core.bukkit.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class PermissionsUtil {

	/**
	 * Gets the highest or lowest numerical representation for a given permission node. permission.*, *, and permission.unlimited grant max value.
	 * @param player UUID of the player in question.
	 * @param permission Start of the permission node to check. ex: tythan.permission will look for the highest x in tythan.permission.x
	 * @param min If true, will search for the lowest numerical representation.
	 * @return Highest permission node value.
	 */
	public static int getMinMaxPermission(UUID player, String permission, int defaultAmount, boolean min) {
		int output = defaultAmount;
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			LuckPerms api = provider.getProvider();
			User user = api.getUserManager().getUser(player);
			if (user != null) {
				for (Node node : user.getNodes()) {
					if (node.getValue()) {
						String thisPerm = node.getKey();
						if (thisPerm.equalsIgnoreCase(permission + ".unlimited") ||
							thisPerm.equalsIgnoreCase(permission + ".*") ||
							thisPerm.equalsIgnoreCase("*")) {
							output = Integer.MAX_VALUE;
							break;
						} else if (thisPerm.startsWith(permission)) {
							String[] split = node.getKey().replace('.', ' ').split(" ");
							int value = Integer.parseInt(split[split.length-1]);

							if (min) {
								if (value < output) {
									output = value;
								}
							} else {
								if (value > output) {
									output = value;
								}
							}

						}
					}
				}
			}
		}
		return output;
	}

}
