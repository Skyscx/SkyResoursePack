package me.skyscx.skyresourcepack.listeners;

import me.skyscx.skyresourcepack.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class JoinPlayer implements Listener {
    private final ResourseConfig resourceConfig;
    private final SkyResourcePack plugin;
    public JoinPlayer(SkyResourcePack plugin, ResourseConfig resourceConfig) {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (resourceConfig.checkServRP()) {
            Player player = event.getPlayer();
            String url = resourceConfig.getServerRPurl();
            if (url != null) {
                System.out.println("TRUE JOIN");
                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
            } else {
                System.out.println("URL of the resource pack is null. Please check the configuration file.");
            }
        }
        System.out.println("FALSE JOIN");
    }
}
