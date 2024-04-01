package me.skyscx.skyresourcepack.commands;

import me.skyscx.skyresourcepack.Functions;
import me.skyscx.skyresourcepack.Messages;
import me.skyscx.skyresourcepack.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static me.skyscx.skyresourcepack.Messages.*;

public class ResourсePackCommand implements CommandExecutor {
    private final SkyResourcePack plugin;
    private final ResourseConfig resourceConfig;
    private final Functions functions;
    private final Messages messages;

    public ResourсePackCommand(SkyResourcePack plugin, ResourseConfig resourceConfig, Functions functions, Messages messages) {
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
            if (name.equals("<name>") || url.equals("<url>") || name.equalsIgnoreCase("SERVER")){
                sender.sendMessage(cmdUpload);
                return true;
            }
            boolean validURL = functions.isUrlValid(url);
            if (!validURL){
                sender.sendMessage(invalidURL);
                return true;
            }
            String playerName;
            if (sender instanceof Player player){
                playerName = player.getName();
                int availableUpload = resourceConfig.getAvailableUpload();
                int uploadedRP = resourceConfig.getUploadedRP(player);
                System.out.println(uploadedRP + " - загружено");
                System.out.println(availableUpload + " - доступно всего");
                if (uploadedRP > availableUpload){
                    sender.sendMessage(moreUploadsError);
                    return true;
                }
            }else{
                playerName = "SERVER";
            }
            boolean containtRP = resourceConfig.getContainsResoursePack(name, sender);
            if (containtRP){return true;}
            boolean saveRP = resourceConfig.saveRP(name, url, playerName, sender);
            if (saveRP) {return true;}
            String message = messages.succefulUpload(name, resourceConfig.getIdRP(name));
            sender.sendMessage(message);
            return true;
        }
        if (args[0].equalsIgnoreCase("load")) {
            if (sender instanceof Player player){
                if (args.length < 2){
                    sender.sendMessage(cmdLoad);
                    return true;
                }
                boolean tryNum = functions.isNumeric(args[1]);
                if (!tryNum){
                    sender.sendMessage(invalidID);
                    return true;
                }
                int id = Integer.parseInt(args[1]);
                boolean containtRP = resourceConfig.checkContainsResoursePack(id, sender);
                if (containtRP){
                    System.out.println("containtRp = true");
                    return true;}
                String name = resourceConfig.getNameRP(id);
                String urlRP = resourceConfig.getUrlRP(name);
                player.setResourcePack(urlRP, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                String message = messages.loadRP(name, id);
                player.sendMessage(message);
                return true;
            }else {
                sender.sendMessage(noConsoleCMD);
            }
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                sender.sendMessage(cmdDelete);
                return true;
            }
            String name = args[1];
            int id = resourceConfig.getIdRP(name);
            boolean containtRP = resourceConfig.checkContainsResoursePack(id, sender);
            if (containtRP) {return true;}
            String owner = resourceConfig.getOwnerRP(name);
            if (sender.hasPermission("skyresourcepack.admin") || sender.isOp() || sender.getName().equalsIgnoreCase(owner)){
                boolean delRP = resourceConfig.deleteRP(name, sender);
                if (delRP) {return true;}
                String message = messages.deleteRP(name);
                sender.sendMessage(message);
            }else {
                sender.sendMessage(noAuthor);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("update")) {
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
            int id = resourceConfig.getIdRP(name);
            boolean containtRP = resourceConfig.checkContainsResoursePack(id, sender);
            if (containtRP){return true;}
            String owner = resourceConfig.getOwnerRP(name);
            if (sender.hasPermission("skyresourcepack.admin") || sender.isOp() || sender.getName().equalsIgnoreCase(owner)){
                boolean updateRP = resourceConfig.updateRP(name, url, sender);
                if (updateRP){ return true;}
                String message = messages.updatedRP(name, id);
                sender.sendMessage(message);
            } else {
                sender.sendMessage(noAuthor);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyresourcepack.admin") || !sender.isOp()) {return true;}
            resourceConfig.reloadResourceConfig();
            sender.sendMessage(reloadedCFG);
            return true;
        }
        if (args[0].equalsIgnoreCase("server")){
            if (args.length > 1 ){
                if (args[1].equalsIgnoreCase("set")){
                    if (!sender.hasPermission("skyresoursepack.admin") || !sender.isOp()){
                        sender.sendMessage(unkownCMD);
                        return true;
                    }
                    if (args.length < 3){
                        sender.sendMessage(serverRPset);
                        return true;
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
            if (sender instanceof Player player) {
                String url = resourceConfig.getServerRPurl();
                if (url == null){
                    sender.sendMessage(noServerRP);
                    return true;
                }
                player.setResourcePack(url, Objects.requireNonNull(plugin.getServer().getResourcePackHash()));
                player.sendMessage(loadServRP);
                return true;
            } else {
                sender.sendMessage(noConsoleCMD);
                return true;
            }

        }
        if (args[0].equalsIgnoreCase("disable")) {
            if (sender instanceof Player player){
                player.setResourcePack("","");
                player.sendMessage(disabledRP);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            StringBuilder message;
            if (args.length >= 2){
                if (args[1].equalsIgnoreCase("my")){
                    String playerName;
                    if (sender instanceof Player player){
                        playerName = player.getName();
                    }else{
                        playerName = "SERVER";
                    }
                    List<String> myRP = resourceConfig.getListRPowner(playerName);
                    if (myRP.isEmpty()) {
                        sender.sendMessage(emptyListRP);
                        return true;
                    }else {
                        message = new StringBuilder(myRPlist);
                        for (int i = 0; i < myRP.size(); i++) {
                            String rp = myRP.get(i);
                            message.append(rp).append(" (id: ").append(resourceConfig.getIdRP(rp)).append(")");

                            if (i < myRP.size() - 1) {
                                message.append(", ");
                            }
                        }
                        sender.sendMessage(message.toString());
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("player")){
                    if (args.length == 2){
                        sender.sendMessage(listPlayerCMD);
                        return true;
                    }
                    String playerName = args[2];
                    String name;

                    if (sender instanceof Player player){
                        name = player.getName();
                    }else{
                        name = "SERVER";
                    }
                    if (playerName.equalsIgnoreCase(name)){
                        message = new StringBuilder(myRPlist);
                    }else {
                        message = new StringBuilder(messages.rpList(playerName));
                    }
                    List<String> myRP = resourceConfig.getListRPowner(playerName);
                    if (myRP.isEmpty()) {
                        sender.sendMessage(emptyListRP);
                        return true;
                    }
                    for (int i = 0; i < myRP.size(); i++) {
                        String rp = myRP.get(i);
                        message.append(rp).append(" (id: ").append(resourceConfig.getIdRP(rp)).append(")");
                        if (i < myRP.size() - 1) {
                            message.append(", ");
                        }
                    }
                    sender.sendMessage(message.toString());
                    return true;
                }
            }
            List<String> rpList = resourceConfig.getListRP();
            message = new StringBuilder(listRP);
            if (rpList.isEmpty()) {
                sender.sendMessage(emptyListRP);
                return true;
            }
            for (int i = 0; i < rpList.size(); i++) {
                String rp = rpList.get(i);
                message.append(rp).append(" (id: ").append(resourceConfig.getIdRP(rp)).append(")");
                if (i < rpList.size() - 1) {
                    message.append(", ");
                }
            }
            sender.sendMessage(message.toString());
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(helpCMD);
            return  true;
        }
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 3){
                sender.sendMessage(cmdInfo);
                return true;
            }
            if (args[1].equalsIgnoreCase("name")){
                String name = args[2];
                int id = resourceConfig.getIdRP(name);
                boolean containtRP = resourceConfig.checkContainsResoursePack(id, sender);
                if (containtRP){return true;}
                String owner = resourceConfig.getOwnerRP(name);
                int version = resourceConfig.getVersion(name);
                sender.sendMessage(messages.info(name, id, version, owner));
                return true;
            }
            if (args[1].equalsIgnoreCase("id")){
                int id = Integer.parseInt(args[2]);
                boolean containtRP = resourceConfig.checkContainsResoursePack(id, sender);
                if (containtRP){return true;}
                String name = resourceConfig.getNameRP(id);
                String owner = resourceConfig.getOwnerRP(name);
                int version = resourceConfig.getVersion(name);
                sender.sendMessage(messages.info(name, id, version, owner));
                return true;
            }
            else {
                sender.sendMessage(cmdInfo);
                return true;
            }
        }
        sender.sendMessage(unkownCMD);
        return true;
    }
}
