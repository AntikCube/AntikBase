package antikbase.commands.pl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCommand {

    private String command;
    private List<String> aliases;

    public CustomCommand(String command, String... aliases) {
        this.command = command;
        this.aliases = Arrays.stream(aliases).collect(Collectors.toList());
    }

    public String getCommand() {
        return command;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
