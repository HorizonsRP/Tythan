package co.lotc.core.bukkit.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookUtil {

	public static String getPagesAsString(ItemStack book) {
		if (book.getItemMeta() instanceof BookMeta) {
			return getPagesAsString((BookMeta) book.getItemMeta());
		}
		return null;
	}

	public static String getPagesAsString(BookMeta meta) {
		StringBuilder combinedDesc = new StringBuilder();
		for (String str : meta.getPages()) {
			if (combinedDesc.length() > 0) {
				combinedDesc.append(" ");
			}
			if (str.contains("\n")) {
				String[] lineBreaks = str.split("\\R", -1);
				for (int i = 0; i < lineBreaks.length; i++) {
					combinedDesc.append(ChatColor.stripColor(lineBreaks[i]));
					if (i < lineBreaks.length-1) {
						combinedDesc.append(" \n ");
					}
				}
			} else {
				combinedDesc.append(ChatColor.stripColor(str));
			}
		}

		return combinedDesc.toString();
	}

}
