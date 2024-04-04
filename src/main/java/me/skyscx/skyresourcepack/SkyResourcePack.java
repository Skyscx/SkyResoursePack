package me.skyscx.skyresourcepack;

import me.skyscx.skyresourcepack.commands.ResourcePackCommandCompleter;
import me.skyscx.skyresourcepack.commands.ResourсePackCommand;
import me.skyscx.skyresourcepack.configs.PlayerConfig;
import me.skyscx.skyresourcepack.configs.ResourseConfig;
import me.skyscx.skyresourcepack.configs.SignsConfig;
import me.skyscx.skyresourcepack.functions.Functions;
import me.skyscx.skyresourcepack.functions.ResourcePackStatusManager;
import me.skyscx.skyresourcepack.listeners.JoinPlayer;
import me.skyscx.skyresourcepack.listeners.ResourcePackStatus;
import me.skyscx.skyresourcepack.listeners.SignResourcePack;
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

public final class SkyResourcePack extends JavaPlugin {
    private ResourseConfig resourseConfig;
    private PlayerConfig playerConfig;
    private SignsConfig signsConfig;
    private ResourcePackStatusManager resourcePackStatusManager;

    public SkyResourcePack() {
    }
    @Override
    public void onEnable() {
        Messages messages = new Messages();
        ResourcePackStatusManager resourcePackStatusManager = new ResourcePackStatusManager();
        Functions functions = new Functions(resourcePackStatusManager, messages, this, playerConfig);

        File configFile = new File(getDataFolder(), "resource.yml");
        this.resourseConfig = new ResourseConfig(configFile);
        resourseConfig.setupDefaultParamets();

        File playerFile = new File(getDataFolder(), "player.yml");
        this.playerConfig = new PlayerConfig(playerFile);

        File signsFile = new File(getDataFolder(), "signs.yml");
        this.signsConfig = new SignsConfig(signsFile);

        ResourseConfig resourceConfig = new ResourseConfig(new File(getDataFolder(), "resource.yml"));
        PlayerConfig playerConfig1 = new PlayerConfig(new File(getDataFolder(), "player.yml"));
        SignsConfig signsConfig1 = new SignsConfig(new File(getDataFolder(), "signs.yml"));

        signsConfig1.signResourcePack(this);

        Objects.requireNonNull(getCommand("resourcepack")).setExecutor(new ResourсePackCommand(this, resourceConfig, functions, messages, playerConfig1, resourcePackStatusManager, signsConfig1));
        Objects.requireNonNull(getCommand("resourcepack")).setTabCompleter(new ResourcePackCommandCompleter(resourceConfig));

        JoinPlayer joinPlayer = new JoinPlayer(this, resourseConfig);
        SignResourcePack signResourcePack = new SignResourcePack(this, resourceConfig, functions, signsConfig1);
        ResourcePackStatus resourcePackStatus = new ResourcePackStatus(resourcePackStatusManager);

        getServer().getPluginManager().registerEvents(signResourcePack, this);
        getServer().getPluginManager().registerEvents(joinPlayer, this);
        getServer().getPluginManager().registerEvents(resourcePackStatus, this);


    }

    @Override
    public void onDisable() {
        //logic
    }
}
