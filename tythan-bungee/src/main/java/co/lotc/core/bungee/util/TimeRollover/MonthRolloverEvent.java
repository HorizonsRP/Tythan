package co.lotc.core.bungee.util.TimeRollover;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

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

}
