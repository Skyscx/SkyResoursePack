package me.skyscx.skyresoursepack;

import me.skyscx.skyresoursepack.commands.ResourсePackCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyResoursePack extends JavaPlugin {

    private ResourseConfig resourceConfig;

    @Override
    public void onEnable() {
        ResourseConfig resourceConfig = new ResourseConfig(new File(getDataFolder(), "resource.yml"));
        getCommand("resourcepack").setExecutor(new ResourсePackCommand(this, resourceConfig));


    }

    @Override
    public void onDisable() {
        //logic
    }
}
