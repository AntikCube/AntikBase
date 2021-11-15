package antikbase.events;

import antikbase.AntikBase;
import antikbase.managers.teleport.Teleport;
import antikbase.managers.teleport.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.stream.Stream;

public class TeleportEvent implements Listener {

    private AntikBase antikBase;
    private TeleportManager teleportManager;

    public TeleportEvent() {
        this.antikBase = AntikBase.getInstance();
        this.teleportManager = antikBase.getTeleportManager();
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) {
            e.setCancelled(true);
            teleportManager.teleport(e.getPlayer(), e.getFrom(), e.getTo());
        } else if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
            if(e.getTo().getWorld().getName().equals("world")) {
                List<World> worldList = Bukkit.getWorlds();

                World toWorld = worldList.stream().filter(world -> world.getName().equals("Ressources")).findFirst().orElse(
                        worldList.stream().filter(world -> world.getName().equals("Construction")).findFirst().orElse(
                                Bukkit.getWorld("world")
                        )
                );

                Location to = e.getTo().clone();
                to.setWorld(toWorld);
                e.setTo(to);
            }
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(teleportManager.get(e.getPlayer()) == null) return;

        Teleport teleport = teleportManager.get(e.getPlayer());

        Location from = teleport.getFrom();
        Location loc = e.getPlayer().getLocation();
        if (from.getBlockX() != loc.getBlockX()
          || from.getBlockY() != loc.getBlockY()
          || from.getBlockZ() != loc.getBlockZ()) {
            teleport.stop();
        }
    }
}
