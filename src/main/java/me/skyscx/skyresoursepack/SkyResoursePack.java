package me.skyscx.skyresoursepack;

import me.skyscx.skyresoursepack.commands.ResourcePackCommandCompleter;
import me.skyscx.skyresoursepack.commands.ResourсePackCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class SkyResoursePack extends JavaPlugin {

    @Override
    public void onEnable() {
        Functions functions = new Functions();
        Messages messages = new Messages();
        ResourseConfig resourceConfig = new ResourseConfig(new File(getDataFolder(), "resource.yml"));
        Objects.requireNonNull(getCommand("resourcepack")).setExecutor(new ResourсePackCommand(this, resourceConfig, functions, messages));
        Objects.requireNonNull(getCommand("resourcepack")).setTabCompleter(new ResourcePackCommandCompleter(resourceConfig));
    }

    @Override
    public void onDisable() {
        //logic
    }
}
