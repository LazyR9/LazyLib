package me.lazyr9.lazylib;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Static general helper methods
 * 
 * @author LazyR9
 * @see LazyLib Non-static methods
 */
public final class Utils {
	
	/**
	 * Format a message to use in chat.
	 * Will use {@link String#format(String, Object...) String.format()},
	 * then use format it using
	 * {@link ChatColor#translateAlternateColorCodes(char, String) ChatColor.translateAlternateColorCodes()}
	 * using '&' as the color code.
	 * 
	 * @param message The message to be formatted.
	 * @param args The args to apply in {@link String#format(String, Object...) String.format()}
	 * @return The formatted message
	 */
	public static String chat(String message, Object... args) {
		if (message == null) {
			return null;
		}
		// Apply the reset to make sure that formatting is apply properly.
		// Without it everything is italicized.
		return ChatColor.translateAlternateColorCodes('&', String.format(message, args));
	}
	
	/**
	 * Gives the length of the inventory to be able to fit input items in it.
	 * Basically rounds up to the nearest multiple of nine.
	 * 
	 * @param input The number to round up.
	 * @return The number rounded up to the nearest multiple of nine.
	 */
	public static int invLength(int input) {
		return (int) Math.ceil((double) input / 9) * 9;
	}
	
	/**
	 * Creates a new ItemStack based on the parameters provided.
	 * 
	 * @param material The material to use for making the stack.
	 * @param amount The size of the stack.
	 * @param name The display name to use for the stack.
	 * @param lore The lore of the stack.
	 * @return A new ItemStack with all the details from the parameters.
	 */
	public static ItemStack createItem(Material material, int amount, String name, String... lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		List<String> loreList = new ArrayList<String>();
		meta.setDisplayName(Utils.chat(name));
		for (String s : lore) {
			loreList.add(Utils.chat(s));
		}
		meta.setLore(loreList);
		item.setItemMeta(meta);
		return item;
	}
	
}
