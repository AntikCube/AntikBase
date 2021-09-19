package antikbase.economy.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EcoTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        Player player = (Player) sender;

        if(args.length == 1) {
            return getPerms(player);
        } else if(args.length == 2) {
            return null;
        }

        return Collections.emptyList();
    }

    private List<String> getPerms(Player player) {
        List<String> returnList = new ArrayList<>();

        if(hasPermissions(player, "give"))
            returnList.add("give");
        if(hasPermissions(player, "remove"))
            returnList.add("remove");
        if(hasPermissions(player, "set"))
            returnList.add("set");
        if(hasPermissions(player, "reset"))
            returnList.add("reset");

        return returnList;
    }

    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.eco.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.eco." + permission);
    }
}
