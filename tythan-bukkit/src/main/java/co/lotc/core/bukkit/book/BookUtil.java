package co.lotc.core.bukkit.book;

import java.lang.reflect.Method;
import java.util.List;

import co.lotc.core.bukkit.util.NMSUtils;
import co.lotc.core.bukkit.util.ReflectionUtil.PackageType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public class BookUtil {

	public static void openBook(ItemStack book, Player p) {
		int slot = p.getInventory().getHeldItemSlot();
		ItemStack oldItem = p.getInventory().getItem(slot);
		p.getInventory().setItem(slot, book);

		ByteBuf buf = Unpooled.buffer(256);
		buf.setByte(0, (byte)0);
		buf.writerIndex(1);

		try {
			Constructor<?> serializerConstructor = NMSUtils.getNMSClass("PacketDataSerializer").getConstructor(ByteBuf.class);
			Object packetDataSerializer = serializerConstructor.newInstance(buf);

			Constructor<?> keyConstructor = NMSUtils.getNMSClass("MinecraftKey").getConstructor(String.class);
			Object bookKey = keyConstructor.newInstance("minecraft:book_open");

			Constructor<?> titleConstructor = NMSUtils.getNMSClass("PacketPlayOutCustomPayload").getConstructor(bookKey.getClass(), NMSUtils.getNMSClass("PacketDataSerializer"));
			Object payload = titleConstructor.newInstance(bookKey, packetDataSerializer);

			NMSUtils.sendPacket(p, payload);
		} catch (Exception e) {
			e.printStackTrace();
		}
		p.getInventory().setItem(slot, oldItem);
	}
}