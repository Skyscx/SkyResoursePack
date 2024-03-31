package me.skyscx.skyresoursepack;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.skyscx.skyresoursepack.Messages.*;

public class ResourseConfig {
    private FileConfiguration config;
    private final File file;
    public ResourseConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadResourceConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**Getter and Setter**/
    public boolean getContainsResoursePack(String name, CommandSender sender){
        if (config.contains("resourcepack." + name)){
            sender.sendMessage(existingRP);
            return true;
        }
        return false;
    }
    public boolean checkContainsResoursePack(String name, CommandSender sender){
        if (!config.contains("resourcepack." + name)){
            sender.sendMessage(notRP);
            return true;
        }
        return false;
    }
    public boolean saveRP(String name, String url, String player, CommandSender sender){
        int maxId = getMaxResourcePackIdFromConfig();
        config.set("resourcepack." + name + ".id", maxId + 1);
        config.set("resourcepack." + name + ".url", url);
        config.set("resourcepack." + name + ".version", 1);
        config.set("resourcepack." + name + ".player", player);
        try {
            config.save(file);
        } catch (IOException e) {
            sender.sendMessage(failSaveCFG + e.getMessage());
            return true;
        }
        return false;
    }
    public int getMaxResourcePackIdFromConfig() {
        int maxId = 0;
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null) {
            for (String name : resourcePackSection.getKeys(false)) {
                int id = config.getInt("resourcepack." + name + ".id");
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId;
    }

    public String getUrlRP(String name){
        return config.getString("resourcepack." + name + ".url");
    }
    public String getOwnerRP(String name){
        String player = config.getString("resourcepack." + name + ".player");
        System.out.println("Player own: " + player);
        return config.getString("resourcepack." + name + ".player");
    }
    public boolean deleteRP(String name, CommandSender sender){
        config.set("resourcepack." + name, null);
        try {
            config.save(file);
        } catch (IOException e) {
            sender.sendMessage(failSaveCFG + e.getMessage());
            return true;
        }
        return false;
    }
    public boolean updateRP(String name, String url, CommandSender sender){
        int version = config.getInt("resourcepack." + name + ".version") + 1;
        config.set("resourcepack." + name + ".url", url);
        config.set("resourcepack." + name + ".version", version);
        try {
            config.save(file);
        }catch (Exception e){
            sender.sendMessage(failSaveCFG + e.getMessage());
            return true;
        }
        return false;
    }
    public String getServerRPurl(){
        return config.getString("server-rp");
    }
    public boolean setServerRPurl(String url, CommandSender sender){
        config.set("server-rp", url);
        try {
            config.save(file);
        }catch (Exception e){
            sender.sendMessage(failSaveCFG + e.getMessage());
            return true;
        }
        return false;
    }
    public List<String> getListRP(){
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(resourcePackSection.getKeys(false));
    }
    public void getListRPString(CommandSender sender) {
        List<String> resourcePackNames = getListRP();
        if (resourcePackNames.isEmpty()) {
            sender.sendMessage(emptyList);
        } else {
            String resourcePacksList = listRP + " " + String.join(", ", resourcePackNames);
            sender.sendMessage(resourcePacksList);
        }
    }
    public List<String> getListRPowner(String player) {
        List<String> playerResourcePacks = new ArrayList<>();
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null) {
            for (String resourcePackName : resourcePackSection.getKeys(false)) {
                String playerName = resourcePackSection.getString(resourcePackName + ".player");
                if (playerName != null && playerName.equalsIgnoreCase(player)) {
                    playerResourcePacks.add(resourcePackName);
                }
            }
        }
        return playerResourcePacks;
    }
    public int getIdRP(String name) {
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack." + name);
        if (resourcePackSection != null) {
            return resourcePackSection.getInt("id");
        }
        return -1; // Возвращаем -1, если секция не найдена
    }

}
