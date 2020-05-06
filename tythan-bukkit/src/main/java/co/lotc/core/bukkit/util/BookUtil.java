package co.lotc.core.bukkit.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookUtil {

	/**
	 * Get the pages of a given book split into each word by spaces
	 * and line breaks. Line breaks will appear as their own word (\n) in
	 * this array for easy parsing.
	 * @param book The book to extract from.
	 * @return A list of words split by spaces and line breaks. Returns
	 * null if the item given did not have a BookMeta attached.
	 */
	public static String[] getPagesAsArray(ItemStack book) {
		if (book.getItemMeta() instanceof BookMeta) {
			return getPagesAsArray((BookMeta) book.getItemMeta());
		}
		return null;
	}

	/**
	 * Get the pages of a given book meta split into each word by spaces
	 * and line breaks. Line breaks will appear as their own word (\n) in
	 * this array for easy parsing.
	 * @param meta The BookMeta to extract from.
	 * @return A list of words split by spaces and line breaks.
	 */
	public static String[] getPagesAsArray(BookMeta meta) {
		String desc = getPagesAsString(meta);
		return desc.split(" ");
	}

	/**
	 * Get the pages of a given book as one long string including line
	 * breaks and chat colors.
	 * @param book The book to extract from.
	 * @return A singular string with colour codes and line breaks included.
	 * Returns null if the item given did not have a BookMeta attached.
	 */
	public static String getPagesAsString(ItemStack book) {
		if (book.getItemMeta() instanceof BookMeta) {
			return getPagesAsString((BookMeta) book.getItemMeta());
		}
		return null;
	}

	/**
	 * Get the pages of a given BookMeta as one long string including
	 * line breaks and chat colors.
	 * @param meta The BookMeta to extract from.
	 * @return A singular string with colour codes and line breaks included.
	 */
	public static String getPagesAsString(BookMeta meta) {
		StringBuilder combinedDesc = new StringBuilder();
		for (String str : meta.getPages()) {
			if (combinedDesc.length() > 0) {
				combinedDesc.append(" ");
			}
			if (str.contains("\n")) {
				String[] lineBreaks = str.split("\\R", -1);
				for (int i = 0; i < lineBreaks.length; i++) {
					combinedDesc.append(lineBreaks[i]);
					if (i < lineBreaks.length-1) {
						combinedDesc.append(" \n ");
					}
				}
			} else {
				combinedDesc.append(str);
			}
		}

		return combinedDesc.toString();
	}

}
