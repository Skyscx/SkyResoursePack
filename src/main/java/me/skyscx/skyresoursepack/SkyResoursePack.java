package me.skyscx.skyresoursepack;

import me.skyscx.skyresoursepack.commands.ResourсePackCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyResoursePack extends JavaPlugin {
    private Functions functions;
    private Messages messages;
    @Override
    public void onEnable() {
        functions = new Functions();
        messages = new Messages();
        ResourseConfig resourceConfig = new ResourseConfig(new File(getDataFolder(), "resource.yml"));
        getCommand("resourcepack").setExecutor(new ResourсePackCommand(this, resourceConfig, functions, messages));
    }

    @Override
    public void onDisable() {
        //logic
    }
}
