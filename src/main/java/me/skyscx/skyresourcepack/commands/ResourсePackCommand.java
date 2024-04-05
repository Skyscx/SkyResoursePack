package me.skyscx.skyresourcepack.commands;

import me.skyscx.skyresourcepack.configs.SignsConfig;
import me.skyscx.skyresourcepack.functions.Functions;
import me.skyscx.skyresourcepack.Messages;
import me.skyscx.skyresourcepack.configs.PlayerConfig;
import me.skyscx.skyresourcepack.configs.ResourseConfig;
import me.skyscx.skyresourcepack.SkyResourcePack;
import me.skyscx.skyresourcepack.functions.ResourcePackStatusManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.skyscx.skyresourcepack.Messages.*;

public class ResourсePackCommand implements CommandExecutor {
    private final ResourcePackStatusManager resourcePackStatusManager;
    private final SkyResourcePack plugin;
    private final ResourseConfig resourceConfig;
    private final Functions functions;
    private final Messages messages;
    private final PlayerConfig playerConfig;
    private final SignsConfig signsConfig;
    public ResourсePackCommand(
            SkyResourcePack plugin,
            ResourseConfig resourceConfig,
            Functions functions,
            Messages messages,
            PlayerConfig playerConfig,
            ResourcePackStatusManager resourcePackStatusManager,
            SignsConfig signsConfig)
    {
        this.plugin = plugin;
        this.resourceConfig = resourceConfig;
        this.functions = functions;
        this.messages = messages;
        this.playerConfig = playerConfig;
        this.resourcePackStatusManager = resourcePackStatusManager;
        this.signsConfig = signsConfig;
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

            AtomicBoolean validURL = new AtomicBoolean(true);
            CompletableFuture<Boolean> future = functions.checkUrlAsync(url);
            future.thenAccept(result -> {
                if (result) {
                    sender.sendMessage(invalidURL);
                    validURL.set(false);
                }
            });
            future.join();
            if (!validURL.get()) {
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
        } // TODO: Добавить описание для пакетов ресурсов.
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
                return true;
            }else {
                sender.sendMessage(noConsoleCMD);
            }
        } // Вроде усе, добавил сохранение в players.yml
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
                playerConfig.delRP(name);
                String message = messages.deleteRP(name);
                sender.sendMessage(message);
            }else {
                sender.sendMessage(noAuthor);
            }
            return true;
        } // Добавил фишку, если рп удален, то он удаляется с конфигурации у игроков юзающих этот рп. и будет серверный
        if (args[0].equalsIgnoreCase("update")) {
            if (args.length < 3){
                sender.sendMessage(cmdUpdate);
                return true;
            }
            String name = args[1], url = args[2];
            AtomicBoolean validURL = new AtomicBoolean(true);
            CompletableFuture<Boolean> future = functions.checkUrlAsync(url);
            future.thenAccept(result -> {
                if (result) {
                    sender.sendMessage(invalidURL);
                    validURL.set(false);
                }
            });
            future.join();
            if (!validURL.get()) {
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
        } // Тут нафиг ничо не надо трогать как по мне
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("skyresourcepack.admin") || !sender.isOp()) {return true;}
            resourceConfig.reloadResourceConfig();
            playerConfig.reloadPlayersConfig();
            signsConfig.reloadSignsConfig();
            sender.sendMessage(reloadedCFG);
            return true;
        } // Добавил перезагрузку новых двух конфигураций.
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
                    AtomicBoolean validURL = new AtomicBoolean(true);
                    CompletableFuture<Boolean> future = functions.checkUrlAsync(url);
                    future.thenAccept(result -> {
                        if (result) {
                            sender.sendMessage(invalidURL);
                            validURL.set(false);
                        }
                    });
                    future.join();
                    if (!validURL.get()) {
                        sender.sendMessage(invalidURL);
                        return true;
                    }
                    boolean setUrl = resourceConfig.setServerRPurl(url,sender);
                    if (setUrl){return true;}
                    sender.sendMessage(succServerRPset);
                    return true;
                }
            } // server set
            if (sender instanceof Player player) {
                String url = resourceConfig.getServerRPurl();
                if (url == null){
                    sender.sendMessage(noServerRP);
                    return true;
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
                return true;
            } else {
                sender.sendMessage(noConsoleCMD);
                return true;
            }

        } // Вроде бы должно работать, сохраняется запись в players.yml (server rp)
        if (args[0].equalsIgnoreCase("disable")) {
            if (sender instanceof Player player){
                player.setResourcePack("","");
                functions.checkResourcePackStatus(player).thenAccept(success -> {
                    if (success) {
                        player.sendMessage(disabledRP);
                        playerConfig.delPlayerRP(player);
                    } else {
                        player.sendMessage(noInstallRP);
                    }
                });
                return true;
            } else {
                sender.sendMessage(noConsoleCMD);
            }
            return true;
        } // Вроде бы должно работать, удаляется запись с players.yml
                                                                    // То есть при автозагрузке ничо не загрузит или рп сервера
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
        } // Оптимизировал сообщения
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
            // TODO: Добавить функцию использования команды без name & id. Чтобы выводилась информация о загруженном пакете ресурсов.
            else {
                sender.sendMessage(cmdInfo);
                return true;
            }
        }
        // TODO: Добавить функцию использования команды без name & id. Чтобы выводилась информация о загруженном пакете ресурсов. (в INFO)
        // TODO: Добавить для админов команду /rp server auto (true/false)
        if (args[0].equalsIgnoreCase("toggle")){
            if (sender instanceof Player player){
                if (playerConfig.getAutoRpPlayer(player)){
                    playerConfig.toggleAutoRpPlayer(player, false);
                    sender.sendMessage(toggleAutoRpPlayerFalse);
                    return true;
                } else {
                    playerConfig.toggleAutoRpPlayer(player, true);
                    sender.sendMessage(toggleAutoRpPlayerTrue);
                    return true;
                }
            }
            return true;
        }
        sender.sendMessage(unkownCMD);
        return true;
    }
}
