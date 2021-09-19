package antikbase.events;

import antikbase.recipes.Recipes;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
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

        Player player = e.getPlayer();

        ItemStack itemStack = e.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();

        ItemStack privateChest = Recipes.getPrivateChest();

        if(!itemMeta.equals(privateChest.getItemMeta())) return;

        Chest chest = (Chest) e.getBlock().getState();
        chest.setCustomName("§7[§cPrivé§7] §7" + player.getName());
        chest.update();
    }
}
