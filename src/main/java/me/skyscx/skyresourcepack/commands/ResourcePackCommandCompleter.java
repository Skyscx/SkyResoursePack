package me.skyscx.skyresourcepack.commands;

import me.skyscx.skyresourcepack.configs.ResourseConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourcePackCommandCompleter implements TabCompleter {
    private final ResourseConfig resourceConfig;
    public ResourcePackCommandCompleter(ResourseConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            if (sender.hasPermission("skyresourcepack.admin") || sender.isOp()){
                return List.of(
                        "help",
                        "server",
                        "list",
                        "load",
                        "disable",
                        "upload",
                        "update",
                        "delete",
                        "reload",
                        "info",
                        "toggle"
                );
            } else {
                return List.of(
                        "help",
                        "server",
                        "list",
                        "load",
                        "disable",
                        "upload",
                        "update",
                        "delete",
                        "info",
                        "toggle"
                );
            }
        } if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load")) {
                return List.of("<id>");
            }
            if (args[0].equalsIgnoreCase("server")) {
                if (sender.hasPermission("skyresourcepack.admin") || sender.isOp()) {
                    return List.of("set");
                } else return null;
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (sender instanceof Player player) {
                    String playerName = player.getName();
                    return resourceConfig.getListRPowner(playerName);
                }
            }
            if (args[0].equalsIgnoreCase("upload")) {
                return List.of("<name>");
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (sender instanceof Player player) {
                    String playerName = player.getName();
                    return resourceConfig.getListRPowner(playerName);
                }
            }
            if (args[0].equalsIgnoreCase("info")) {
                return List.of("name", "id");
            }
            if (args[0].equalsIgnoreCase("list")) {
                return List.of("player", "my");
            }
            return Collections.emptyList();
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("upload")) {
                return List.of("<url>");
            }
            if (args[0].equalsIgnoreCase("update")) {
                return List.of("<url>");
            }
            if (args[0].equalsIgnoreCase("set")) {
                return List.of("<url>");
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args[1].equalsIgnoreCase("name")){
                    return  resourceConfig.getAllRP();
                }
                if (args[1].equalsIgnoreCase("id")){
                    return List.of("<id>");
                }
            }
            if (args[0].equalsIgnoreCase("list")){
                if (args[1].equalsIgnoreCase("player")){
                    List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
                    List<String> playerNames = new ArrayList<>();
                    for (Player player : players) {
                        playerNames.add(player.getName());
                    }
                    return playerNames;
                }
            }
            return Collections.emptyList();
        }
        return null;
    }
}
