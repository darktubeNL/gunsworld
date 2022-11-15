package dtnl.gunsworld.events;

import dtnl.gunsworld.main;
import me.deecaad.weaponmechanics.weapon.projectile.weaponprojectile.WeaponProjectile;
import me.deecaad.weaponmechanics.weapon.weaponevents.ProjectileExplodeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class molotov implements Listener {
    public static Map<Location, Integer> seconds = new HashMap<>();

    @EventHandler
    public void test(ProjectileExplodeEvent e){
        WeaponProjectile name = e.getProjectile();
        if(name.getWeaponTitle().equalsIgnoreCase("Molotov")) {
            Location location = e.getLocation();
            if(location.getWorld() != null) {
                ArrayList<Block> blocks = getBlocksAroundCenter(location, 4);
                for (Block b : blocks) {
                    if(location.getWorld().getBlockAt(b.getLocation().add(0,1,0)).getType() == Material.AIR) {
                        double d = Math.random();
                        if (d < 0.6) {
                            Location firelocation = b.getRelative(BlockFace.UP).getLocation();
                            HashSet<Location> points = getLineBetweenPoints(location, firelocation, 6);
                            boolean place = true;
                            for(Location l : points){
                                if(l.getWorld().getBlockAt(l).getType() != Material.AIR){
                                    if(l.getWorld().getBlockAt(l).getType() != Material.FIRE) {
                                        place = false;
                                    }
                                }
                            }
                            if(place) {
                                b.getRelative(BlockFace.UP).setType(Material.FIRE);
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    }

                                }.runTaskLater(main.pl, 300);
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();

        for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
            for (int y = (loc.getBlockY()-2); y <= (loc.getBlockY()+2); y++) {
                for (int z = (loc.getBlockZ()-radius); z <= (loc.getBlockZ()+radius); z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    public static HashSet<Location> getLineBetweenPoints(Location point1, Location point2, int pointsInLine){
        double p1X = point1.getX();
        double p1Y = point1.getY();
        double p1Z = point1.getZ();
        double p2X = point2.getX();
        double p2Y = point2.getY();
        double p2Z = point2.getZ();

        double lineAveX = (p2X-p1X)/pointsInLine;
        double lineAveY = (p2Y-p1Y)/pointsInLine;
        double lineAveZ = (p2Z-p1Z)/pointsInLine;

        World world = point1.getWorld();
        HashSet<Location> line = new HashSet<>();
        for(int i = 0; i <= pointsInLine; i++){
            Location loc = new Location(world, p1X + lineAveX * i, p1Y + lineAveY * i, p1Z + lineAveZ * i);
            line.add(loc);
        }
        return line;
    }
}
