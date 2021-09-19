package antikbase.events;

import antikbase.AntikBase;
import antikbase.sql.SpawnInterface;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    private AntikBase antikBase;
    private SpawnInterface spawnInterface;

    public DeathEvent(AntikBase antikBase) {
        this.antikBase = antikBase;
        this.spawnInterface = antikBase.getSpawnInterface();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        spawnInterface.getSpawnLocation().thenAccept(loc -> {
            Bukkit.getScheduler().runTask(antikBase, () -> {
                player.setBedSpawnLocation(loc);
                player.spigot().respawn();
            });
        });
    }
}
