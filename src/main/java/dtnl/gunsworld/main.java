package dtnl.gunsworld;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class main extends JavaPlugin implements Listener {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ShootEvent(), this);
        plugin = this;
        new FakeEquipment((Plugin)this) {
            protected boolean onEquipmentSending(FakeEquipment.EquipmentSendingEvent equipmentEvent) {
                if ((equipmentEvent.getSlot() == EnumWrappers.ItemSlot.MAINHAND) &&
                        equipmentEvent.getVisibleEntity() instanceof org.bukkit.entity.Player && equipmentEvent.getVisibleEntity().getEquipment() != null) {
                    ItemStack item = new ItemStack(Material.AIR);
                    equipmentEvent.setEquipment(item);
                    return true;
                }
                return false;
            }
        };
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
        }, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
