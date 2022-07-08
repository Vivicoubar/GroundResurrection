package fr.arcialys.groundresurrection.player;

import fr.arcialys.groundresurrection.Main;

import java.util.ArrayList;
import java.util.List;

public class DeathManager {
    private List<CustomPlayer> deathList;
    private Main main;

    public DeathManager(Main main){
        this.main = main;
        this.deathList = new ArrayList<CustomPlayer>();
    }

    public void addDeath(CustomPlayer player){
        if(!deathList.contains(player)){
            deathList.add(player);
        }
    }

    public void removeDeath(CustomPlayer player){
        deathList.remove(player);
    }

    public List<CustomPlayer> getDeathList() {
        return deathList;
    }

    public boolean isDead(Main player){
        return deathList.contains(player);
    }

}
