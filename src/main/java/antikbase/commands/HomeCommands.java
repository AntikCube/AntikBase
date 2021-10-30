package antikbase.commands;

import antikbase.AntikBase;
import antikbase.sql.HomeInterface;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class HomeCommands implements CommandExecutor {

    private AntikBase antikBase;
    private HomeInterface homesInterface;

    public HomeCommands(AntikBase antikBase) {
        this.antikBase = antikBase;
        this.homesInterface = antikBase.getHomeInterface();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        switch(command.getName()) {
            case "sethome":
                return setHome(player, command, args);
            case "delhome":
                return delHome(player, command, args);
            case "home":
                return home(player, command, args);
            case "homes":
                return homes(player, command, args);
            case "ahome":
                return ahome(player, command, args);
        }

        return true;
    }

    private boolean setHome(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "set")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String maxHome = player.getEffectivePermissions().stream().filter(perm -> perm.getPermission().contains("antikbase.home.size.")).map(perm -> perm.getPermission().replace("antikbase.home.size.", "")).max(Comparator.comparingInt(Integer::parseInt)).orElse(null);

        if(maxHome == null) {
            player.sendMessage("§cVous n'avez pas la permission d'avoir des homes. Veuillez contacter un administrateur.");
            return true;
        }

        String home = args[0].replaceAll("[^a-zA-Z0-9]", "");

        homesInterface.homeExist(player, home).thenAccept(bool -> {
            if(bool) {
                homesInterface.setHome(player, home);
                player.sendMessage("§aHome mis à jour avec succès");
            } else {
                homesInterface.getHomesList(player).thenAccept(list -> {
                    if(list.size() < Integer.parseInt(maxHome)) {
                        homesInterface.createHome(player, home);
                        player.sendMessage("§aHome créé avec succès");
                    } else {
                        player.sendMessage("§cVous avez atteint votre limite de home (" + list.size() + "/" + maxHome + ")");
                    }
                });

            }
        });

        return true;
    }

    private boolean delHome(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "del")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String home = args[0].replaceAll("[^a-zA-Z0-9]", "");

        homesInterface.homeExist(player, home).thenAccept(bool -> {
            if(bool) {
                homesInterface.removeHome(player, home);
                player.sendMessage("§aHome supprimé avec succès");
            } else {
                player.sendMessage("§cCe home n'existe pas");
            }
        });

        return true;
    }

    private boolean home(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "tp")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <nom>", command.getName()));
            return true;
        }

        String home = args[0].replaceAll("[^a-zA-Z0-9]", "");

        homesInterface.homeExist(player, home).thenAccept(bool -> {
            if(bool) {
                homesInterface.getHomeLocation(player, home).thenAccept(loc -> {
                    Bukkit.getScheduler().runTask(AntikBase.getInstance(), () -> player.teleport(loc));
                });
            } else {
                player.sendMessage("§cCe home n'existe pas");
            }
        });

        return true;
    }

    private boolean homes(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "view")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(String.format("§cFaites : /%s", command.getName()));
            return true;
        }

        String maxHome = player.getEffectivePermissions().stream().filter(perm -> perm.getPermission().contains("antikbase.home.size.")).map(perm -> perm.getPermission().replace("antikbase.home.size.", "")).max(Comparator.comparingInt(Integer::parseInt)).orElse(null);

        if(maxHome == null) {
            player.sendMessage("§cVous n'avez pas la permission d'avoir des homes. Veuillez contacter un administrateur.");
            return true;
        }

        if(hasPermissions(player, "admin") && args.length == 1) {
            try {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                homesInterface.getHomesList(target).thenAccept(list -> {
                    player.sendMessage(String.format("§6Liste des homes de %s ", target.getName()) + " (" + list.size() + "/" + maxHome + ")\n§7" + StringUtils.join(list, "§8, §7"));
                });
            } catch(Exception ignore) {
                player.sendMessage("§cLe joueur n'existe pas");
            }
        } else {
            homesInterface.getHomesList(player).thenAccept(list -> {
                player.sendMessage("§6Liste des homes  (" + list.size() + "/" + maxHome + ") : \n§7" + StringUtils.join(list, "§8, §7"));
            });
        }

        return true;
    }

    private boolean ahome(Player player, Command command, String[] args) {
        if(!hasPermissions(player, "admin")) {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(String.format("§cFaites : /%s <pseudo> <nom>", command.getName()));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String home = args[1].replaceAll("[^a-zA-Z0-9]", "");

        homesInterface.homeExist(target, home).thenAccept(bool -> {
            if(bool) {
                homesInterface.getHomeLocation(player, home).thenAccept(loc -> {
                    Bukkit.getScheduler().runTask(AntikBase.getInstance(), () -> player.teleport(loc));
                });
            } else {
                player.sendMessage("§cCe home n'existe pas");
            }
        });

        return true;
    }


    private boolean hasPermissions(Player player, String permission) {
        return player.isOp()
                || player.hasPermission("antikbase.home.*")
                || player.hasPermission("*")
                || player.hasPermission("antikbase.home." + permission);
    }
}
