package antikbase.events.chest;

import antikbase.recipes.Recipes;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestEvents implements Listener {

    private String privateChestTitle = "§7[§cPrivé§7] §7";

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
        chest.setCustomName(privateChestTitle + player.getName());
        chest.update();
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(!(e.getClickedBlock().getState() instanceof Chest)) return;

        Player player = e.getPlayer();

        Chest chest = (Chest) e.getClickedBlock().getState();
        String chestTitle = chest.getCustomName();

        if(!chestTitle.contains(privateChestTitle)) return;

        e.setCancelled(!chestTitle.contains(player.getName()));

    }
}
