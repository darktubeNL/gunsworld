package dtnl.gunsworld.events;

import dtnl.gunsworld.main;
import me.deecaad.weaponmechanics.weapon.projectile.weaponprojectile.WeaponProjectile;
import me.deecaad.weaponmechanics.weapon.weaponevents.ProjectileExplodeEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponShootEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Smoke implements Listener {
    public static Map<Location, Integer> seconds = new HashMap<>();

    @EventHandler
    public void test(ProjectileExplodeEvent e){
        WeaponProjectile name = e.getProjectile();
        if(name.getWeaponTitle().equalsIgnoreCase("Smoke")) {
            Location location = e.getLocation();
            if(location.getWorld() != null) {
                ArrayList<Block> outside = getBlocksAroundCenter(location, 6);
                ArrayList<Block> inside = getBlocksAroundCenter(location, 4);
                seconds.put(location, 0);
                for (Block b : outside) {
                    if (b.getType() == Material.AIR) {
                        double d = Math.random();
                        if (d < 0.2) {
                            b.setType(Material.WHEAT);
                        }
                    }
                }
                for (Block b : inside) {
                    if (b.getType() == Material.AIR) {
                        b.setType(Material.WHEAT);
                    }
                }
                ArrayList<Block> smokeblocks = getBlocksAroundCenter(location, 6);
                smokeblocks.removeIf(b -> b.getType() == Material.AIR);

                e.setCancelled(true);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (seconds.get(location) != null) {
                            for (Block b : smokeblocks) {
                                if (b.getType() == Material.AIR) {
                                    b.setType(Material.WHEAT);
                                }
                            }
                            seconds.put(location, seconds.get(location) + 1);
                            ArrayList<Block> check = getBlocksAroundCenter(location, 6);
                            for (Block b : check) {
                                if (b.getType() == Material.WHEAT) {
                                    Ageable ageable = (Ageable) b.getBlockData();
                                    if (ageable.getAge() < 7) {
                                        ageable.setAge(ageable.getAge() + 1);
                                        b.setBlockData(ageable);
                                    } else {
                                        ageable.setAge(0);
                                        b.setBlockData(ageable);
                                    }
                                }
                            }
                            if (seconds.get(location) > 40) {
                                this.cancel();
                                for (Block b : check) {
                                    if (b.getType() == Material.WHEAT) {
                                        b.setType(Material.AIR);
                                    }
                                }
                                seconds.remove(location);
                            }
                        }
                    }
                }.runTaskTimer(main.pl, 0, 5);
            }
        }
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();

        for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
            for (int y = (loc.getBlockY()-radius); y <= (loc.getBlockY()+radius); y++) {
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
}
