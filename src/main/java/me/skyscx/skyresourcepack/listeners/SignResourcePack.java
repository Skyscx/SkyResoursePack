package me.skyscx.skyresourcepack.listeners;

import me.skyscx.skyresourcepack.Messages;
import me.skyscx.skyresourcepack.configs.PlayerConfig;
import me.skyscx.skyresourcepack.configs.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import me.skyscx.skyresourcepack.configs.SignsConfig;
import me.skyscx.skyresourcepack.functions.Functions;
import me.skyscx.skyresourcepack.functions.ResourcePackStatusManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

import static me.skyscx.skyresourcepack.Messages.*;

public class SignResourcePack implements Listener {
    private final ResourseConfig resourceConfig;
    private final SkyResourcePack plugin;
    private final Functions functions;
    private final SignsConfig signsConfig;
    private final Messages messages;
    private final ResourcePackStatusManager resourcePackStatusManager;
    private final PlayerConfig playerConfig;
    public SignResourcePack(SkyResourcePack plugin, ResourseConfig resourceConfig, Functions functions, SignsConfig signsConfig, ResourcePackStatusManager resourcePackStatusManager, PlayerConfig playerConfig, Messages messages) {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
        this.signsConfig = signsConfig;
        this.functions = functions;
        this.resourcePackStatusManager = resourcePackStatusManager;
        this.playerConfig = playerConfig;
        this.messages = messages;
    }
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        boolean isLineReplaced = false;
        for (int i = 0; i < event.getLines().length; i++) {
            String line = event.getLine(i);
            assert line != null;
            if (line.matches("\\[rp:(server|\\d+)\\]")) {
                if (isLineReplaced) {return;}
                String rpId = line.replaceAll("\\[rp:", "").replaceAll("\\]", "");
                if (rpId.equalsIgnoreCase("server")) {
                    event.getBlock().setMetadata("rpId", new FixedMetadataValue(this.plugin, "server"));
                    event.setLine(i, "§u§aRP-SERVER");
                } else {
                    int id = Integer.parseInt(rpId);
                    if (resourceConfig.checkingID(id)) {event.getBlock().setMetadata("rpId", new FixedMetadataValue(this.plugin, rpId));}
                    event.setLine(i, "§u§aRP-" + rpId);
                }
                String serializedLocation = event.getBlock().getWorld().getName() + "," + event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
                System.getLogger(serializedLocation);
                signsConfig.saveResourcePack(serializedLocation, rpId);
                isLineReplaced = true;
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        String serializedLocation = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
        if (signsConfig.isResourcePackSign(serializedLocation)) {
            signsConfig.deleteResourcePackSign(serializedLocation);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.HAND && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof org.bukkit.block.Sign sign) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                String serializedLocation = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
                if (signsConfig.isResourcePackSign(serializedLocation)) {
                    for (int i = 0; i < sign.getLines().length; i++) {
                        if (sign.getLine(i).startsWith("§u§aRP-")) {
                            String rpId = sign.getLine(i).replaceAll("§u§aRP-", "").replaceAll("§u§a", "");
                            if (rpId.equalsIgnoreCase("server")) {
                                String url = resourceConfig.getServerRPurl();
                                if (url == null){
                                    event.getPlayer().sendMessage(noWorkSigns);
                                    break;
                                }
                                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                                functions.checkResourcePackStatus(player).thenAccept(success -> {
                                    if (success) {
                                        player.sendMessage(loadServRP);
                                        String name = "SERVER";
                                        playerConfig.savePlayerRP(player, name);
                                    } else {
                                        player.sendMessage(deniedServerRPset);
                                    }
                                });
                                break;
                            } else {
                                int id = Integer.parseInt(rpId);
                                if (!(resourceConfig.checkingID(id))) {
                                    player.sendMessage(inSignNoRP);
                                    return;
                                }
                                String name = resourceConfig.getNameRP(id);
                                String url = resourceConfig.getUrlRP(name);
                                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                                resourcePackStatusManager.put(player.getUniqueId(), PlayerResourcePackStatusEvent.Status.DECLINED);
                                functions.checkResourcePackStatus(player).thenAccept(success -> {
                                    if (success) {
                                        String message = messages.loadRP(name, id);
                                        player.sendMessage(message);
                                        playerConfig.savePlayerRP(player, name);
                                    } else {
                                        player.sendMessage(failLoadRP);
                                    }
                                });
                                break;
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
