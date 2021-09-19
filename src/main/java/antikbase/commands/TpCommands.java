package antikbase.commands;

import antikbase.AntikBase;
import antikbase.managers.teleportrequest.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommands implements CommandExecutor {

    private AntikBase antikBase;

    public TpCommands(AntikBase antikBase) {
        this.antikBase = antikBase;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch (command.getName()) {
            case "tpa":
                return tpa(player, command, args);
            case "tpyes":
                return tpyes(player, command, args);
        }

        return true;
    }

    private boolean tpa(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "to")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(target != null && target.isOnline())
            TeleportRequest.createTeleportRequest(player, target.getPlayer());
        else
            player.sendMessage("§cLe joueur n'est pas connecté");

        return true;
    }

    private boolean tpyes(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "accept")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        TeleportRequest.acceptTeleportRequest(player);

        return true;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.teleport.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.teleport." + permission);
    }
}
