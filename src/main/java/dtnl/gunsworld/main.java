package dtnl.gunsworld;

import com.comphenix.protocol.wrappers.EnumWrappers;
import dtnl.gunsworld.commands.TeamCMD;
import dtnl.gunsworld.events.FakeEquipment;
import dtnl.gunsworld.events.ShootEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class main extends JavaPlugin implements Listener {

    public static Plugin pl;
    public static Map<UUID, String> Games = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ShootEvent(), this);
        pl = this;

        //equip code voor crossbow
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

    public void registerCommands() {
        getCommand("team").setExecutor((CommandExecutor) new TeamCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
