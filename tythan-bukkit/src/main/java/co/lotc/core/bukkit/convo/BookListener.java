package co.lotc.core.bukkit.convo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

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
			stream.onBookClose();
		}
	}

}
