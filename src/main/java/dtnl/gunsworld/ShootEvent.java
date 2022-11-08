package dtnl.gunsworld;

import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.beykerykt.minecraft.lightapi.common.LightAPI;

public class ShootEvent implements Listener {
    @EventHandler
    public void test(WeaponShootEvent e){
        try{
            Bukkit.broadcast("test","ikben.staf");
            if (Bukkit.getPluginManager().getPlugin("LightAPI") != null) {
                Location loc = e.getShooter().getLocation();
                LightAPI.get().setLightLevel(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 15);
                (new BukkitRunnable() {
                    public void run() {
                        LightAPI.get().setLightLevel(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0);
                    }
                }).runTaskLater(main.plugin, 3L);
            }
        } catch (Error|Exception error) {}
    }

}
