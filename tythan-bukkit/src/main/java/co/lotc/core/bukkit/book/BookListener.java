package co.lotc.core.bukkit.book;

import co.lotc.core.bukkit.TythanBukkit;
import co.lotc.core.bukkit.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookListener implements Listener {

	private static Map<UUID, BookStream> bookStreamMap = new HashMap<>();
	private static Map<UUID, Integer> oldSlotMap = new HashMap<>();
	private static Map<UUID, ItemStack> oldItemMap = new HashMap<>();

	protected static void addStreamMap(Player p, BookStream stream) {
		bookStreamMap.put(p.getUniqueId(), stream);
	}
	protected static void addItemMap(Player p, int slot, ItemStack item) {
		UUID uuid = p.getUniqueId();
		oldSlotMap.put(uuid, slot);
		oldItemMap.put(uuid, item);
	}

	/**
	 * Plays when a book is closed (PlayerEditBookEvent) to remove the item
	 * BookStream from the list, if it's on it. Plays the BookStream's
	 * onBookClose method after 20 ticks.
	 */
	@EventHandler(ignoreCancelled=true)
	public void onBookClose(PlayerEditBookEvent e) {
		if (ItemUtil.hasCustomTag(e.getPreviousBookMeta(), BookStream.BOOK_TAG)) {
			UUID uuid = e.getPlayer().getUniqueId();
			if (bookStreamMap.containsKey(uuid)) {
				e.setCancelled(true);

				new BukkitRunnable() {
					@Override
					public void run() {
						e.getPlayer().getInventory().setItem(oldSlotMap.get(uuid), oldItemMap.get(uuid));
						oldSlotMap.remove(uuid);
						oldItemMap.remove(uuid);
					}
				}.runTaskLaterAsynchronously(TythanBukkit.get(), 5);

				BookStream stream = bookStreamMap.get(uuid);
				bookStreamMap.remove(uuid);

				ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
				book.setItemMeta(e.getNewBookMeta());
				stream.setBookData(book);
				stream.onBookClose();
			}
		}
	}

}
