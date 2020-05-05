package co.lotc.core.bukkit.book;

import co.lotc.core.bukkit.util.NMSUtil;
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
			Constructor<?> serializerConstructor = NMSUtil.getNMSClass("PacketDataSerializer").getConstructor(ByteBuf.class);
			Object packetDataSerializer = serializerConstructor.newInstance(buf);

			Constructor<?> keyConstructor = NMSUtil.getNMSClass("MinecraftKey").getConstructor(String.class);
			Object bookKey = keyConstructor.newInstance("minecraft:book_open");

			Constructor<?> titleConstructor = NMSUtil.getNMSClass("PacketPlayOutCustomPayload").getConstructor(bookKey.getClass(), NMSUtil.getNMSClass("PacketDataSerializer"));
			Object payload = titleConstructor.newInstance(bookKey, packetDataSerializer);

			NMSUtil.sendPacket(p, payload);
		} catch (Exception e) {
			e.printStackTrace();
		}
		p.getInventory().setItem(slot, oldItem);
	}
}