package me.skyscx.skyresourcepack.functions;

import me.skyscx.skyresourcepack.Messages;
import me.skyscx.skyresourcepack.configs.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.skyscx.skyresourcepack.Messages.*;

public class Functions {
    private final ResourcePackStatusManager resourcePackStatusManager;
    private final Messages messages;
    private final Plugin plugin;
    private final PlayerConfig playerConfig;

    public Functions(ResourcePackStatusManager resourcePackStatusManager, Messages messages, Plugin plugin, PlayerConfig playerConfig) {
        this.resourcePackStatusManager = resourcePackStatusManager;
        this.messages = messages;
        this.plugin = plugin;
        this.playerConfig = playerConfig;
    }
    public CompletableFuture<Boolean> isUrlValidAsync(String urlString) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(urlString);
                url.openConnection().connect();
                return true;
            } catch (IOException e) {
                return false;
            }
        });
    }  // TODO: Пофиксить двойной вывод сообщения о инвалиде юрл
    public CompletableFuture<Boolean> checkUrlAsync(String url) {
        return isUrlValidAsync(url).thenApply(isValid -> !isValid);
    }
    public boolean isNumeric(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
    public CompletableFuture<Boolean> checkResourcePackStatus(Player player) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLaterAsynchronously(plugin, () -> {
            scheduler.runTaskTimerAsynchronously(plugin, () -> {
                PlayerResourcePackStatusEvent.Status status = resourcePackStatusManager.getResourcePackStatus(player.getUniqueId());
                String statusString = status.toString();
                System.out.println(statusString);
                if (statusString.equalsIgnoreCase("SUCCESSFULLY_LOADED")) {
                    scheduler.cancelTasks(plugin);
                    future.complete(true);
                } else if (statusString.equalsIgnoreCase(
                        "FAILED_DOWNLOAD") ||
                        statusString.equalsIgnoreCase("FAILED_RELOAD") ||
                        statusString.equalsIgnoreCase("INVALID_URL"))
                {
                    player.sendMessage(failLoadRPa);
                    scheduler.cancelTasks(plugin);
                    future.complete(false);
                }
            }, 0L, 20L);
            scheduler.runTaskLaterAsynchronously(plugin, () -> {
                PlayerResourcePackStatusEvent.Status status = resourcePackStatusManager.getResourcePackStatus(player.getUniqueId());
                if (status != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                    scheduler.cancelTasks(plugin);
                    future.complete(false);
                }
            }, 1200L);
        }, 20L);
        return future;
    }

}
