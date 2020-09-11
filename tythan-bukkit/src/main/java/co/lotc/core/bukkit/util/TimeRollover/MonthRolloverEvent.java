package co.lotc.core.bukkit.util.TimeRollover;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Is thrown by RolloverUtil once per month.
 */
public class MonthRolloverEvent extends Event implements Cancellable {

	private boolean active = true;

	@Override
	public boolean isCancelled() {
		return active;
	}

	@Override
	public void setCancelled(boolean b) {
		active = b;
	}

	@Override
	public HandlerList getHandlers() {
		return null;
	}
}
