package antikbase.events;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

public class PtEvent implements Listener {

    private AntikBase antikBase;

    public PtEvent(AntikBase antikBase) {
        this.antikBase = antikBase;
    }

    @EventHandler
    public void onTap(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if(!player.isOp() && !player.hasPermission("*"))
            return;

        if(!player.hasMetadata("pt_command"))
            return;

        if(e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack.getType() == Material.AIR)
            return;

        String[] data = player.getMetadata("pt_command").get(0).asString().split("::::");

        if(Material.valueOf(data[1]) == itemStack.getType()) {
            Bukkit.dispatchCommand(player, data[0]);
            e.setCancelled(true);
        }

    }
}
