package dtnl.gunsworld.commands;

import dtnl.gunsworld.Managers.Teammanager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCMD implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (args.length < 1) {
                Help(p);
                return true;
            }
            //help command
            if(args[0].equalsIgnoreCase("help")){
                Help(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("info")){
                String team = Teammanager.getteamofplayer(p);
                p.sendMessage("Je team:" + team);
                return true;
            }


        }
        return true;
    }

    public void Help(Player player){
        player.sendMessage("hey!");
    }
}
