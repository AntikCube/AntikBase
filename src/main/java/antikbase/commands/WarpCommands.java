package antikbase.commands;

import antikbase.AntikBase;
import antikbase.sql.WarpInterface;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommands implements CommandExecutor {

    private AntikBase antikBase;
    private WarpInterface warpInterface;

    public WarpCommands(AntikBase antikBase) {
        this.antikBase = antikBase;
        this.warpInterface = antikBase.getWarpInterface();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch (command.getName()) {
            case "setwarp":
                return setWarp(player, command, args);
            case "delwarp":
                return delWarp(player, command, args);
            case "warp":
                return warp(player, command, args);
            case "warps":
                return warps(player, command, args);
        }

        return true;
    }

    private boolean setWarp(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "set")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String warp = args[0].replaceAll("[^a-zA-Z0-9]", "");

        warpInterface.warpExist(warp).thenAccept(bool -> {
            if (bool) {
                warpInterface.setWarp(player, warp);
                player.sendMessage("§aWarp mis à jour avec succès");
            } else {
                warpInterface.createWarp(player, warp);
                player.sendMessage("§aWarp créé avec succès");
            }
        });

        return true;
    }

    private boolean delWarp(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "del")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String warp = args[0].replaceAll("[^a-zA-Z0-9]", "");

        warpInterface.warpExist(warp).thenAccept(bool -> {
            if (bool) {
                warpInterface.removeWarp(warp);
                player.sendMessage("§aWarp supprimé avec succès");
            } else {
                player.sendMessage("§cCe warp n'existe pas");
            }
        });

        return true;
    }

    private boolean warp(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "tp")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String warp = args[0].replaceAll("[^a-zA-Z0-9]", "");

        warpInterface.warpExist(warp).thenAccept(bool -> {
            if (bool) {
                warpInterface.getWarpLocation(warp).thenAccept(loc ->
                        Bukkit.getScheduler().runTask(antikBase, () -> player.teleport(loc)));
            } else {
                player.sendMessage("§cCe warp n'existe pas");
            }
        });

        return true;
    }

    private boolean warps(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "view")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        warpInterface.getWarpsList().thenAccept(warps -> {
            player.sendMessage("§6Liste des warps disponible : \n§7" + StringUtils.join(warps, "§8, §7"));
        });

        return true;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.warp.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.warp." + permission);
    }
}
