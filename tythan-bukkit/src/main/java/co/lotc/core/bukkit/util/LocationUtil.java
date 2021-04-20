package co.lotc.core.bukkit.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.lotc.core.Tythan;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LocationUtil {

	public enum Direction {
		NEG_NORTH(-180),
		NORTHEAST(-135),
		EAST(-90),
		SOUTHEAST(-45),
		SOUTH(0),
		SOUTHWEST(45),
		WEST(90),
		NORTHWEST(135),
		POS_NORTH(180);

		@Getter
		private final float yaw;

		private Direction(int yaw) {
			this.yaw = yaw;
		}

		public static Direction getDirectionByName(String directionName) {
			if (directionName.equalsIgnoreCase("north")) {
				return POS_NORTH;
			}

			for (Direction direction : values()) {
				if (direction.name().equalsIgnoreCase(directionName)) {
					return direction;
				}
			}
			return POS_NORTH;
		}

		public static Direction getDirectionFromYaw(float yaw) {
			Direction output = NEG_NORTH;
			if (yaw > 180 || yaw < -180) {
				return output;
			}

			for (Direction direction : values()) {
				if (direction == output) {
					continue;
				}

				if (yaw < direction.yaw) {
					float difference = (direction.yaw - output.yaw)/2;
					if (yaw >= direction.yaw - difference) {
						output = direction;
					}
					break;
				} else {
					output = direction;
				}
			}

			return output;
		}
	}

	public static String serializeLocation(Location location) {
		YamlConfiguration yaml = new YamlConfiguration();
		yaml.set("l", location.serialize());
		return yaml.saveToString();
	}

	@SuppressWarnings("unchecked")
	public static Location deserializeLocation(String serializedLocation) {
		YamlConfiguration yaml = new YamlConfiguration();
		try {
			yaml.loadFromString(serializedLocation);
			if(!yaml.isList("l")) throw new IllegalArgumentException("String must have a location under key 'l'");
			List<Location> locations = yaml.getList("l").stream()
										   .map(ent -> (Map<String, Object>) ent)
										   .map(ent -> ent == null ? null : Location.deserialize(ent))
										   .collect(Collectors.toList());
			if (locations.size() > 0) {
				if (locations.size() > 1 && Tythan.get().isDebugging()) {
					Tythan.get().getLogger().warning("[DEBUG] Found more than one location in the provided serialized location.");
				}
				return locations.get(0);
			} else {
				if (Tythan.get().isDebugging()) {
					Tythan.get().getLogger().warning("[DEBUG] Found zero locations in the provided serialized location.");
				}
				return null;
			}
		} catch (InvalidConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private LocationUtil() {}

	public static boolean isClose(Entity e, Location l) {
		return isClose(e, l, 5);
	}
	
	public static boolean isClose(Entity e, Location l, double maxDist) {
		return isClose(e.getLocation(), l, maxDist);
	}
	
	public static boolean isClose(Entity e1, Entity e2) {
		return isClose(e1, e2, 5);
	}
	
	public static boolean isClose(Entity e1, Entity e2, double maxDist) {
		return isClose(e1.getLocation(), e2.getLocation(), maxDist);
	}
	
	public static boolean isClose(Location l1, Location l2) {
		return isClose(l1, l2, 5);
	}
	
	public static boolean isClose(Location l1, Location l2, double maxDist) {
		return l1.getWorld() == l2.getWorld() && l1.distanceSquared(l2) <= maxDist*maxDist;
	}

	public static <T extends Entity> T getNearest(Entity e, List<T> ents) {
		ents.remove(e);
		return getNearest(e.getLocation(), ents);
	}
	
	public static <T extends Entity> T getNearest(Location l, List<T> ents) {
		double dist = 99999999999999999999999d;
		T result = null;
		for(T e : ents) {
			double dist2 = l.distanceSquared(e.getLocation());
			if(dist2 < dist) {
				dist = dist2;
				result = e;
			}
		}
		return result;
	}
	
	public static boolean isFloating(LivingEntity le) {
		return isInWater(le) || isInLava(le);
	}
	
	public static boolean isInLava(LivingEntity le) {
		Block b = le.getLocation().getBlock();
		return b.getType() == Material.LAVA;
	}
	
	public static boolean isInWater(LivingEntity le) {
		Block b = le.getLocation().getBlock();
		return b.getType() == Material.WATER;
	}
}
