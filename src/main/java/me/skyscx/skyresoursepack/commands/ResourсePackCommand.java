package me.skyscx.skyresoursepack.commands;

import me.skyscx.skyresoursepack.Messages;
import me.skyscx.skyresoursepack.ResourseConfig;
import me.skyscx.skyresoursepack.SkyResoursePack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static me.skyscx.skyresoursepack.Messages.*;

public class ResoursePackCommand implements CommandExecutor {
    private ResourseConfig resourceConfig;
    private SkyResoursePack plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1){
            sender.sendMessage(usageCMD);
            return true;
        }
        if (args[0].equalsIgnoreCase("upload")){
            if (args.length < 3){
                sender.sendMessage(cmdUpload);
                return true;
            }
            String name = args[1], url = args[2];
            String playerName = null;
            if (sender instanceof Player){
                Player player = (Player) sender;
                playerName = player.getName();
            }else{
                playerName = "SERVER";
            }
            boolean containtRP = resourceConfig.getContainsResoursePack(name, sender);
            if (containtRP){return true;}
            boolean saveRP = resourceConfig.saveRP(name, url, playerName, sender);
            if (saveRP) {return true;}
            sender.sendMessage(succefulUpload);
        } else if (args[0].equalsIgnoreCase("load")) {
            if (sender instanceof Player){
                if (args.length < 2){
                    sender.sendMessage(cmdLoad);
                    return true;
                }
                String name = args[1];
                Player player = (Player) sender;
                boolean containtRP = resourceConfig.checkContainsResoursePack(name, sender);
                if (containtRP){return true;}
                String urlRP = resourceConfig.getUrlRP(name);
                player.setResourcePack(urlRP, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                player.sendMessage(loadRP);
            }else {
                sender.sendMessage(noConsoleCMD);
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                sender.sendMessage(cmdDelete);
                return true;
            }
            String name = args[1];
            boolean containtRP = resourceConfig.checkContainsResoursePack(name, sender);
            if (containtRP) {return true;}
            String owner = resourceConfig.getOwnerRP(name);
            if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp() || !sender.getName().equalsIgnoreCase(owner)) {
                sender.sendMessage(noAuthor);
                return true;
            }
            boolean delRP = resourceConfig.deleteRP(name, sender);
            if (delRP) {
                return true;
            }
            sender.sendMessage(deleteRP);
        } else if (args[0].equalsIgnoreCase("update")) {
            if (args.length < 3){
                sender.sendMessage(cmdUpdate);
                return true;
            }
            String name = args[1], url = args[2];
            boolean containtRP = resourceConfig.checkContainsResoursePack(name, sender);
            if (containtRP){return true;}
            String owner = resourceConfig.getOwnerRP(name);
            if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp() || !sender.getName().equalsIgnoreCase(owner)){
                sender.sendMessage(noAuthor);
                return true;
            }
            String playerName = null;
            if (sender instanceof Player){
                Player player = (Player) sender;
                playerName = player.getName();
            }else{
                playerName = "SERVER";
            }
            boolean updateRP = resourceConfig.updateRP(name, url, sender);
            if (updateRP){ return true;}
            sender.sendMessage(updatedRP);
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp()) {return true;}
            resourceConfig.reloadResourceConfig();
            sender.sendMessage(reloadedCFG);
        } else if (args[0].equalsIgnoreCase("server")){
            if (sender instanceof Player){
                Player player = (Player) sender;
                String url = resourceConfig.getServerRPurl();
                if (url == null){
                    sender.sendMessage(noServerRP);
                    return true;
                }
                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                player.sendMessage(loadRP);
            }else {
                sender.sendMessage(noConsoleCMD);
                return true;
            }
        } else {
            sender.sendMessage(unkownCMD);
        }
        return true;
    }

}
