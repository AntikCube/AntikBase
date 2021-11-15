package antikbase.events;

import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;

public class ShulkerBoxInvEvent implements Listener {

    private Map<Player, ItemStack> maps = new HashMap<>();

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if(!hasPermissions((Player) e.getWhoClicked(), "open"))
            return;

        switch(e.getInventory().getType()) {
            case SHULKER_BOX:
                updateShulker(e);
                break;
            default:
                openShulker(e);
                break;
        }
    }

    public void openShulker(InventoryClickEvent e) {

        if(e.getAction() != InventoryAction.NOTHING)
            return;

        ItemStack itemStack = e.getCurrentItem();

        if(itemStack == null || !itemStack.hasItemMeta() || !(itemStack.getItemMeta() instanceof BlockStateMeta))
            return;

        BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();

        if(!(blockStateMeta.getBlockState() instanceof ShulkerBox))
            return;

        ShulkerBox shulker = (ShulkerBox) blockStateMeta.getBlockState();
        e.getWhoClicked().openInventory(shulker.getInventory());

        maps.put((Player) e.getWhoClicked(), itemStack);
    }

    public void updateShulker(InventoryClickEvent e) {
        if(!maps.containsKey((Player) e.getWhoClicked()))
            return;

        ItemStack itemStack = maps.get((Player) e.getWhoClicked());
        if(!(itemStack.getItemMeta() instanceof BlockStateMeta))
            return;

        BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) blockStateMeta.getBlockState();

        shulker.getInventory().setContents(e.getWhoClicked().getOpenInventory().getTopInventory().getContents());
        shulker.update();

        blockStateMeta.setBlockState(shulker);

        itemStack.setItemMeta(blockStateMeta);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(e.getInventory().getType() != InventoryType.SHULKER_BOX || !maps.containsKey((Player) e.getPlayer()))
            return;

        maps.remove((Player) e.getPlayer());
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.shulker.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.shulker." + permission);
    }

}
