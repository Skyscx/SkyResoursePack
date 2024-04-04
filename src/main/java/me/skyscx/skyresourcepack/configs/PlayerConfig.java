package me.skyscx.skyresourcepack.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import static me.skyscx.skyresourcepack.Messages.failConfigPlayer;
import static me.skyscx.skyresourcepack.Messages.failSaveCFG;
import static org.bukkit.Bukkit.getLogger;

public class PlayerConfig {
    private FileConfiguration config;
    private final File file;
    public PlayerConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
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
    public void delRP(String name) {
        ConfigurationSection playersSection = config.getConfigurationSection("players");
        if (playersSection != null) {
            for (String playerName : playersSection.getKeys(false)) {
                ConfigurationSection playerSection = playersSection.getConfigurationSection(playerName);
                if (playerSection != null && playerSection.contains("name-rp") && playerSection.getString("name-rp").equals(name)) {
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







    public void checkInstallRP(Player player){
        Player targetPlayer = Bukkit.getPlayerExact(player.getName());
        if (targetPlayer != null) {
            String getStatusRP = targetPlayer.getResourcePackStatus() != null ? targetPlayer.getResourcePackStatus().name() : null;
            if (getStatusRP != null) {
                getLogger().info(getStatusRP);
            } else {
                getLogger().info("NULL RP");
            }
        } else {
            getLogger().info("PLAYER NOT ONLINE");
        }
    }
}