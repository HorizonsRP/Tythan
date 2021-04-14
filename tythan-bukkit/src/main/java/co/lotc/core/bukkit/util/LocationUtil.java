package co.lotc.core.bukkit.util;

import java.util.List;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

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
