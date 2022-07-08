package fr.arcialys.groundresurrection.commands;

import fr.arcialys.groundresurrection.Main;
import fr.arcialys.groundresurrection.player.CustomPlayer;
import fr.arcialys.groundresurrection.player.State;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveUp implements CommandExecutor {

    private Main main;

    public CommandGiveUp(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && command.getName().equalsIgnoreCase("giveup")){
            CustomPlayer player = main.getPlayerManager().getPlayer((Player) sender);
            if(player.getState() == State.DEAD){
                player.kill();
            }else{
                player.getPlayer().sendMessage("Vous n'êtes pas au sol!");
            }
            return true;
        }else{
            sender.sendMessage("Only Player should use that!");
            return true;
        }
    }
}
