package co.lotc.core.bukkit.util.TimeRollover;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Is thrown by RolloverUtil once per hour.
 */
public class HourRolloverEvent extends Event implements Cancellable {

	private boolean isCancelled;
	private static final HandlerList HANDLER_LIST = new HandlerList();

	public HourRolloverEvent() {
		this.isCancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		isCancelled = b;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
