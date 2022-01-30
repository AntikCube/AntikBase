package antikbase.events;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.InventoryHolder;

public class NPCsEvent implements Listener {

    @EventHandler
    public void onOpenTrade(InventoryOpenEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        e.setCancelled(inventory.getType() == InventoryType.MERCHANT && inventoryHolder instanceof Villager);
    }
}
