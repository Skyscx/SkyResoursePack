package me.skyscx.skyresourcepack;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;


import static me.skyscx.skyresourcepack.Messages.*;

public class ResourseConfig {
    private FileConfiguration config;
    private final File file;
    public ResourseConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void setupDefaultParamets() {
//        if (!config.contains("server-rp")) {
//            System.out.println("Null RP");
//            config.set("server-rp", null);
//        }
        if (!config.contains("auto-rp-server")) {
            config.set("auto-rp-server", true);
        }
        if (!config.contains("available-upload")){
            config.set("available-upload", 10); // 10 по умолчанию, настроить можно будет в конфиге
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public boolean checkContainsResoursePack(int id, CommandSender sender){
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null) {
            for (String resourcePackName : resourcePackSection.getKeys(false)) {
                int resourcePackId = resourcePackSection.getInt(resourcePackName + ".id");
                if (resourcePackId == id) {
                    return false;
                }
            }
        }
        sender.sendMessage(notRP);
        return true;
    }
    public boolean saveRP(String name, String url, String player, CommandSender sender){
        int id = getFirstAvailableId();
        if (id == -1){
            id = getMaxResourcePackIdFromConfig() + 1;
        }
        config.set("resourcepack." + name + ".id", id);
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
    public String getNameRP(int id){
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null){
            for (String resourcePackName : resourcePackSection.getKeys(false)) {
                int resourcePackId = resourcePackSection.getInt(resourcePackName + ".id");
                if (resourcePackId == id) {
                    return resourcePackName;
                }
            }
        }
        return null;
    }
    public String getUrlRP(String name){
        return config.getString("resourcepack." + name + ".url");
    }
    public String getOwnerRP(String name){
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
        return -1;
    }
    public int getFirstAvailableId() {
        Set<Integer> usedIds = new HashSet<>();
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null) {
            for (String resourcePackName : resourcePackSection.getKeys(false)) {
                int id = resourcePackSection.getInt(resourcePackName + ".id");
                usedIds.add(id);
            }
        }
        for (int id = 1; id <= Integer.MAX_VALUE; id++) {
            if (!usedIds.contains(id)) {
                return id;
            }
        }

        return -1; // Возвращаем -1, если все ID заняты (не должно происходить)
    }
    public boolean checkServRP(){
        boolean bool = config.getBoolean("auto-rp-server");
        System.out.println(bool);
        return bool;
    }
    public int getVersion(String name){
        return config.getInt("resourcepacl." + name + ".version") + 1;
    }
    public List<String> getAllRP() {
        List<String> resourcePacks = new ArrayList<>();
        ConfigurationSection resourcePackSection = config.getConfigurationSection("resourcepack");
        if (resourcePackSection != null) {
            resourcePacks.addAll(resourcePackSection.getKeys(false));
        }
        return resourcePacks;
    }
    public int getAvailableUpload(){
        return config.getInt("available-upload") - 1;
    }
    public int getUploadedRP(Player player){
        int count = 0;
        ConfigurationSection resourcepackSection = config.getConfigurationSection("resourcepack");
        if (resourcepackSection != null) {
            for (String key : resourcepackSection.getKeys(false)) {
                if (config.getString("resourcepack." + key + ".player").equalsIgnoreCase(player.getName())) {
                    count++;
                }
            }
        }
        return count;
    }

}
