package fr.arcialys.groundresurrection.listeners;

import fr.arcialys.groundresurrection.Main;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private Main main;

    public PlayerListener(Main customMain){
        this.main = customMain;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        joinEvent.setJoinMessage("§bBienvenue à §e" + joinEvent.getPlayer().getDisplayName());
        main.getPlayerManager().getPlayer(joinEvent.getPlayer());
        if(joinEvent.getPlayer().getGameMode() == GameMode.SPECTATOR){
            joinEvent.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent){
        quitEvent.setQuitMessage("§bAu revoir §e" + quitEvent.getPlayer().getDisplayName());
        main.getPlayerManager().quitPlayer(quitEvent.getPlayer());
    }
}
