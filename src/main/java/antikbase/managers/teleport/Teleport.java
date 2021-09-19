package antikbase.managers.teleport;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

public class Teleport {

    private TeleportManager manager;
    private Player player;
    private Location from;
    private Location to;

    private final int maxSeconds = 3;
    private int seconds = 0;
    private BukkitTask task;

    public Teleport(TeleportManager manager, Player player, Location from, Location to) {
        this.manager = manager;
        this.player = player;
        this.from = from;
        this.to = to;

        if(hasPermissions(player, "bypass")) {
            teleport();
            return;
        }

        this.player.sendMessage(String.format("§7Patientez %s secondes...", maxSeconds));

        task = Bukkit.getScheduler().runTaskTimer(AntikBase.getInstance(), () -> {
            if(seconds > maxSeconds) teleport();
            seconds++;
        }, 0L, 20L);
    }

    public void teleport() {
        player.sendMessage("§7Téléportation en cours...");
        if(task != null) task.cancel();

        Bukkit.getScheduler().runTaskLater(AntikBase.getInstance(), () -> {
            player.teleport(to, PlayerTeleportEvent.TeleportCause.COMMAND);
            manager.remove(this);
        }, 2L);
    }

    public void stop() {
        if(task != null) {
            task.cancel();
            player.sendMessage("§cTéléportation annulée");
        }

        manager.remove(this);
    }

    public boolean getPlayer(String name) {
        return player.getName().equals(name);
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.teleport.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.teleport." + permission);
    }
}
