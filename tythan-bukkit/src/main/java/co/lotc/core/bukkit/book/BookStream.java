package co.lotc.core.bukkit.book;

import co.lotc.core.bukkit.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class BookStream {

	protected static final String BOOK_TAG = "tythan-book-stream";

	private ItemStack book;

	public BookStream(ItemStack book, String title) {
		ItemMeta meta = book.getItemMeta();
		meta.setDisplayName(title);

		ItemStack clone = book.clone();
		clone.setItemMeta(meta);
		setBookData(clone);
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
		int slot = player.getInventory().getHeldItemSlot();
		ItemStack oldItem = player.getInventory().getItem(slot);

		ItemUtil.setCustomTag(this.book, BOOK_TAG, player.getUniqueId().toString());
		BookListener.addStreamMap(player, this);
		BookListener.addItemMap(player, slot, oldItem);

		player.getInventory().setItem(slot, book);
	}

	/**
	 * This runs when the player exits the book they were editing.
	 * Use getItem() and getMeta() to access the stored data.
	 */
	public abstract void onBookClose();

}
