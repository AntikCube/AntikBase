package antikbase.commands;

import antikbase.utils.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class MessagesCommand implements CommandExecutor {

    private String KEY = "53BPHzmO8Fo7S4r";

    public enum CustomCommand {
        VOTE(new String[] {""}, "§6>> Votes pour le serveur avec §ehttp://antikcubes.fr/vote"),
        DISCORD(new String[] {""}, "§6>> Rejoinds notre discord avec §ehttp://antikcubes.fr/nous-rejoindre"),
        SITE(new String[] {""}, "§6>> Visite notre site sur §ehttp://antikcubes.fr"),
        BOUTIQUE(new String[] {""}, "§6>> Accèdes à notre boutique avec §ehttp://boutique.antikcubes.fr"),

        // "ressources", "ress", "constru", "const", "tutoriel", "tutorial"
        RESSOURCE(new String[] {"ressources", "ress"}, "", (player) -> {
            Bukkit.dispatchCommand(player, "warp ressources");
        }),
        CONSTRUCTION( new String[] {"constru", "const"}, "", (player) -> {
            Bukkit.dispatchCommand(player, "warp construction");
        }),
        TUTO(new String[] {"tutoriel", "tutorial"}, "", (player) -> {
            Bukkit.dispatchCommand(player, "warp tuto");
        });

        private final String message;
        private final String[] aliases;
        private final Consumer<Player>[] playerConsumers;

        CustomCommand(String[] aliases, String message, Consumer<Player>...playerConsumers) {
            this.aliases = aliases;
            this.message = message;
            this.playerConsumers = playerConsumers;
        }

        public void run(CommandSender sender) {
            if(this.message.length() > 0) {
                sender.sendMessage(this.message);
            }

            if((sender instanceof Player) && this.playerConsumers != null) {
                for(Consumer<Player> playerConsumer : this.playerConsumers) {
                    playerConsumer.accept((Player) sender);
                }
            }
        }

        public boolean hasAliase(String aliase) {
            if(aliases.length == 0)
                return false;

            return Arrays.stream(aliases).anyMatch(alia -> alia.equalsIgnoreCase(aliase));
        }

        public String[] getAliases() {
            return this.aliases;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && command.getName().equalsIgnoreCase("VOTE") && args.length == 1 && args[0].equalsIgnoreCase("claim")) {

            Player player = (Player) sender;

            try {
                JSONObject json = JsonReader.readJsonFromUrl("https://serveur-prive.net/api/vote/json/" + KEY + "/" + player.getAddress().getHostName());
                int status = json.getInt("status");
                int data = json.getInt("data");

            } catch(IOException e) {
                player.sendMessage("§4Une erreur s'est produite lors de la vérification");
            }

        } else {

            for(CustomCommand value : CustomCommand.values()) {
                if(value.hasAliase(command.getName())) {
                    value.run(sender);
                    return true;
                }
            }

            CustomCommand.valueOf(command.getName().toUpperCase()).run(sender);
        }
        return true;
    }
}
