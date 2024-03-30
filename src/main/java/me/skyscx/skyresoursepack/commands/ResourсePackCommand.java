package me.skyscx.skyresoursepack.commands;

import me.skyscx.skyresoursepack.Functions;
import me.skyscx.skyresoursepack.Messages;
import me.skyscx.skyresoursepack.ResourseConfig;
import me.skyscx.skyresoursepack.SkyResoursePack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static me.skyscx.skyresoursepack.Messages.*;

public class ResourсePackCommand implements CommandExecutor {
    private final SkyResoursePack plugin;
    private ResourseConfig resourceConfig;
    private Functions functions;
    private Messages messages;

    public ResourсePackCommand(SkyResoursePack plugin, ResourseConfig resourceConfig, Functions functions, Messages messages) {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
        this.functions = functions;
        this.messages = messages;
    }

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
            boolean validURL = functions.isUrlValid(url);
            if (!validURL){
                sender.sendMessage(invalidURL);
                return true;
            }
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
            String message = messages.succefulUpload(name);
            sender.sendMessage(message);
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
                String message = messages.loadRP(name);
                player.sendMessage(message);
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
            String message = messages.deleteRP(name);
            sender.sendMessage(message);
        } else if (args[0].equalsIgnoreCase("update")) {
            if (args.length < 3){
                sender.sendMessage(cmdUpdate);
                return true;
            }
            String name = args[1], url = args[2];
            boolean validURL = functions.isUrlValid(url);
            if (!validURL){
                sender.sendMessage(invalidURL);
                return true;
            }
            boolean containtRP = resourceConfig.checkContainsResoursePack(name, sender);
            if (containtRP){return true;}
            String owner = resourceConfig.getOwnerRP(name);
            if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp() || !sender.getName().equalsIgnoreCase(owner)){
                sender.sendMessage(noAuthor);
                return true;
            }
            boolean updateRP = resourceConfig.updateRP(name, url, sender);
            if (updateRP){ return true;}
            String message = messages.updatedRP(name);
            sender.sendMessage(message);
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp()) {return true;}
            resourceConfig.reloadResourceConfig();
            sender.sendMessage(reloadedCFG);
        } else if (args[0].equalsIgnoreCase("server")){
            if (args.length >= 2){
                if (args[1].equalsIgnoreCase("set")){
                    if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp()){
                        sender.sendMessage(unkownCMD);
                        return true;
                    }
                    if (args.length < 3){
                        sender.sendMessage(serverRPset + "ARGS 3 UNKOWN");
                    }
                    String url = args[2];
                    boolean validURL = functions.isUrlValid(url);
                    if (!validURL){
                        sender.sendMessage(invalidURL);
                        return true;
                    }
                    boolean setUrl = resourceConfig.setServerRPurl(url,sender);
                    if (setUrl){return true;}
                    sender.sendMessage(succServerRPset);
                    return true;
                }
            }
            if (sender instanceof Player){
                Player player = (Player) sender;
                String url = resourceConfig.getServerRPurl();
                if (url == null){
                    sender.sendMessage(noServerRP);
                    return true;
                }
                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                String name = "§6SERVER§r";
                String message = messages.loadRP(name);
                player.sendMessage(message);
            }else {
                sender.sendMessage(noConsoleCMD);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("disable")) {
            if (sender instanceof Player){
                Player player = (Player) sender;
                player.setResourcePack("");
                player.sendMessage(disabledRP);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("list")) {
            resourceConfig.getListRP(sender);
            return true;
        } else {
            sender.sendMessage(unkownCMD);
            return true;
        }
        return true;
    }
}
