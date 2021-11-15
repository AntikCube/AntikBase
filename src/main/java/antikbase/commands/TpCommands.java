package antikbase.commands;

import antikbase.AntikBase;
import antikbase.managers.teleportrequest.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommands implements CommandExecutor {

    private AntikBase antikBase = AntikBase.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch(command.getName()) {
            case "tpa":
                return tpa(player, command, args);
            case "tpyes":
                return tpyes(player, command, args);
            case "tp":
                tp(player, command, args);
                break;
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

    private boolean tp(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "tp")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }


        Player target;
        double x;
        double y;
        double z;
        Location to;
        switch(args.length) {
            case 1:
                try {
                    target = Bukkit.getPlayer(args[0]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }
                player.teleport(target.getLocation());
                break;
            case 2:
                Player player1;
                Player player2;

                try {
                    player1 = Bukkit.getPlayer(args[0]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }

                try {
                    player2 = Bukkit.getPlayer(args[1]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }

                player1.teleport(player2.getLocation());
                break;
            case 3:
                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }

                to = new Location(player.getWorld(), x, y, z);
                player.teleport(to);
                break;
            case 4:
                try {
                    target = Bukkit.getPlayer(args[0]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }

                try {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                } catch(Exception e) {
                    player.sendMessage("§cCe joueur n'existe pas");
                    break;
                }

                to = new Location(target.getWorld(), x, y, z);
                target.teleport(to);
                break;
            default:
                player.sendMessage("§cFaites : \n" +
                        "/" + command.getName() + " [pseudo] <x> <y> <z> \n" +
                        "/" + command.getName() + " <from> <to> \n" +
                        "/" + command.getName() + " <to> \n");
                break;
        }

        return true;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.teleport.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.teleport." + permission);
    }
}
