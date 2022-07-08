package fr.arcialys.groundresurrection.listeners;

import fr.arcialys.groundresurrection.Main;
import fr.arcialys.groundresurrection.player.CustomPlayer;
import fr.arcialys.groundresurrection.player.State;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathAndResurectionListener implements Listener {

    private final Main main;

    public DeathAndResurectionListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeathByFalling(EntityDamageEvent entityDamageEvent){
        if(entityDamageEvent.getEntity() instanceof Player){
            Player victim = (Player) entityDamageEvent.getEntity();
            if(victim.getHealth() <= entityDamageEvent.getFinalDamage()){
                entityDamageEvent.setCancelled(true);
                registerDeath(victim);
            }
        }
    }
    @EventHandler
    public void onDeathByEntity(EntityDamageByEntityEvent entityDamageEvent){
        if(entityDamageEvent.getEntity() instanceof Player){
            Player victim = (Player) entityDamageEvent.getEntity();
            if(victim.getHealth() <= entityDamageEvent.getFinalDamage()){
                entityDamageEvent.setCancelled(true);
                registerDeath(victim);
            }
        }
    }

    @EventHandler
    public void onDeathByBlock(EntityDamageByBlockEvent entityDamageEvent){
        if(entityDamageEvent.getEntity() instanceof Player){
            Player victim = (Player) entityDamageEvent.getEntity();
            if(victim.getHealth() <= entityDamageEvent.getFinalDamage()){
                entityDamageEvent.setCancelled(true);
                registerDeath(victim);
            }
        }
    }

    public void registerDeath(Player victim){
        //Entity is dead
        victim.setGameMode(GameMode.SPECTATOR);
        CustomPlayer customVictim = main.getPlayerManager().getPlayer(victim);
        main.getDeathManager().addDeath(customVictim);
        //State is necessarily Playing
        customVictim.setState(State.DEAD);
        //Spawn armorStand
        Block block = customVictim.getPlayer().getWorld().getBlockAt(customVictim.getPlayer().getLocation().add(0,1,0));
        block.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) block.getState();
        skull.setOwnerProfile(victim.getPlayerProfile());
        skull.update(true);
        customVictim.setBlockLocation(block.getLocation());
        new BukkitRunnable() {
            int timer = 30;
            @Override
            public void run() {
                if (timer > 0 && customVictim.getState()==State.DEAD) {
                    victim.sendTitle("§bVous êtes au sol!", "§bRésurrection dans §b" + timer + "s",5,10,5);
                }
                if (timer <= 0 && customVictim.getState() == State.DEAD) {
                    victim.sendTitle("§bVous êtes au sol!", "§bRésurrection...",5,10,5);
                    customVictim.kill();
                    cancel();
                }
                if (customVictim.getState() == State.PLAYING) {
                    cancel();
                }
                if(customVictim.getState() == State.DEAD) {
                    timer--;
                }
            }
        }.runTaskTimer(main, 0, 20);
            }





    @EventHandler
    public void breakHead(BlockBreakEvent breakEvent){
        if(breakEvent.getBlock().getType() == Material.PLAYER_HEAD){
            breakEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void tryRessucitate(PlayerToggleSneakEvent playerToggleSneakEvent){
        if(playerToggleSneakEvent.isSneaking()) {
            CustomPlayer saver = main.getPlayerManager().getPlayer(playerToggleSneakEvent.getPlayer());
            if (saver.getState() == State.PLAYING){
                for (CustomPlayer customPlayer : main.getDeathManager().getDeathList()) {
                    if (customPlayer.getState() == State.DEAD) {
                        if (isNear(customPlayer.getSkullLocation(), saver.getPlayer().getLocation()) && customPlayer.getState() == State.DEAD) {
                            Block saveLocation = saver.getPlayer().getLocation().getBlock();
                            customPlayer.setState(State.HEALING);
                            saver.getPlayer().playNote(saver.getPlayer().getLocation(), Instrument.BIT, Note.natural(2, Note.Tone.A));
                            customPlayer.getPlayer().playNote(saver.getPlayer().getLocation(), Instrument.BIT, Note.natural(2, Note.Tone.A));
                            saver.getPlayer().sendMessage("§eSauvetage lancé sur " + customPlayer.getPlayer().getDisplayName());
                            customPlayer.getPlayer().sendMessage("§eSauvetage lancé par " + saver.getPlayer().getDisplayName());
                            new BukkitRunnable() {
                                int timer = 5;
                                @Override
                                public void run() {
                                    if(customPlayer.getState() != State.HEALING){
                                        cancel();
                                        saver.getPlayer().sendMessage("§cLe joueur ne peut plus être sauvé....");
                                    }
                                    if (timer<0) {
                                        if (isNear(customPlayer.getSkullLocation(), saveLocation.getLocation())) {
                                            cancel();
                                            customPlayer.ressucitate();
                                            saver.getPlayer().sendMessage("§eSauvetage réussi!");
                                        }
                                        else{
                                            cancel();
                                            customPlayer.setState(State.DEAD);
                                            saver.getPlayer().sendMessage("§cVous avez bougé! Sauvetage échoué");
                                        }
                                    }
                                    if(!isNear(customPlayer.getSkullLocation(), saveLocation.getLocation())){
                                        cancel();
                                        customPlayer.setState(State.DEAD);
                                        saver.getPlayer().sendMessage("§cVous avez bougé! Sauvetage échoué");
                                    }else{
                                        customPlayer.getPlayer().sendTitle("§dSauvetage en cours...","§dRemise sur pied dans §d" + timer + "s",5,10,5);
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main,0, 20 * 5);
                            return;
                        }
                    }
                }
            }
        }
    }


    public boolean isNear(Location loc1, Location loc2){
        return loc1.distance(loc2) < 5;
    }
}
