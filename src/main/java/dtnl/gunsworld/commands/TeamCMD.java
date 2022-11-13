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
                p.sendMessage("help command!");
                return true;
            }
            //help command
            if(args[0].equalsIgnoreCase("help")){
                p.sendMessage("help command!");
                return true;
            }
            //info command
            if(args[0].equalsIgnoreCase("info")){
                String team = Teammanager.getteamofplayer(p);
                p.sendMessage("Je team:" + team);
                return true;
            }
            if (args.length > 1) {
                if(args[0].equalsIgnoreCase("create")){
                    Teammanager.Createteam(args[1]);
                    return true;
                }
                if(args[0].equalsIgnoreCase("delete")){
                    if(Teammanager.isteam(args[1])) {
                        Teammanager.Deleteteam(args[1]);
                        return true;
                    }
                }
            }


        }
        return true;
    }

}
