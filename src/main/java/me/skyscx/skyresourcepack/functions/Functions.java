package me.skyscx.skyresourcepack.functions;

import me.skyscx.skyresourcepack.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.net.URL;

import static me.skyscx.skyresourcepack.Messages.*;

public class Functions {
    private final ResourcePackStatusManager resourcePackStatusManager;
    private final Messages messages;
    private final Plugin plugin;

    public Functions(ResourcePackStatusManager resourcePackStatusManager, Messages messages, Plugin plugin) {
        this.resourcePackStatusManager = resourcePackStatusManager;
        this.messages = messages;
        this.plugin = plugin;
    }
    public boolean isUrlValid(String urlString) {
        try {
            URL url = new URL(urlString);
            url.openConnection().connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean isNumeric(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
    public void checkResourcePackStatus(Player player, String name, int id) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLaterAsynchronously(plugin, () -> {
            scheduler.runTaskTimerAsynchronously(plugin, () -> {
                PlayerResourcePackStatusEvent.Status status = resourcePackStatusManager.getResourcePackStatus(player.getUniqueId());
                String statusString = status.toString();
                System.out.println(statusString);
                if (statusString.equalsIgnoreCase("SUCCESSFULLY_LOADED")) {
                    String message = messages.loadRP(name, id);
                    player.sendMessage(message);
                    scheduler.cancelTasks(plugin);
                } else if (statusString.equalsIgnoreCase("FAILED_DOWNLOAD") || statusString.equalsIgnoreCase("FAILED_RELOAD")) {
                    player.sendMessage(failLoadRP);
                    scheduler.cancelTasks(plugin);
                }
            }, 0L, 20L);
            scheduler.runTaskLaterAsynchronously(plugin, () -> {
                PlayerResourcePackStatusEvent.Status status = resourcePackStatusManager.getResourcePackStatus(player.getUniqueId());
                if (status != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                    player.sendMessage(failCooldownLoadRP);
                    scheduler.cancelTasks(plugin);
                }
            }, 1200L);
        }, 20L);
    }

}
