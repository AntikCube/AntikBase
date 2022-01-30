package antikbase.events;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.desktop.QuitEvent;
import java.util.HashMap;
import java.util.Map;

public class AfkEvent implements Listener {

    private AntikBase antikBase;
    private Map<Player, Long> afkMap = new HashMap<>();

    public AfkEvent() {
        this.antikBase = AntikBase.getInstance();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.antikBase, this::timeoutKick, 20 * 60 * 2, 20 * 60 * 2);
    }

    @EventHandler
    public void onPlayerAfk(PlayerMoveEvent e) {
        this.afkMap.put(e.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.afkMap.remove(e.getPlayer());
    }

    public void timeoutKick() {
        long currentTime = System.currentTimeMillis();

        this.afkMap.entrySet().stream()
                .filter(entry -> !hasPermissions(entry.getKey(), "bypass") && (currentTime - entry.getValue()) > 300000)
                .forEach(entry -> entry.getKey().kickPlayer("§cKick pour inactivité prolongée"));
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.afk.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.afk." + permission);
    }

}
