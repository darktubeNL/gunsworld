package dtnl.gunsworld.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class blockburn implements Listener {
    @EventHandler
    public void test(BlockBurnEvent e){
        Block block = e.getIgnitingBlock();
        if(block.getWorld().getBlockAt(block.getLocation().add(0,1,0)).getType() == Material.FIRE) {
            block.getWorld().getBlockAt(block.getLocation().add(0,1,0)).setType(Material.AIR);
        }
        e.setCancelled(true);
    }
    @EventHandler
    public void test(BlockIgniteEvent e){
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            e.setCancelled(true);
        }
    }
}
