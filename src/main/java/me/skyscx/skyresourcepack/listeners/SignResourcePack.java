package me.skyscx.skyresourcepack.listeners;

import me.skyscx.skyresourcepack.configs.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import me.skyscx.skyresourcepack.configs.SignsConfig;
import me.skyscx.skyresourcepack.functions.Functions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.Objects;

import static me.skyscx.skyresourcepack.Messages.*;

public class SignResourcePack implements Listener {
    private final ResourseConfig resourceConfig;
    private final SkyResourcePack plugin;
    private final Functions functions;
    private final SignsConfig signsConfig;
    public SignResourcePack(SkyResourcePack plugin, ResourseConfig resourceConfig, Functions functions, SignsConfig signsConfig) {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
        this.signsConfig = signsConfig;
        this.functions = functions;
    }
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        boolean isLineReplaced = false;
        for (int i = 0; i < event.getLines().length; i++) {
            String line = event.getLine(i);
            assert line != null;
            if (line.matches("\\[rp:\\d+\\]")) {
                if (isLineReplaced) {return;}
                String rpId = line.replaceAll("\\D", "");
                int id = Integer.parseInt(rpId);
                if (!(resourceConfig.checkingID(id))) {return;}
                event.setLine(i, "§u§aResourсePack");
                event.getBlock().setMetadata("rpId", new FixedMetadataValue(this.plugin, rpId));
                String serializedLocation = event.getBlock().getWorld().getName() + "," + event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
                signsConfig.saveResourcePack(serializedLocation, rpId);
                isLineReplaced = true;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.HAND && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof org.bukkit.block.Sign sign) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                for (int i = 0; i < sign.getLines().length; i++) {
                    if (sign.getLine(i).equalsIgnoreCase("§u§aResourсePack")) {
                        String rpId = event.getClickedBlock().getMetadata("rpId").get(0).asString();
                        int id = Integer.parseInt(rpId);
                        if (!(resourceConfig.checkingID(id))) {
                            player.sendMessage(inSignNoRP);
                            return;
                        }
                        String name = resourceConfig.getNameRP(id);
                        String url = resourceConfig.getUrlRP(name);
                        player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                        functions.checkResourcePackStatus(player, name, id);
                        break;
                    }
                }
            }
        }
    }
}
