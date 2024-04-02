package me.skyscx.skyresourcepack.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import static me.skyscx.skyresourcepack.Messages.failSaveCFG;
import static org.bukkit.Bukkit.getLogger;

public class PlayerConfig {
    private FileConfiguration config;
    private final File file;
    public PlayerConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
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
//    public boolean playerLoadRP(Player player, String name){
//        int version = config.getInt("resourcepack." + name + ".version") + 1;
//        config.set("resourcepack." + name + ".url", url);
//        config.set("resourcepack." + name + ".version", version);
//        try {
//            config.save(file);
//        }catch (Exception e){
//            sender.sendMessage(failSaveCFG + e.getMessage());
//            return true;
//        }
//        return false;
//    }
}
