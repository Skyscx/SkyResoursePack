package me.skyscx.skyresoursepack;

import me.skyscx.skyresoursepack.commands.ResourсePackCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyResoursePack extends JavaPlugin {

    private ResourseConfig resourceConfig;

    @Override
    public void onEnable() {
        File resourseFile = new File(getDataFolder(), "resourсe.yml");
        resourceConfig = new ResourseConfig(resourseFile);
        resourceConfig.reloadResourceConfig();
        getCommand("resourcepack").setExecutor(new ResourсePackCommand(this));

    }

    @Override
    public void onDisable() {
        resourceConfig.saveResourceConfig();
    }
}
