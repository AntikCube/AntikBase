package antikbase.commands;

import antikbase.AntikBase;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class PtCommand implements CommandExecutor {

    private AntikBase antikBase;

    public PtCommand(AntikBase antikBase) {
        this.antikBase = antikBase;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof ConsoleCommandSender) return true;

        Player player = (Player) sender;

        if(!player.isOp() && !player.hasPermission("antikbase.admin") &&  !player.hasPermission("antikbase.*")) return true;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack.getType() == Material.AIR) {
            player.sendMessage("§cIl vous faut un item dans la main");
            return true;
        }

        if(args.length == 0) {
            if(player.hasMetadata("pt_command")) {
                player.sendMessage("§aCommande supprimée");
                player.removeMetadata("pt_command", antikBase);
            } else {
                player.sendMessage("§cAucune commande enregistrée");
            }
            return true;
        }

        if(player.hasMetadata("pt_command"))
            player.removeMetadata("pt_command", antikBase);


        player.setMetadata("pt_command", new FixedMetadataValue(antikBase, String.join(" ", args) + "::::" + itemStack.getType().name()));
        player.sendMessage("§aCommand définie");


        return false;
    }
}
