package co.lotc.core.bukkit.command;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DefaultArgs {
	private static final BrigadierProvider provider = BrigadierProvider.get();

	private DefaultArgs() {}

	// World
	public static void buildWorldParameter() {
		Commands.defineArgumentType(World.class)
				.defaultName("World")
				.completer((s,$) -> getWorldStringList())
				.mapperWithSender((sender, world) -> Bukkit.getWorld(world))
				.register();
	}

	private static List<String> getWorldStringList() {
		List<String> output = new ArrayList<>();
		for (World world : Bukkit.getWorlds()) {
			output.add(world.getName());
		}
		return output;
	}

	// Material
	public static void buildMaterialParameter() {
		Commands.defineArgumentType(Material.class)
		.brigadierType(provider.argumentItemStack())
		.mapper( parse.andThen(ItemStack::getType) )
		.defaultName("item")
		.defaultError("please specify a valid item name")
		.register();
	}

	// ItemStack
	public static void buildItemStackParameter() {
		Commands.defineArgumentType(ItemStack.class)
			.brigadierType(provider.argumentItemStack())
			.mapper(parse)
			.defaultName("itemstack")
			.defaultError("please specify a valid item name")
			.register();
	}
	
	private static Function<String, ItemStack> parse = input->{
		try {
			Object argumentPredicateItemStack = provider.argumentItemStack().parse(new StringReader(input));
			Object nmsStack = provider.getItemStackParser().invoke(argumentPredicateItemStack, 1, false);
			return MinecraftReflection.getBukkitItemStack(nmsStack);
		} catch (CommandSyntaxException e) { //User Parsing error
			return null;
		} catch (Exception e) { //Unexpected error
			e.printStackTrace();
			return null;
		}
	};

	// Color
	public static void buildBungeeColorParameter() {
		Commands.defineArgumentType(net.md_5.bungee.api.ChatColor.class)
				.defaultName("Color")
				/*.completer((s,$) -> {
					List<String> values = Arrays.stream(org.bukkit.ChatColor.values()).map(object -> Objects.toString(object, null)).collect(Collectors.toList());
					values.add("hex-code");
					return values;
				}) TODO Figure out how to grab a list of bungee chat colors post deprecation.*/
				.mapperWithSender((sender, color) -> {
					try {
						return net.md_5.bungee.api.ChatColor.of(color); // Try to parse through normal chat colors first.
					} catch (IllegalArgumentException mustBeAHex) {
						try {
							return net.md_5.bungee.api.ChatColor.of(hexToColor(color)); // Couldn't parse it as a normal color, how about as hex?
						} catch (Exception ignore) {
							return null; // At this point the color cannot be parsed and we return null.
						}
					}
				})
				.register();
	}

	public static void buildBukkitColorParameter() {
		Commands.defineArgumentType(org.bukkit.ChatColor.class)
				.defaultName("Color")
				//.completer((s,$) -> Arrays.stream(org.bukkit.ChatColor.values()).map(object -> Objects.toString(object, null)).collect(Collectors.toList()))
				.mapperWithSender((sender, color) -> {
					try {
						return org.bukkit.ChatColor.valueOf(color); // Try to parse through normal chat colors first.
					} catch (IllegalArgumentException mustBeAHex) {
						//try {
						//	return org.bukkit.ChatColor.valueOf(hexToColor(color)); // Couldn't parse it as a normal color, how about as hex?
						//} catch (Exception ignore) {
							return null; // At this point the color cannot be parsed and we return null.
						//} TODO Figure out how bukkit ChatColor uses the new hex values.
					}
				})
				.register();
	}

	private static final Pattern hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6})$");
	private static Color hexToColor(String hex) {
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
