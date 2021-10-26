package antikbase.commands;

import antikbase.AntikBase;
import antikbase.sql.SpawnInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommands implements CommandExecutor {

    private AntikBase antikBase;
    private SpawnInterface spawnInterface;

    public SpawnCommands(AntikBase antikBase) {
        this.antikBase = antikBase;
        this.spawnInterface = antikBase.getSpawnInterface();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch (command.getName()) {
            case "setspawn":
                return setSpawn(player, command, args);
            case "spawn":
                return spawn(player, command, args);
        }

        return false;
    }

    private boolean setSpawn(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "set")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(String.format("§cFaites : /%s", command.getName()));
            return true;
        }

        spawnInterface.spawnExist().thenAccept(bool -> {
            if(bool) {
                spawnInterface.setSpawn(player.getLocation());
                player.sendMessage("§aSpawn mis à jour avec succès");
            } else {
                spawnInterface.createSpawn(player.getLocation());
                player.sendMessage("§aSpawn créé avec succès");
            }
        });

        return true;
    }

    private boolean spawn(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "tp")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(String.format("§cFaites : /%s", command.getName()));
            return true;
        }

        spawnInterface.spawnExist().thenAccept(bool -> {
            if(bool) {
                spawnInterface.getSpawnLocation().thenAccept(loc -> Bukkit.getScheduler().runTask(antikBase, () -> player.teleport(loc)));
            } else {
                player.sendMessage("§cLe spawn n'a pas été définis. Contactez un administrateur.");
            }
        });

        return true;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.spawn.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.spawn." + permission);
    }
}
