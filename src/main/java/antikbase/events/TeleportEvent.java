package antikbase.events;

import antikbase.AntikBase;
import antikbase.managers.teleport.Teleport;
import antikbase.managers.teleport.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvent implements Listener {

    private AntikBase antikBase;
    private TeleportManager teleportManager;

    public TeleportEvent(AntikBase antikBase) {
        this.antikBase = antikBase;
        this.teleportManager = antikBase.getTeleportManager();
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(!e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) return;
        e.setCancelled(true);

        teleportManager.teleport(e.getPlayer(), e.getFrom(), e.getTo());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if(teleportManager.get(e.getPlayer()) == null) return;

        Teleport teleport = teleportManager.get(e.getPlayer());

        if(teleport.getFrom().getBlock() != e.getPlayer().getLocation().getBlock()) {
            teleport.stop();
        }
    }
}
