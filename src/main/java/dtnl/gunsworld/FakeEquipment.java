package dtnl.gunsworld;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class FakeEquipment {
    public static class EquipmentSendingEvent {
        private Player client;

        private LivingEntity visibleEntity;

        private EnumWrappers.ItemSlot slot;

        private ItemStack equipment;

        private EquipmentSendingEvent(Player client, LivingEntity visibleEntity, EnumWrappers.ItemSlot slot, ItemStack equipment) {
            this.client = client;
            this.visibleEntity = visibleEntity;
            this.slot = slot;
            this.equipment = equipment;
        }

        public Player getClient() {
            return this.client;
        }

        public LivingEntity getVisibleEntity() {
            return this.visibleEntity;
        }

        public ItemStack getEquipment() {
            return this.equipment;
        }

        public void setEquipment(ItemStack equipment) {
            this.equipment = equipment;
        }

        public EnumWrappers.ItemSlot getSlot() {
            return this.slot;
        }

        public void setSlot(EnumWrappers.ItemSlot slot) {
            this.slot = (EnumWrappers.ItemSlot)Preconditions.checkNotNull(slot, "slot cannot be NULL");
        }
    }

    private Map<Object, EnumWrappers.ItemSlot> processedPackets = (new MapMaker()).weakKeys().makeMap();

    private Plugin plugin;

    private ProtocolManager manager;

    boolean main = false;

    private PacketListener listener;

    private List<Pair<EnumWrappers.ItemSlot, ItemStack>> pair;

    public FakeEquipment(Plugin plugin) {
        this.plugin = plugin;
        this.manager = ProtocolLibrary.getProtocolManager();
        this.manager.addPacketListener(this.listener = (PacketListener)new PacketAdapter(plugin, new PacketType[] { PacketType.Play.Server.ENTITY_EQUIPMENT }) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                PacketType type = event.getPacketType();
                if (packet.getEntityModifier(event).read(0) instanceof LivingEntity) {
                    LivingEntity visibleEntity = (LivingEntity)packet.getEntityModifier(event).read(0);
                    Player observingPlayer = event.getPlayer();
                    if (PacketType.Play.Server.ENTITY_EQUIPMENT.equals(type)) {
                        FakeEquipment.this.pair = (List)packet.getSlotStackPairLists().read(0);
                        EquipmentSendingEvent sendingEvent = new EquipmentSendingEvent(observingPlayer, visibleEntity, (EnumWrappers.ItemSlot)((Pair)FakeEquipment.this.pair.get(0)).getFirst(), (ItemStack)((Pair)FakeEquipment.this.pair.get(0)).getSecond());
                        EnumWrappers.ItemSlot previous = (EnumWrappers.ItemSlot)FakeEquipment.this.processedPackets.get(packet.getHandle());
                        if (FakeEquipment.this.onEquipmentSending(sendingEvent))
                            FakeEquipment.this.processedPackets.put(packet.getHandle(), (previous != null) ? previous : (EnumWrappers.ItemSlot)((Pair)FakeEquipment.this.pair.get(0)).getFirst());
                        if (((Pair)FakeEquipment.this.pair.get(0)).getSecond() != sendingEvent.getEquipment())
                            if ((sendingEvent.getVisibleEntity().getEquipment().getItemInMainHand() != null)) {
                                List<String> lores;
                                lores = Collections.singletonList("none");
                                //main hand
                                if (sendingEvent.getVisibleEntity().getEquipment().getItemInMainHand() != null) {
                                    try {
                                        lores = sendingEvent.getVisibleEntity().getEquipment().getItemInMainHand().getItemMeta().getLore();
                                        for (String s : lores) {
                                            ChatColor.stripColor(s);
                                            if (s.contains("Gun World Guns")) {
                                                main = true;
                                                break;
                                            }else{
                                                main = false;
                                            }
                                        }
                                    }catch(Exception e){
                                        main = false;
                                        lores = Collections.singletonList("none");
                                    }
                                }
                                if (lores != null) {
                                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> data = new ArrayList<>();
                                    if (main) {
                                        ItemStack item = sendingEvent.getVisibleEntity().getEquipment().getItemInMainHand();
                                        if (item.getType() == Material.FEATHER) {
                                            int modeldata;
                                            if (item.getItemMeta().hasCustomModelData()) {
                                                modeldata = item.getItemMeta().getCustomModelData();
                                            } else {
                                                modeldata = 0;
                                            }
                                            ItemStack Crossbow = new ItemStack(Material.CROSSBOW);
                                            CrossbowMeta meta = (CrossbowMeta) Crossbow.getItemMeta();
                                            meta.addChargedProjectile(new ItemStack(Material.ARROW));
                                            meta.setCustomModelData(modeldata);
                                            Crossbow.setItemMeta(meta);
                                            data.add(new Pair(EnumWrappers.ItemSlot.MAINHAND, Crossbow));
                                        } else {
                                            data.add(new Pair(EnumWrappers.ItemSlot.MAINHAND, sendingEvent.getVisibleEntity().getEquipment().getItemInMainHand()));
                                        }
                                        data.add(new Pair(EnumWrappers.ItemSlot.FEET , sendingEvent.getVisibleEntity().getEquipment().getBoots()));
                                        data.add(new Pair(EnumWrappers.ItemSlot.LEGS , sendingEvent.getVisibleEntity().getEquipment().getLeggings()));
                                        data.add(new Pair(EnumWrappers.ItemSlot.CHEST , sendingEvent.getVisibleEntity().getEquipment().getChestplate()));
                                        data.add(new Pair(EnumWrappers.ItemSlot.HEAD , sendingEvent.getVisibleEntity().getEquipment().getHelmet()));
                                        if (main)
                                            packet.getSlotStackPairLists().write(0, data);
                                    }
                                }
                            }
                    } else {
                        throw new IllegalArgumentException("Unknown packet type:" + type);
                    }
                }
            }
        });
    }

    public void close() {
        if (this.listener != null) {
            this.manager.removePacketListener(this.listener);
            this.listener = null;
        }
    }

    protected abstract boolean onEquipmentSending(EquipmentSendingEvent paramEquipmentSendingEvent);
}