package me.skyscx.skyresourcepack.functions;

import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.HashMap;
import java.util.UUID;

public class ResourcePackStatusManager {
    private final HashMap<UUID, PlayerResourcePackStatusEvent.Status> resourcePackStatuses = new HashMap<>();

    public void setResourcePackStatus(UUID playerId, PlayerResourcePackStatusEvent.Status status) {
        resourcePackStatuses.put(playerId, status);
    }

    public PlayerResourcePackStatusEvent.Status getResourcePackStatus(UUID playerId) {
        return resourcePackStatuses.getOrDefault(playerId, PlayerResourcePackStatusEvent.Status.DECLINED);
    }
    public void put(UUID uuid, PlayerResourcePackStatusEvent.Status status) {
        resourcePackStatuses.put(uuid, status);
    }
}
