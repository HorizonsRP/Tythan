package co.lotc.core.bungee.util;


import co.lotc.core.agnostic.AbstractChatBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatBuilder extends AbstractChatBuilder<ChatBuilder> {

	public ChatBuilder() {
		super("");
	}
	
	public ChatBuilder(String initial) {
		super(initial);
	}
	
	public ChatBuilder send(CommandSender s) {
		//ProxiedPlayer extends this. Should work for all
		s.sendMessage(this.build());
		return getThis();
	}
	
	@Override
	protected ChatBuilder getThis() {
		return this;
	}


	/**
	 * Appends a new TextComponent to the provided TextComponent with the given message and color.
	 * @param base The base TextComponent. If null a new one will be created and returned with the appended message.
	 * @param message The raw string to use within the appended TextComponent.
	 * @param color The color to set the appended TextComponent.
	 * @return A copy of the base TextComponent, or a new TextComponent if the base was null.
	 */
	public static TextComponent appendTextComponent(TextComponent base, String message, ChatColor color) {
		return appendTextComponent(base, message, color, false, false);
	}

	/**
	 * Appends a new TextComponent to the provided TextComponent with the given message and color.
	 * @param base The base TextComponent. If null a new one will be created and returned with the appended message.
	 * @param message The raw string to use within the appended TextComponent.
	 * @param color The color to set the appended TextComponent.
	 * @param bold Whether the appended TextComponent should be bold.
	 * @param italic Whether the appended TextComponent should be italic.
	 * @return A copy of the base TextComponent, or a new TextComponent if the base was null.
	 */
	public static TextComponent appendTextComponent(TextComponent base, String message, ChatColor color,
													boolean bold, boolean italic) {
		return appendTextComponent(base, message, color, bold, italic, false, false, false);
	}

	/**
	 * Appends a new TextComponent to the provided TextComponent with the given message and color.
	 * @param base The base TextComponent. If null a new one will be created and returned with the appended message.
	 * @param message The raw string to use within the appended TextComponent.
	 * @param color The color to set the appended TextComponent.
	 * @param bold Whether the appended TextComponent should be bold.
	 * @param italic Whether the appended TextComponent should be italic.
	 * @param under Whether the appended TextComponent should be underlined.
	 * @param obfusc Whether the appended TextComponent should be obfuscated.
	 * @param strike Whether the appended TextComponent should be striken through.
	 * @return A copy of the base TextComponent, or a new TextComponent if the base was null.
	 */
	public static TextComponent appendTextComponent(TextComponent base, String message, ChatColor color,
													boolean bold, boolean italic, boolean under, boolean obfusc, boolean strike) {
		return appendTextComponent(base, message, color, bold, italic, under, obfusc, strike, null, null, null);
	}

	/**
	 * Appends a new TextComponent to the provided TextComponent with the given message and color.
	 * @param base The base TextComponent. If null a new one will be created and returned with the appended message.
	 * @param message The raw string to use within the appended TextComponent.
	 * @param color The color to set the appended TextComponent.
	 * @param bold Whether the appended TextComponent should be bold.
	 * @param italic Whether the appended TextComponent should be italic.
	 * @param under Whether the appended TextComponent should be underlined.
	 * @param obfusc Whether the appended TextComponent should be obfuscated.
	 * @param strike Whether the appended TextComponent should be striken through.
	 * @param font The font to use for the appended TextComponent.
	 * @param hover A HoverEvent to be applied to the appended TextComponent.
	 * @param click A ClickEvent to be applied to the appended TextComponent.
	 * @return A copy of the base TextComponent, or a new TextComponent if the base was null.
	 */
	public static TextComponent appendTextComponent(TextComponent base, String message, ChatColor color,
													boolean bold, boolean italic, boolean under, boolean obfusc, boolean strike,
													String font, HoverEvent hover, ClickEvent click) {
		if (base == null) {
			base = new TextComponent();
		}

		TextComponent componentMessage = new TextComponent();

		if (message != null) {
			componentMessage.setText(message);
		}

		if (color != null) {
			componentMessage.setColor(color);
		}

		componentMessage.setBold(bold);
		componentMessage.setItalic(italic);
		componentMessage.setStrikethrough(strike);
		componentMessage.setUnderlined(under);
		componentMessage.setObfuscated(obfusc);

		if (font != null) {
			componentMessage.setFont(font);
		}

		if (hover != null) {
			componentMessage.setHoverEvent(hover);
		}

		if (click != null) {
			componentMessage.setClickEvent(click);
		}

		base.addExtra(componentMessage);
		return base;
	}

}
