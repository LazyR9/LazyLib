package me.lazyr9.lazylib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Util libary for Paper plugins, made by LazyR9.
 * This class contains non-static helper methods
 * 
 * @author LazyR9
 * @see me.lazyr9.lazylib.Utils Static methods
 */
public class LazyLib {
	
	private JavaPlugin plugin;
	
	/**
	 * Main constructor for general utils.
	 * These utils are specific to a certain plugin. 
	 * 
	 * @param plugin A {@link JavaPlugin} to use the utils on
	 */
	public LazyLib(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Get a {@link FileConfiguration} for a config file.
	 * 
	 * @param file The File object to get a config from.
	 * @return A {@link FileConfiguration} for the file name provided.
	 * @throws IllegalArgumentException If the file given isn't a .yml file.
	 * @throws FileNotFoundException If no file is found.
	 */
	public FileConfiguration getConfig(File file) throws FileNotFoundException {
		return getConfig(file.getName());
	}
	
	/**
	 * Get a {@link FileConfiguration} for a config file.
	 * 
	 * @param fileName The name of the file.
	 * @return A {@link FileConfiguration} for the file name provided.
	 * @throws IllegalArgumentException If the file given isn't a .yml file.
	 * @throws FileNotFoundException If no file is found.
	 */
	public FileConfiguration getConfig(String fileName) throws FileNotFoundException {
		if (!fileName.endsWith(".yml")) {
			throw new IllegalArgumentException("File must be a .yml file");
		}
		File folder = plugin.getDataFolder();
		if (!folder.exists()) {
			throw new FileNotFoundException("This plugin does not have its own data folder!");
		}
		File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equals(fileName);
			}
		});
		if (files.length == 0) {
			throw new FileNotFoundException("No file was found for " + fileName);
		}
		return YamlConfiguration.loadConfiguration(files[0]);
	}
	
	/**
	 * Get a list of all config files ending with .yml in the base plugin folder.
	 * For configs not in the main folder,
	 * use supply the folder with the filename to {@link #getConfig(String) getConfig()}
	 * @return A {@link FileConfiguration} array for all .yml files
	 */
	public FileConfiguration[] getConfigs() {
		// Add new 
		List<FileConfiguration> list = new ArrayList<>();
		for (File file : plugin.getDataFolder().listFiles()) {
			if (!file.getName().endsWith(".yml")) {
				continue;
			}
			list.add(YamlConfiguration.loadConfiguration(file));
		}
		return (FileConfiguration[]) list.toArray();
	}
	
	/**
	 * Gets the plugin for this LazyLib instance.
	 * 
	 * @return This instance's plugin.
	 */
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	
}
