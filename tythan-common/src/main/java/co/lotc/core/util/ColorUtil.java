package co.lotc.core.util;

import java.awt.*;
import java.util.regex.Pattern;

public class ColorUtil {

	private static final Pattern hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6})$");
	public static Color hexToColor(String hex) {
		if (hex != null) {
			if (!hex.startsWith("#")) {
				hex = "#" + hex;
			}
			if (hexPattern.matcher(hex).matches()) {
				return Color.decode(hex);
			}
		}
		return null;
	}

}
