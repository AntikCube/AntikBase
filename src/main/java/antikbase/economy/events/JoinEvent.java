package antikbase.economy.events;

import antikbase.AntikBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final Economy economy = AntikBase.getInstance().getEconomy();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(!economy.hasAccount(e.getPlayer())) {
            economy.createPlayerAccount(e.getPlayer());
        }
    }

}
