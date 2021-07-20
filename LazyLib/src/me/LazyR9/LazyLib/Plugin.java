package me.LazyR9.LazyLib;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents the plugin part of the library.
 * Currently doesn't do much, just says that it's loaded.
 * 
 * @author LazyR9
 */
public class Plugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		this.getLogger().info("LazyLibs has been loaded!");
	}

}
