package antikbase.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MessagesCommand implements CommandExecutor {

    public enum CustomCommand {
        VOTE("§6>> Votes pour le serveur avec §ehttp://antikcubes.fr/vote"),
        DISCORD("§6>> Rejoinds notre discord avec §ehttp://antikcubes.fr/nous-rejoindre"),
        SITE("§6>> Visite notre site sur §ehttp://antikcubes.fr"),
        BOUTIQUE("§6>> Accèdes à notre boutique avec §ehttp://boutique.antikcubes.fr");

        private String cmd;
        private String message;

        CustomCommand(String message) {
            this.message = message;
        }

        public void run(CommandSender sender) {
            sender.sendMessage(this.message);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CustomCommand.valueOf(command.getName().toUpperCase()).run(sender);
        return true;
    }
}
