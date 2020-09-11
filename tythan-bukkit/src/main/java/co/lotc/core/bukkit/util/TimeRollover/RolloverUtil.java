package co.lotc.core.bukkit.util.TimeRollover;

import co.lotc.core.bukkit.TythanBukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

public class RolloverUtil {

	private static BukkitRunnable checker = null;
	private static LocalDateTime lastDate = null;

	private static BukkitRunnable getNewChecker() {
		lastDate = LocalDateTime.now();
		return new BukkitRunnable() {
			@Override
			public void run() {
				PluginManager pluginManager = TythanBukkit.get().getServer().getPluginManager();

				LocalDateTime date = LocalDateTime.now();

				// Month
				if (date.getMonth() != lastDate.getMonth()) pluginManager.callEvent(new MonthRolloverEvent());

				// Week
				if (!date.getDayOfWeek().equals(lastDate.getDayOfWeek()) &&
					 date.getDayOfWeek().equals(DayOfWeek.MONDAY)         ) {
					pluginManager.callEvent(new WeekRolloverEvent());
				}

				// Day
				if (date.getDayOfYear() != lastDate.getDayOfYear()) pluginManager.callEvent(new DayRolloverEvent());

				// Hour
				if (date.getHour() != lastDate.getHour()) pluginManager.callEvent(new HourRolloverEvent());
			}
		};
	}

	public static void startRolloverChecking() {
		if (checker == null || checker.isCancelled()) {
			checker = getNewChecker();
			checker.runTaskTimer(TythanBukkit.get(), 0, 600);
		}
	}

	public static void stopRolloverChecking() {
		if (checker != null && !checker.isCancelled()) {
			checker.cancel();
		}
	}

	public static boolean isRolloverChecking() {
		return !checker.isCancelled();
	}

}