package co.lotc.core.bukkit.convo;

import co.lotc.core.bukkit.util.BookUtil;
import co.lotc.core.bukkit.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BookStream {

	private static final String BOOK_TAG = "Tythan_BookStream";

	private ItemStack book;

	// GET //
	private ItemStack getItem() {
		return this.book;
	}
	private BookMeta getMeta() {
		if (this.book.getItemMeta() instanceof BookMeta) {
			return (BookMeta) this.book.getItemMeta();
		}
		return null;
	}

	/**
	 * Pass through a book to use as the base for this interaction.
	 * @param book Must be a Material.WRITABLE_BOOK.
	 */
	public void setBookData(ItemStack book) {
		if (book.getType().equals(Material.WRITABLE_BOOK)) {
			this.book = book;
		}
	}

	/**
	 * Opens the book for the given player and registers them to the BookListener.
	 * @param player The player to open the book.
	 */
	public void open(Player player) {
		ItemUtil.setCustomTag(this.book, BOOK_TAG, player.getUniqueId().toString());
		BookListener.addMap(player, this);
		BookUtil.openBook(book, player);
	}

	/**
	 * This runs when the player exits the book they were editing.
	 * Use getItem() and getMeta() to access the stored data.
	 */
	public abstract void onBookClose();

}
