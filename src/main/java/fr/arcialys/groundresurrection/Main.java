package fr.arcialys.groundresurrection;

import fr.arcialys.groundresurrection.commands.CommandGiveUp;
import fr.arcialys.groundresurrection.listeners.DeathAndResurectionListener;
import fr.arcialys.groundresurrection.listeners.PlayerListener;
import fr.arcialys.groundresurrection.player.DeathManager;
import fr.arcialys.groundresurrection.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private PlayerManager playerManager;
    private DeathManager deathManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        playerManager = new PlayerManager(this);
        deathManager = new DeathManager(this);
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new DeathAndResurectionListener(this), this);
        getCommand("giveup").setExecutor(new CommandGiveUp(this));



        System.out.println("Chargement termine!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Arret du plugin!");
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DeathManager getDeathManager() {
        return deathManager;
    }
}
