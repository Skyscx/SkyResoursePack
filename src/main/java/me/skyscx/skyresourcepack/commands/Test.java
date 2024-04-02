package me.skyscx.skyresourcepack.commands;

import me.skyscx.skyresourcepack.SkyResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){

        }

        return false;
    }
    public void printResourcePackInfo(Player player) {
        String name = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getResourcePackStatus()).getDeclaringClass().getPackage().getName();
        String title = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getResourcePackStatus()).getDeclaringClass().getPackage().getImplementationTitle();
        String version = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getResourcePackStatus()).getDeclaringClass().getPackage().getSpecificationVersion();
        System.out.println("Name: " + name);
        System.out.println("title: " + title);
        System.out.println("version: " + version);
    }
}
