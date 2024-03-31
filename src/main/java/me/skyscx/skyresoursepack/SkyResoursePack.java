package me.skyscx.skyresoursepack;

import me.skyscx.skyresoursepack.commands.ResourcePackCommandCompleter;
import me.skyscx.skyresoursepack.commands.ResourсePackCommand;
import me.skyscx.skyresoursepack.listeners.JoinPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

// =============================================================================
// SkyResourcePack
// =============================================================================
// [RU] Плагин разработан для игрового сервера майнкрафт - ZenPower RP.
// [EN] Plugin developed for the Minecraft game server - ZenPower RP.
// -----------------------------------------------------------------------------
// Author:
// - Skyscx (https://github.com/Skyscx)
// -----------------------------------------------------------------------------

public final class SkyResoursePack extends JavaPlugin {
    private ResourseConfig resourseConfig;
    public SkyResoursePack() {
    }
    @Override
    public void onEnable() {
        Functions functions = new Functions();
        Messages messages = new Messages();
        File configFile = new File(getDataFolder(), "resource.yml");
        this.resourseConfig = new ResourseConfig(configFile);
        resourseConfig.setupDefaultParamets();
        ResourseConfig resourceConfig = new ResourseConfig(new File(getDataFolder(), "resource.yml"));
        Objects.requireNonNull(getCommand("resourcepack")).setExecutor(new ResourсePackCommand(this, resourceConfig, functions, messages));
        Objects.requireNonNull(getCommand("resourcepack")).setTabCompleter(new ResourcePackCommandCompleter(resourceConfig));
        JoinPlayer joinPlayer = new JoinPlayer(this, resourseConfig);
        getServer().getPluginManager().registerEvents(joinPlayer, this);


    }

    @Override
    public void onDisable() {
        //logic
    }
}
