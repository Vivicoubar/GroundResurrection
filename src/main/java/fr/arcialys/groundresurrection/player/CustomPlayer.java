package fr.arcialys.groundresurrection.player;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomPlayer {
    private Player player;
    private State state;
    private Location location;

    public CustomPlayer(Player player){
        this.player = player;
        this.state = State.PLAYING;
    }

    public Player getPlayer() {
        return player;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setBlockLocation(Location location) {
        this.location = location;
    }

    public void ressucitate() {
        Player victim = this.player;
        setState(State.PLAYING);
        victim.playNote(victim.getLocation(),Instrument.BELL,Note.natural(2, Note.Tone.A));
        victim.setHealth(10);
        Location deathLocation = victim.getLocation();
        victim.setGameMode(GameMode.SURVIVAL);
        victim.teleport(deathLocation);
        victim.setNoDamageTicks(60);
        victim.getWorld().getBlockAt(location).setType(Material.AIR);
    }

    public void kill() {
        //Kill Player and spawn items on ground
        Player victim = this.player;
        victim.playNote(victim.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Note.Tone.B));
        Bukkit.broadcastMessage("§c" + victim.getDisplayName() + " a succombé à ses blessures");
        ItemStack[] items = victim.getInventory().getContents();
        Location deathLocation = victim.getLocation();
        victim.getWorld().getBlockAt(location).setType(Material.AIR);
        for(ItemStack item :items){
            if (item != null) {
                victim.getWorld().dropItemNaturally(deathLocation, item);
            }
        }
        victim.getInventory().clear();

        //After death
        Location spawnLocation = victim.getBedSpawnLocation();
        if(spawnLocation== null){
            spawnLocation = victim.getWorld().getSpawnLocation();
        }
        victim.teleport(spawnLocation);
        victim.setGameMode(GameMode.SURVIVAL);
        victim.setHealth(victim.getMaxHealth());
        this.state = State.PLAYING;

    }

    public Location getSkullLocation() {
        return location;
    }
}
