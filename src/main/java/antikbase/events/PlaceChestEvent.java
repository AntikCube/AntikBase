package antikbase.events;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlaceChestEvent implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        if(!(e.getBlock().getState() instanceof Chest)) return;
        if(!e.getItemInHand().hasItemMeta()) return;

        ItemStack itemStack = e.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();



        Chest chest = (Chest) e.getBlock().getState();
        chest.setCustomName("[Privé]");
        chest.update();
    }
}
