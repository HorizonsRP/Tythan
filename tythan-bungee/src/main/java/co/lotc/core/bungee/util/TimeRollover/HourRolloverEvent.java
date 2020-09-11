package co.lotc.core.bungee.util.TimeRollover;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class HourRolloverEvent extends Event implements Cancellable {

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
