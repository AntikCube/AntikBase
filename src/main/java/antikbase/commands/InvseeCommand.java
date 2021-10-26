package antikbase.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if(!hasPermissions(player, "open")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage("§cFaites : " + command.getName() + " <username>");
            return true;
        }

        Player target;

        try {
            target = Bukkit.getPlayer(args[0]);
        } catch (Exception ee) {
            player.sendMessage("§cLe joueur n'existe pas ou n'est pas connecté");
            return true;
        }

        player.openInventory(target.getInventory());

        return false;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.invsee.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.invsee." + permission);
    }
}
