package dtnl.gunsworld.events;

import me.deecaad.weaponmechanics.weapon.weaponevents.ProjectileExplodeEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class blockbreak implements Listener {
    @EventHandler
    public void test(BlockPhysicsEvent e){
        if (e.getBlock().getType() == Material.WHEAT){
            e.setCancelled(true);
        }
    }
}
