package antikbase.managers.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportManager {

    private List<Teleport> teleportList = new ArrayList<>();

    public void teleport(Player player, Location from, Location to) {
        teleportList.add(new Teleport(this, player, from, to));
    }

    public void cancel(Player player) {
        Teleport teleport = teleportList.stream().filter(tp -> tp.getPlayer(player.getName())).findFirst().orElse(null);
        if(teleport == null) return;

        teleport.stop();
    }

    public void remove(Teleport teleport) {
        if(teleport == null) return;
        teleportList.remove(teleport);
    }

    public Teleport get(Player player) {
        return teleportList.stream().filter(tp -> tp.getPlayer(player.getName())).findFirst().orElse(null);
    }
}
