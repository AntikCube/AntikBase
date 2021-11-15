package antikbase.events.chest;

import antikbase.recipes.Recipes;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

public class ChestEvents implements Listener {

    private static String privateChestTitle = "§8[§cPrivé§8] §c";

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
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!(e.getClickedBlock().getState() instanceof Chest)) return;

        Player player = e.getPlayer();

        Chest chest = (Chest) e.getClickedBlock().getState();
        String chestTitle = chest.getCustomName();

        if(chestTitle == null)
            chestTitle = player.getOpenInventory().getTitle();

        if(!chestTitle.contains(privateChestTitle)) return;

        if(chestTitle.contains(player.getName())) return;

        player.sendMessage("§cVous ne pouvez pas accéder à ce coffre");
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreakChest(BlockBreakEvent e) {
        Block block = e.getBlock();

        if(!(block.getState() instanceof Chest)) return;

        Player player = e.getPlayer();

        Chest chest = (Chest) block.getState();
        String chestTitle = chest.getCustomName();

        if(chestTitle == null)
            chestTitle = player.getOpenInventory().getTitle();

        if(!chestTitle.contains(privateChestTitle)) return;

        if(chestTitle.contains(player.getName())) return;

        player.sendMessage("§cVous ne pouvez pas casser à ce coffre");
        e.setCancelled(true);
    }

    @EventHandler
    public void onTakeChest(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Item item = e.getItem();
        ItemStack itemStack = item.getItemStack();

        if(!itemStack.hasItemMeta()) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta privateChestMeta = Recipes.getPrivateChest().getItemMeta();

        if(!itemMeta.getDisplayName().contains(privateChestTitle)) return;

        itemMeta.setDisplayName(privateChestMeta.getDisplayName());
        itemStack.setItemMeta(itemMeta);
        item.setItemStack(itemStack);
    }

    @EventHandler
    public void onTnt(BlockExplodeEvent e) {
        Iterator<Block> iterator = e.blockList().iterator();

        while(iterator.hasNext()) {
            Block block = iterator.next();
            if(!(block.getState() instanceof Chest)) continue;

            Chest chest = (Chest) block.getState();
            String chestTitle = chest.getCustomName();

            chestTitle.contains(privateChestTitle);
            e.blockList().remove(block);
        }

    }

    public static String getPrivateChestTitle() {
        return privateChestTitle;
    }
}
