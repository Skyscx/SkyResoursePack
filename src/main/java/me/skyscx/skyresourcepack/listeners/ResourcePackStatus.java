package me.skyscx.skyresourcepack.listeners;

import me.skyscx.skyresourcepack.functions.ResourcePackStatusManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackStatus implements Listener {
    private final ResourcePackStatusManager resourcePackStatusManager;

    public ResourcePackStatus(ResourcePackStatusManager resourcePackStatusManager) {
        this.resourcePackStatusManager = resourcePackStatusManager;
    }
    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        System.out.println(status);
        resourcePackStatusManager.setResourcePackStatus(player.getUniqueId(), status);
    }
}
