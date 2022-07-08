package fr.arcialys.groundresurrection.player;

import fr.arcialys.groundresurrection.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    private final List<CustomPlayer> playerList;
    private Main main;

    public PlayerManager(Main customMain){
        this.main = customMain;
        this.playerList = new ArrayList<CustomPlayer>();
    }

    public CustomPlayer getPlayer(Player player){
        UUID playerId = player.getUniqueId();
        for(CustomPlayer customPlayer : playerList){
            if(customPlayer.getPlayer().getUniqueId() == playerId){
                return customPlayer;
            }
        }
        CustomPlayer newPlayer = new CustomPlayer(player);
        playerList.add(newPlayer);
        return newPlayer;
    }

    public void quitPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        CustomPlayer toRemove = null;
        for(CustomPlayer customPlayer : playerList){
            if(customPlayer.getPlayer().getUniqueId() == playerId){
                toRemove = customPlayer;
            }
        }
        playerList.remove(toRemove);
    }
}
