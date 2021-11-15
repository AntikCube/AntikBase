package antikbase.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class NPCsEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        Entity entity = e.getRightClicked();

        if(!entity.hasMetadata("NPC"))
            return;

    }
}
