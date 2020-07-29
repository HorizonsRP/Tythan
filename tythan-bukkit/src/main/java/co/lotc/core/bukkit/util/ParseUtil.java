package co.lotc.core.bukkit.util;

import co.lotc.core.util.ColorUtil;
import net.md_5.bungee.api.ChatColor;

public class ParseUtil {

	public static String parseWithHexColors(String rawString, char colorKey) {
		boolean gettingColor = false;
		boolean gettingHex = false;
		StringBuilder hexString = new StringBuilder();
		StringBuilder finalString = new StringBuilder();
		for (char c : rawString.toCharArray()) {
			if (gettingHex) {
				if (hexString.length() < 6) {
					hexString.append(c);
				} else {
					gettingColor = false;
					gettingHex = false;
					finalString.append(net.md_5.bungee.api.ChatColor.of(ColorUtil.hexToColor(hexString.toString())));
					hexString = new StringBuilder();
				}
			} else if (gettingColor && c == '#') {
				gettingHex = true;
			} else if (c == colorKey) {
				gettingColor = true;
			} else {
				finalString.append(c);
			}
		}
		return ChatColor.translateAlternateColorCodes(colorKey, finalString.toString());
	}

}
