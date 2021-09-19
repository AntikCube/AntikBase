package antikbase.managers.teleportrequest;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.UUID;

public class TeleportRequest {

    private static final int maxSeconds = 10;

    public static void createTeleportRequest(Player player, Player target) {
        target.setMetadata("teleport", new FixedMetadataValue(AntikBase.getInstance(), player.getUniqueId().toString()));

        player.sendMessage(String.format("§7Requête de téléportation envoyé à %s.", target.getName()));
        target.sendMessage(String.format("§7Une demande de téléportation de la part de %s vous a été envoyée. Vous avez %d secondes pour accepter.", player.getName(), maxSeconds));

        Bukkit.getScheduler().runTaskLater(AntikBase.getInstance(), () -> {
            if(hasMetadata(player, target) && player.isOnline() && target.isOnline()) {
                target.removeMetadata("teleport", AntikBase.getInstance());
                target.sendMessage(String.format("§7Demande de téléportation de %s expirée.", player.getName()));
                player.sendMessage(String.format("§7Votre demande de téléportation à %s a expiré.", target.getName()));
            }
        }, 20L*maxSeconds);
    }

    public static void acceptTeleportRequest(Player target) {
        MetadataValue playerUuid = target.getMetadata("teleport").stream().findFirst().orElse(null);

        if(playerUuid == null || !target.hasMetadata("teleport") || target.getMetadata("teleport").size() == 0) {
            target.sendMessage("§cVous n'avez aucune demande de téléportation en attente.");
            return;
        }

        if(Bukkit.getOfflinePlayer(UUID.fromString(playerUuid.asString())).isOnline()) {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUuid.asString()));
            player.teleport(target);
        } else {
            target.sendMessage("§cLe joueur est déconnecté.");
        }

        target.removeMetadata("teleport", AntikBase.getInstance());
    }

    private static boolean hasMetadata(Player player, Player target) {
        return target.hasMetadata("teleport") && target.getMetadata("teleport").stream()
                .anyMatch(value -> value.asString().equals(player.getUniqueId().toString()));
    }

}
