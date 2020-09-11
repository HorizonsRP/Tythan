package co.lotc.core.bungee.util.TimeRollover;

import co.lotc.core.bungee.TythanBungee;
import lombok.Setter;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class RolloverUtil {

	private static ScheduledTask checker = null;
	@Setter private static LocalDateTime lastDate = null;

	private static Runnable getNewChecker() {
		lastDate = LocalDateTime.now();
		return new Runnable() {
			@Override
			public void run() {
				PluginManager pluginManager = TythanBungee.get().getProxy().getPluginManager();

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

				RolloverUtil.setLastDate(date);
			}
		};
	}

	public static void startRolloverChecking() {
		if (checker != null) {
			checker.cancel();
		}
		checker = TythanBungee.get().getProxy().getScheduler().schedule(TythanBungee.get(), getNewChecker(), 0, 30, TimeUnit.SECONDS);
	}

	public static void stopRolloverChecking() {
		if (checker != null) {
			checker.cancel();
		}
	}

}