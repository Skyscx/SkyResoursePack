package me.skyscx.skyresourcepack.configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SignsConfig {
    private final File configFile;
    private FileConfiguration config;
    public SignsConfig(File file) {
        this.configFile = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void reloadSignsConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void signResourcePack(Plugin plugin) {
        for (String key : config.getKeys(false)) {
            String[] locationParts = key.split(",");
            String worldName = locationParts[0];
            int x = Integer.parseInt(locationParts[1]);
            int y = Integer.parseInt(locationParts[2]);
            int z = Integer.parseInt(locationParts[3]);
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location location = new Location(world, x, y, z);
                String rpId = config.getString(key);
                location.getBlock().setMetadata("rpId", new FixedMetadataValue(plugin, rpId));
            }
        }
    }
    public void saveResourcePack(String serializedLocation, String rpId){
        config.set(serializedLocation, rpId);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isResourcePackSign(String serializedLocation) {
        return config.contains(serializedLocation);
    }
    public void deleteResourcePackSign(String serializedLocation) {
        if (config.contains(serializedLocation)) {
            config.set(serializedLocation, null);
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
