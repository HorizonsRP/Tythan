package co.lotc.core.bukkit.util;

import co.lotc.core.util.ColorUtil;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class ParseUtil {

	public static String parseWithHexColors(char colorKey, String rawString) {
		boolean gettingHex = false;
		boolean gettingColor = false;
		StringBuilder hexString = new StringBuilder();
		StringBuilder finalString = new StringBuilder();
		for (char c : rawString.toCharArray()) {
			if (gettingHex) {
				hexString.append(c);
				if (hexString.length() == 6) {
					Color color = ColorUtil.hexToColor(hexString.toString());
					if (color != null) {
						finalString.append(ChatColor.of(color));
					} else {
						finalString.append("&#").append(hexString.toString());
					}

					gettingColor = false;
					gettingHex = false;
					hexString = new StringBuilder();
				}
			} else if (gettingColor) {
				if (c == '#') {
					gettingHex = true;
				} else {
					finalString.append(ChatColor.getByChar(c));
					gettingColor = false;
				}
			} else if (c == colorKey) {
				gettingColor = true;
			} else {
				finalString.append(c);
			}
		}
		return finalString.toString();
	}

}
