package me.LazyR9.LazyLib.gui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.LazyR9.LazyLib.Utils;

/**
 * Represents a GUI in a Minecraft inventory.
 * 
 * @author LazyR9
 */
public class InventoryGui implements Listener {
	
	private JavaPlugin plugin;

	private Inventory inv;
	private List<ItemStack> buttons = new ArrayList<ItemStack>();

	private List<GuiListener> listeners = new ArrayList<GuiListener>();

	/**
	 * Create a new GUI, and an inventory to go with it.
	 * 
	 * @param listener The class used for GUI event listeners
	 * @param plugin The plugin that this GUI is registered to.
	 * @param name The name of the GUI. Will be applied to the inventory.
	 * @param size The size of the inventory.
	 */
	public InventoryGui(GuiListener listener, JavaPlugin plugin, String name, int size) {
		this.inv = Bukkit.createInventory(null, size, Utils.chat(name));
		Bukkit.getPluginManager().registerEvents(this, plugin);
		listeners.add(listener);
	}

	/**
	 * Gets the inventory for this GUI.
	 * 
	 * @return The inventory used by this GUI instance.
	 */
	public Inventory getInv() {
		return this.inv;
	}

	/**
	 * Shows this GUI to the specified player.
	 * 
	 * @param player The player to show to GUI to
	 */
	public void show(Player player) {
		player.openInventory(inv);
	}

	/**
	 * Closes this GUI for the specified player,
	 * if they are currently looking at it.
	 * 
	 * @param player The player to try and hide the GUI from.
	 */
	public void hide(Player player) {
		// If the player is looking at this inventory, close it
		if (inv.getViewers().contains(player)) {
			player.closeInventory();
		}
	}

	/**
	 * Adds a button to the inventory, and registers an event for it.
	 * If you want an item without sending GUI events, directly add to the inventory.
	 * 
	 * @param button The ItemStack that will represent a button.
	 * @param slot What slot of the inventory the button will be in.
	 */
	public void addButton(ItemStack button, int slot) {
		this.inv.setItem(slot, button);
		buttons.add(button);
	}
	
	/**
	 * Adds multiple buttons to an inventory, starting at the first free spot.
	 * Use addButton() to set the slot that a button will go in.
	 * 
	 * @param buttons The buttons to add to the
	 * @see #addButton(ItemStack, int) addButton()
	 */
	public void addButtons(ItemStack... buttons) {
		this.inv.addItem(buttons);
		this.buttons.addAll(Arrays.asList(buttons));
	}

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		// Standard inventory event stuff
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		// Isn't this instances inventory
		if (event.getInventory() != this.inv) {
			return;
		}
		// Cancel the event, so the item isn't picked up
		// This can be uncancelled from the listener if this is wanted.
		event.setCancelled(true);
		// Make sure that the item clicked isn't in the player's inventory
		// Otherwise they can "make" their own buttons if they have access to the /data command.
		if (event.getClickedInventory() != this.inv) {
			return;
		}
		// If the item clicked isn't registered as a button
		if (!buttons.contains(event.getCurrentItem())) {
			return;
		}
		// Call all listeners, notifying them of the event.
		for (GuiListener listener : listeners) {
			Method[] methods = listener.getClass().getDeclaredMethods();
			for (Method method : methods) {
				// If there isn't an annotation, skip it
				if (!method.isAnnotationPresent(ButtonClickHandler.class)) {
					continue;
				}
				ButtonClickHandler annotation;
				try {
					annotation = method.getAnnotation(ButtonClickHandler.class);
				} catch (Exception e) {
					// Annotation should exist, we just checked for it.
					e.printStackTrace();
					// Don't continue executing if something happens,
					// the annotation variable will not have a value and errors will happen!
					return;
				}
				// If the annotation material is the one we are dealing with
				// or air, the default for the annotation,
				// call the event.
				if (annotation.type() == event.getCurrentItem().getType() ||
						annotation.type() == Material.AIR) {
					try {
						method.invoke(listener, event);
					} catch (IllegalAccessException e) {
						// This error could be users fault
						plugin.getLogger().warning("Skipping " + method.getName() + ": Method is private.");
					} catch (Exception e) {
						// No other errors should happen, but print stack trace if they do.
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent event) {
		// Make sure we are dealing with a player
		if (!(event.getPlayer() instanceof Player)) {
			return;
		}
		// Make sure we are dealing with our inventory
		if (event.getInventory() != this.inv) {
			return;
		}
		// Call all listeners
		for (GuiListener listener : listeners) {
			// Get all methods from the listener
			Method[] methods = listener.getClass().getDeclaredMethods();
			// Go through all methods
			for (Method method : methods) {
				// If the method isn't annotated, skip it
				if (!method.isAnnotationPresent(GuiCloseHandler.class)) {
					continue;
				}
				// Annotation exists, call listener
				try {
					method.invoke(listener, event);
				} catch (IllegalAccessException e) {
					// Error would be caused by user, so show a helpful error message
					plugin.getLogger().warning("Skipping " + method.getName() + ": Method is private.");
				} catch (Exception e) {
					// Any other errors just get printed, no other errors should happen.
					e.printStackTrace();
				}
			}
		}
	}

}
