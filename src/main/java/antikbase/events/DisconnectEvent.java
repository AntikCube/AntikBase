package antikbase.events;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class DisconnectEvent implements Listener {

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        sendServer("hub", "§cDisconnected : " + e.getReason(), player);
    }

    @EventHandler
    public void onStop(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().equals("/stop") && e.getPlayer().isOp()) {
            sendServer("hub", "§cDisconnected : Server stopped", Bukkit.getOnlinePlayers().stream().toArray(Player[]::new));
        }
    }

    public void sendServer(String server, String reason, Player ...players) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
        } catch(IOException err) {
            err.printStackTrace();
        }

        for(Player player : players) {
            if(player.isOnline()) {
                player.sendPluginMessage(AntikBase.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
                if(reason.length() != 0) player.sendMessage(reason);
            }
        }

    }
}
