package antikbase.events.chest;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class MinecartHopperEvent implements Listener {

    @EventHandler
    public void onInventoryPickupItem(InventoryMoveItemEvent e) {
        Inventory source = e.getSource();
        Block sourceBlock = source.getLocation().getBlock();

        Inventory destination = e.getDestination();

        if(destination.getType() == InventoryType.HOPPER && sourceBlock.getState() instanceof Chest) {
            Chest chest = (Chest) sourceBlock.getState();
            e.setCancelled(chest.getCustomName().contains(ChestEvents.getPrivateChestTitle()));
        }

    }

}
