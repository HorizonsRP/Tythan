package co.lotc.core.bukkit.convo;

import co.lotc.core.bukkit.TythanBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookListener implements Listener {

	private static Map<UUID, BookStream> bookStreamMap = new HashMap<>();

	public static void addMap(Player p, BookStream stream) {
		bookStreamMap.put(p.getUniqueId(), stream);
	}

	// LISTEN //
	@EventHandler(ignoreCancelled=true)
	public void onBookClose(PlayerEditBookEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		if (bookStreamMap.containsKey(uuid)) {
			BookStream stream = bookStreamMap.get(uuid);
			bookStreamMap.remove(uuid);
			new BukkitRunnable() {
				@Override
				public void run() {
					stream.onBookClose();
				}
			}.runTaskLaterAsynchronously(TythanBukkit.get(), 2);
		}
	}

}
