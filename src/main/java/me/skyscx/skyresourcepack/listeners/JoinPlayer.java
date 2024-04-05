package me.skyscx.skyresourcepack.listeners;

import me.skyscx.skyresourcepack.Messages;
import me.skyscx.skyresourcepack.configs.PlayerConfig;
import me.skyscx.skyresourcepack.configs.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import me.skyscx.skyresourcepack.functions.Functions;
import me.skyscx.skyresourcepack.functions.ResourcePackStatusManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.Objects;

import static me.skyscx.skyresourcepack.Messages.failAutoLoadRP;

public class JoinPlayer implements Listener {
    private final ResourseConfig resourceConfig;
    private final SkyResourcePack plugin;
    private final PlayerConfig playerConfig;
    private final ResourcePackStatusManager resourcePackStatusManager;
    private final Functions functions;
    private final Messages messages;
    public JoinPlayer(SkyResourcePack plugin, ResourseConfig resourceConfig, PlayerConfig playerConfig, ResourcePackStatusManager resourcePackStatusManager, Functions functions, Messages messages) {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
        this.playerConfig = playerConfig;
        this.resourcePackStatusManager = resourcePackStatusManager;
        this.functions = functions;
        this.messages = messages;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerConfig.firstSetAutoRPSettingPlayer(player);
        String name = playerConfig.searchPlayerParams(player);
        if (name != null){
            if (name.equalsIgnoreCase("server")){
                String url = resourceConfig.getServerRPurl();
                if (url != null) {
                    player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                    return;
                }
                return;
            }
            int id = resourceConfig.getIdRP(name);
            boolean containtRP = resourceConfig.checkContainsResoursePack(id, player);
            if (containtRP){return;}
            String urlRP = resourceConfig.getUrlRP(name);
            player.setResourcePack(urlRP, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
            resourcePackStatusManager.put(player.getUniqueId(), PlayerResourcePackStatusEvent.Status.DECLINED);
            functions.checkResourcePackStatus(player).thenAccept(success -> {
                if (success) {
                    String message = messages.autoLoadRP(name, id);
                    player.sendMessage(message);
                    playerConfig.savePlayerRP(player, name);

                } else {
                    String url = resourceConfig.getServerRPurl();
                    if (url != null) {player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));}
                    player.sendMessage(failAutoLoadRP);
                }
            });
        } else {
            if (resourceConfig.checkServRP()) {
                String url = resourceConfig.getServerRPurl();
                if (url != null) {player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));}
            }
        }

    }
}
