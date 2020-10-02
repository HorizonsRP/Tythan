package co.lotc.core.bukkit.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
		return getPagesAsArray(meta.getPages());
	}

	/**
	 * Re-format the list of pages as per BookMeta standards split into each
	 * word by spaces and line breaks. Line breaks will appear as their own word
	 * (\n) in this array for easy parsing.
	 * @param pages The pages of a book as per BookMeta standards.
	 * @return A list of words split by spaces and line breaks.
	 */
	public static String[] getPagesAsArray(List<String> pages) {
		String desc = getPagesAsString(pages);
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
		return getPagesAsString(meta.getPages());
	}

	/**
	 * Return the list of pages as one long string as per BookMeta
	 * standards, including line breaks and chat colors still present.
	 * @param pages The pages as per BookMeta standards.
	 * @return A singular string representing the pages put together.
	 */
	public static String getPagesAsString(List<String> pages) {
		StringBuilder combinedDesc = new StringBuilder();
		if (pages != null) {
			for (String str : pages) {
				if (combinedDesc.length() > 0) {
					combinedDesc.append(" ");
				}
				if (str.contains("\n")) {
					String[] lineBreaks = str.split("\\R", -1);
					for (int i = 0; i < lineBreaks.length; i++) {
						combinedDesc.append(lineBreaks[i]);
						if (i < lineBreaks.length - 1) {
							combinedDesc.append(" \n ");
						}
					}
				} else {
					combinedDesc.append(str);
				}
			}
		}
		return combinedDesc.toString();
	}

	/**
	 * @param string Returns a book-friendly page-list with the contents
	 *               as the given string. Replaces a few combos with
	 *               unique codes.
	 *               [*1] = Small 1x1 Dot
	 *               [*]  = Medium 2x2 Dot
	 *               [*4] = Large 4x4 Dot
	 *               [**] = Split Diamond
	 */
	public static List<String> getPagesForString(String string) {
		List<String> output = new ArrayList<>();
		string = string.replace("･", "[*1]");
		string = string.replace("•", "[*]");
		string = string.replace("●", "[*4]");
		string = string.replace("❖", "[**]");
		int pages = (int) Math.ceil(string.length() / 255d); // 255 is the max chars per page in current MC books.
		for (int i = 1; i <= pages; i++) {
			int j = (i - 1) * 255;
			int k = i * 255;

			if (string.length() < k) {
				k = string.length();
			}
			output.add(string.substring(j, k));
		}
		return output;
	}

}
