package antikbase.economy.commands;

import antikbase.AntikBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoCommand implements CommandExecutor {

    private final Economy economy = AntikBase.getInstance().getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if(args.length > 1) {
            switch(args[0]) {
                case "give":
                    return give(player, args);
                case "remove":
                    return remove(player, args);
                case "set":
                    return set(player, args);
                case "reset":
                    return reset(player, args);
            }

        } else {
            player.sendMessage("§cFaites : /eco <give/remove/set/reset> <username> [<amount>]");
        }

        return false;
    }

    private boolean give(Player player, String[] args) {

        if(!hasPermissions(player, "give")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length == 3) {
            OfflinePlayer target;
            try {
                target = Bukkit.getOfflinePlayer(args[1]);
            } catch (Exception ignore) {
                player.sendMessage("§cCe joueur n'existe pas");
                return true;
            }

            double money;
            try {
                money = Double.parseDouble(args[2].replace(",", "."));
            } catch (Exception ignore) {
                player.sendMessage("§cEntrez une valeur correcte");
                return true;
            }

            economy.depositPlayer(target, money);
            player.sendMessage(economy.format(money) + " §7ajouté au compte de §c" + target.getName());
        } else {
            player.sendMessage("§cFaites : /eco give <username> <amount>");
        }

        return true;
    }

    private boolean remove(Player player, String[] args) {

        if(!hasPermissions(player, "remove")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length == 3) {
            OfflinePlayer target;
            try {
                target = Bukkit.getOfflinePlayer(args[1]);
            } catch (Exception ignore) {
                player.sendMessage("§cCe joueur n'existe pas");
                return true;
            }

            double money;
            try {
                money = Double.parseDouble(args[2].replace(",", "."));
            } catch (Exception ignore) {
                player.sendMessage("§cEntrez une valeur correcte");
                return true;
            }

            economy.withdrawPlayer(target, money);
            player.sendMessage(economy.format(money) + " §7retiré au compte de §c" + target.getName());
        } else {
            player.sendMessage("§cFaites : /eco remove <username> <amount>");
        }

        return true;
    }

    private boolean set(Player player, String[] args) {

        if(!hasPermissions(player, "set")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length == 3) {
            OfflinePlayer target;
            try {
                target = Bukkit.getOfflinePlayer(args[1]);
            } catch (Exception ignore) {
                player.sendMessage("§cCe joueur n'existe pas");
                return true;
            }

            double money;
            try {
                money = Double.parseDouble(args[2].replace(",", "."));
            } catch (Exception ignore) {
                player.sendMessage("§cEntrez une valeur correcte");
                return true;
            }

            economy.withdrawPlayer(target, economy.getBalance(target));
            economy.depositPlayer(target, money);

            player.sendMessage(economy.format(money) + " §7défini au compte de §c" + target.getName());
        } else {
            player.sendMessage("§cFaites : /eco set <username> <amount>");
        }

        return true;
    }

    private boolean reset(Player player, String[] args) {

        if(!hasPermissions(player, "reset")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length == 2) {
            OfflinePlayer target;
            try {
                target = Bukkit.getOfflinePlayer(args[1]);
            } catch (Exception ignore) {
                player.sendMessage("§cCe joueur n'existe pas");
                return true;
            }

            economy.createPlayerAccount(target, "reset");

            player.sendMessage("§7Le compte de §c" + target.getName() + " §7a été réinitialisé");
        } else {
            player.sendMessage("§cFaites : /eco reset <username>");
        }

        return true;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.eco.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.eco." + permission);
    }
}
