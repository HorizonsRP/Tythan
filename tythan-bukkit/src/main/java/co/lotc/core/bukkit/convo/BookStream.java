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

	// SET //
	public void setBookData(ItemStack book) {
		if (book.getType().equals(Material.WRITABLE_BOOK)) {
			this.book = book;
		}
	}

	// START //
	public void open(Player p) {
		ItemUtil.setCustomTag(this.book, BOOK_TAG, p.getUniqueId().toString());
		BookListener.addMap(p, this);
		BookUtil.openBook(book, p);
	}

	// END //
	public abstract void onBookClose();

}
