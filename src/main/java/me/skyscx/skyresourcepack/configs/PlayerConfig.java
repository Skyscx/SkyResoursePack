package me.skyscx.skyresourcepack.configs;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static me.skyscx.skyresourcepack.Messages.*;

public class PlayerConfig {
    private FileConfiguration config;
    private final File file;
    public PlayerConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void setupDefaultParamets() {
        if (!config.contains("url-wiki")) {
            config.set("url-wiki", "https://example.com");
        }
        if (!config.contains("show-join-message")){
            config.set("show-join-message", true);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadPlayersConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void savePlayerRP(Player player, String name){
        config.set("players." + player.getName() + ".name-rp", name);
        try {
            config.save(file);
        }catch (Exception e){
            player.sendMessage(failConfigPlayer);
        }
    }
    public void delPlayerRP(Player player){
        config.set("players." + player.getName() + ".name-rp", null);
        try {
            config.save(file);
        }catch (Exception e){
            player.sendMessage(failConfigPlayer);
        }
    }
    public void firstSetAutoRPSettingPlayer(Player player) {
        String playerName = player.getName();
        if (!config.contains("players." + playerName)) {
            config.set("players." + playerName + ".auto-rp", false);
            if (config.getBoolean("show-join-message")){
                player.sendMessage(firstJoin + config.getString("url-wiki"));
            }
        }
        try {
            config.save(file);
        }catch (Exception e){
            player.sendMessage(failConfigPlayer);
        }
    }
    public void getMessageWikiURP(CommandSender sender){
        String message = firstJoin + config.getString("url-wiki");
        sender.sendMessage(message);
    }
    public boolean setURLwiki(String url){
        if (url != null){
            config.set("url-wiki", url);
            try {
                config.save(file);
            }catch (Exception e){
                System.getLogger(failConfigPlayer);
            }
            return true;
        }
        return false;
    }

    public void delRP(String name) {
        ConfigurationSection playersSection = config.getConfigurationSection("players");
        if (playersSection != null) {
            for (String playerName : playersSection.getKeys(false)) {
                ConfigurationSection playerSection = playersSection.getConfigurationSection(playerName);
                if (playerSection != null && playerSection.contains("name-rp") && Objects.equals(playerSection.getString("name-rp"), name)) {
                    playersSection.set(playerName, null);
                    try {
                        config.save(file);
                    }catch (Exception e){
                        System.getLogger(failConfigPlayer);
                    }
                    return;
                }
            }
        }
    }
    public boolean getAutoRpPlayer(Player player){
        return config.getBoolean("players." + player.getName() + ".auto-rp");
    }
    public void toggleAutoRpPlayer(Player player, boolean param){
        config.set("players." + player.getName() + ".auto-rp", param);
        try {
            config.save(file);
        }catch (Exception e){
            System.getLogger(failConfigPlayer);
        }
    }
    public String searchPlayerParams(Player player){
        String playerName = player.getName();
        if (config.contains("players." + playerName)){
            if (getAutoRpPlayer(player)){
                return config.getString("players." + playerName + ".name-rp");
            }
        }
        return null;
    }
}