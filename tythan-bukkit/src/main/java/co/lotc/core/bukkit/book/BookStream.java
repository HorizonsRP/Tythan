package co.lotc.core.bukkit.book;

import co.lotc.core.bukkit.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BookStream {

	protected static final String BOOK_TAG = "tythan-book-stream";
	private static Map<UUID, BookStream> activeStreams = new HashMap<>();

	private ItemStack book;
	private Player holder;

	public BookStream(Player holder, ItemStack book, String title) {
		this.holder = holder;
		ItemMeta meta = book.getItemMeta();
		meta.setDisplayName(title);

		ItemStack clone = book.clone();
		clone.setItemMeta(meta);
		setBookData(clone);
		activeStreams.put(holder.getUniqueId(), this);
	}

	/**
	 * Return the current book as an item.
	 */
	public ItemStack getItem() {
		return this.book;
	}

	/**
	 * Return the meta of the current book as BookMeta
	 */
	public BookMeta getMeta() {
		if (this.book.getItemMeta() instanceof BookMeta) {
			return (BookMeta) this.book.getItemMeta();
		}
		return null;
	}

	/**
	 * Pass through a book to use as the base for this interaction.
	 * @param book Must be of Material.WRITABLE_BOOK.
	 */
	public void setBookData(ItemStack book) {
		if (book.getType().equals(Material.WRITABLE_BOOK)) {
			this.book = book.clone();
		}
	}

	/**
	 * Swaps writable books with the given player's item and registers
	 * them to the BookListener, or simply opens the book if it's specifically
	 * a WRITTEN_BOOK just meant for reading. If you're using this for written
	 * books, don't. Just use player.openBook(book); instead. Writable books
	 * still should go through this method.
	 * @param player The player to swap the book for.
	 */
	public void open(Player player) {
		if (book.getType().equals(Material.WRITTEN_BOOK)) {
			player.openBook(book);
			activeStreams.remove(holder.getUniqueId());
		} else {
			int slot = player.getInventory().getHeldItemSlot();
			ItemStack oldItem = player.getInventory().getItem(slot);

			ItemUtil.setCustomTag(this.book, BOOK_TAG, player.getUniqueId().toString());
			BookListener.addStreamMap(player, this);
			BookListener.addItemMap(player, slot, oldItem);

			player.getInventory().setItem(slot, book);
		}
	}

	/**
	 * Returns the item to the player without making any edits.
	 */
	public void abort() {
		BookListener.returnItem(holder);
		activeStreams.remove(holder.getUniqueId());
		holder.sendMessage("Book entry cancelled!");
	}

	/**
	 * Aborts all currently active book streams.
	 */
	public static void abortAll() {
		for (BookStream stream : activeStreams.values()) {
			stream.abort();
		}
	}

	/**
	 * Returns the BookStream for the given player.
	 * @param player The player in question.
	 * @return The relevant BookStream, otherwise null.
	 */
	public static BookStream getFor(Player player) {
		return activeStreams.get(player.getUniqueId());
	}

	/**
	 * This runs when the player exits the book they were editing.
	 * Use getItem() and getMeta() to access the stored data.
	 */
	public abstract void onBookClose();

}
