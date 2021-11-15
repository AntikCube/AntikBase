package antikbase.commands;

import antikbase.AntikBase;
import antikbase.managers.teleport.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class RtpCommand implements CommandExecutor {

    private AntikBase antikBase;
    private TeleportManager teleportManager;

    public RtpCommand() {
        this.antikBase = AntikBase.getInstance();
        this.teleportManager = antikBase.getTeleportManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(!hasPermissions(player, "rtp")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        Random random = new Random();
        World world = Bukkit.getWorld("Ressources");

        int low = -9999;
        int high = 10000;
        int x = random.nextInt(high-low) + low;
        int z = random.nextInt(high-low) + low;
        int y = world.getHighestBlockYAt(x, z) + 1;

        teleportManager.teleport(player, player.getLocation(), new Location(world, x, y, z));
        return false;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.teleport.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.teleport." + permission);
    }
}
