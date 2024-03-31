package me.skyscx.skyresoursepack;

import org.bukkit.command.CommandSender;
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
        return new ArrayList<>(Objects.requireNonNull(config.getConfigurationSection("resourcepack")).getKeys(false));
    }
    public void getListRPString(CommandSender sender){
        List<String> resourcePackNames = new ArrayList<>(Objects.requireNonNull(config.getConfigurationSection("resourcepack")).getKeys(false));
        if (resourcePackNames.isEmpty()) {
            sender.sendMessage(emptyList);
        } else {
            String resourcePacksList = listRP + " " + String.join(", ", resourcePackNames);
            sender.sendMessage(resourcePacksList);
        }
    }
    public List<String> getListRPowner(String player){
        List<String> playerResourcePacks = new ArrayList<>();
        for (String resourcePackName : Objects.requireNonNull(config.getConfigurationSection("resourcepack")).getKeys(false)) {
            String playerName = config.getString("resourcepack." + resourcePackName + ".player");
            if (playerName != null && playerName.equalsIgnoreCase(player)) {
                playerResourcePacks.add(resourcePackName);
            }
        }
        System.out.println(playerResourcePacks);
        return playerResourcePacks;

    }
}
