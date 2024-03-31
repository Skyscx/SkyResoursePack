package me.skyscx.skyresoursepack.commands;

import me.skyscx.skyresoursepack.ResourseConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                        "reload"
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
                        "delete"
                );
            }
        } if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load")) {
                return resourceConfig.getListRP().stream()
                        .filter(rp -> rp.startsWith(args[1]))
                        .collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("server")) {
                if (sender.hasPermission("skyresourcepack.admin") || sender.isOp()) {
                    return List.of("set");
                } else return null;
            } else if (args[0].equalsIgnoreCase("update")) {
                if (sender instanceof Player player) {
                    String playerName = player.getName();
                    return resourceConfig.getListRPowner(playerName);
                }
            } else if (args[0].equalsIgnoreCase("upload")) {
                return List.of("<name>");
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (sender instanceof Player player) {
                    String playerName = player.getName();
                    return resourceConfig.getListRPowner(playerName);
                }
            }
            return Collections.emptyList();
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("upload")) {
                return List.of("<url>");
            } else if (args[0].equalsIgnoreCase("update")) {
                return List.of("<url>");
            } else if (args[0].equalsIgnoreCase("set")) {
                return List.of("<url>");
            }
            return Collections.emptyList();
        }
        return null;
    }
}
