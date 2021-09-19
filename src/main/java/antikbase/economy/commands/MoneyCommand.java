package antikbase.economy.commands;

import antikbase.AntikBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private Economy economy;

    public MoneyCommand(AntikBase antikBase) {
        this.economy = antikBase.getEconomy();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {

            try {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (economy.hasAccount(target)) {
                    player.sendMessage("§7Balance de " + target.getName() + " : §c" + economy.format(economy.getBalance(target)));
                } else {
                    player.sendMessage("§cCe joueur n'a pas de compte -> Contactez un administrateur");
                }

            } catch (Exception ignor) {
                player.sendMessage("§cCe joueur n'existe pas");
            }

        } else {
            player.sendMessage("§7Balance : " + economy.format(economy.getBalance(player)));
        }

        return false;
    }
}
