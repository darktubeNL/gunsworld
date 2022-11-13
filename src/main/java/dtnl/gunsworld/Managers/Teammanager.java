package dtnl.gunsworld.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Teammanager {
    //maak / laad team yml file
    public static YamlFile getteamfile(String name){
        return new YamlFile(Bukkit.getServer().getPluginManager().getPlugin("gunsworld").getDataFolder() +"/teams/"+name+".yml");
    }

    public static void Createteam(String team){
        YamlFile yml = getteamfile(team);
        try {
            if(!yml.exists()){
                yml.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage("Created team: "+ team);
            }else{
                Bukkit.getServer().getConsoleSender().sendMessage(team +" already is a team");
            }
            yml.load();

        } catch (Exception ignored) {}
        List<String> leaders = new ArrayList<String>();
        yml.set("team.leaders",leaders);
        List<String> members = new ArrayList<String>();
        yml.set("team.members", members);
        try {
            yml.save();
        } catch (Exception ignored) {}

    }

    public static void Deleteteam(String team) {
        YamlFile yml = getteamfile(team);
        try {
            if(yml.exists()) {
                List<String> members = getallmembers(team);
                for (String member : members) {
                    Bukkit.getPlayer(member).sendMessage("message.kicked");
                }
               yml.deleteFile();
            }
        } catch (Exception ignored) {}
    }

    public static List<String> getallmembers(String name){
        YamlFile yml = getteamfile(name);
        try {
            if (yml.exists()) {
                yml.load();
                if(yml.isSet("team.leaders") && yml.isSet("team.members")) {
                    List<String> leaders = (List<String>) yml.get("team.leaders");
                    List<String> members = (List<String>) yml.get("team.members");
                    List<String> returnlist = new ArrayList<String>();;
                    returnlist.addAll(leaders);
                    returnlist.addAll(members);
                    return returnlist;
                }
            }
        } catch (Exception ignored) {}
        return new ArrayList();
    }
    public static List<String> getmembers(String name){
        YamlFile yml = getteamfile(name);
        try {
            if (yml.exists()) {
                yml.load();
                if(yml.isSet("team.members") && yml.isSet("team.members")) {
                    List<String> members = (List<String>) yml.get("team.members");
                    return members;
                }
            }
        } catch (Exception ignored) {}
        return new ArrayList();
    }

    public static Boolean isteam(String name) {
        YamlFile yml = getteamfile(name);
        try {
            if (yml.exists()) {
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }
    public static List<String> getleaders(String name){
        YamlFile yml = getteamfile(name);
        try {
            if (yml.exists()) {
                yml.load();
                if(yml.isSet("team.leaders") && yml.isSet("team.leaders")) {
                    List<String> leaders = (List<String>) yml.get("team.leaders");
                    return leaders;
                }
            }
        } catch (Exception ignored) {}
        return new ArrayList();
    }

    public static void addmember(String team, UUID player){
        List<String> players = getmembers(team);
        if(!players.contains(player.toString())) {
            players.add(player.toString());
            YamlFile yml = getteamfile(team);
            try {
                if (yml.exists()) {
                    yml.load();
                    if (yml.isSet("team.members")) {
                        yml.set("team.members", players);
                    }
                }
            } catch (Exception ignored) {
            }
            try {
                yml.save();
            } catch (Exception ignored) {
            }
        }
    }

    public static List<String> getteams(){
        File dataFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("gunsworld").getDataFolder()+"/teams");
        File[] files = dataFolder.listFiles();
        List<String> teamlist = new ArrayList<String>();
        Arrays.sort(files);
        for (File teams : files) {
            String teamyml = teams.getName();
            String teamnaam = teamyml.substring(0, (teamyml.length() - 4));
            teamlist.add(teamnaam);
        }
        return teamlist;
    }

    public static String getteamofplayer(Player player){
        UUID playeruuid = player.getUniqueId();
        List<String> list = getteams();
        for (String teams : list) {
            if(getallmembers(teams).contains(playeruuid.toString())){
               return teams;
            }
        }
        return "none";
    }
    public static void addleader(String team, UUID player){
        List<String> players = getleaders(team);
        if(!players.contains(player.toString())) {
            players.add(player.toString());
            YamlFile yml = getteamfile(team);
            try {
                if (yml.exists()) {
                    yml.load();
                    if (yml.isSet("team.leader")) {
                        yml.set("team.leader", players);
                    }
                }
            } catch (Exception ignored) {}
            try {
                yml.save();
            } catch (Exception ignored) {}
        }
    }
}
