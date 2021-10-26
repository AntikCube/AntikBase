package antikbase.economy.commands;

import antikbase.AntikBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private Economy economy = AntikBase.getInstance().getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(!hasPermissions(player, "pay")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if (args.length == 2) {
            try {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (economy.hasAccount(target)) {
                    double amount = 0;

                    try {
                        amount = Double.parseDouble(args[1]);
                    } catch(Exception ignor2) {
                        player.sendMessage("§cCette valeur '" + args[1] + "' n'est pas valide");
                        return false;
                    }

                    if(economy.getBalance(player) < amount) {
                        player.sendMessage("§cVous n'avez pas l'argent requis");
                    } else {
                        economy.withdrawPlayer(player, amount);
                        economy.depositPlayer(target, amount);

                        player.sendMessage("§7Vous avez envoyé " + economy.format(amount) + " §7à " + target.getName());
                        if(target.isOnline())
                            ((Player) target).sendMessage("§7" + player.getName() + " vous a envoyé " + economy.format(amount));
                    }

                } else {
                    player.sendMessage("§cCe joueur n'a pas de compte -> Contactez un administrateur");
                }

            } catch (Exception ignor1) {
                player.sendMessage("§cCe joueur n'existe pas");
            }
        } else {
            player.sendMessage("§cFaites : /pay <username> <amount>");
        }

        return false;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.money.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.money." + permission);
    }
}
