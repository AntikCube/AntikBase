package antikbase;

import antikbase.commands.*;
import antikbase.commands.pl.CustomCommand;
import antikbase.economy.commands.EcoCommand;
import antikbase.economy.commands.EcoTabCompleter;
import antikbase.economy.commands.MoneyCommand;
import antikbase.economy.commands.PayCommand;
import antikbase.economy.events.JoinEvent;
import antikbase.events.*;
import antikbase.events.chest.ChestEvents;
import antikbase.events.chest.MinecartHopperEvent;
import antikbase.managers.teleport.TeleportManager;
import antikbase.recipes.Recipes;
import antikbase.sql.HomeInterface;
import antikbase.sql.SpawnInterface;
import antikbase.sql.WarpInterface;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public final class AntikBase extends JavaPlugin {

    private static AntikBase instance;
    private PluginManager pluginManager;
    private static final Logger log = Logger.getLogger("Minecraft");


    private Economy economy;

    private WarpInterface warpInterface;
    private SpawnInterface spawnInterface;
    private HomeInterface homeInterface;

    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        pluginManager = this.getServer().getPluginManager();

        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registers();

        Map<String, String> mess = new HashMap<>();
        mess.put("AA", "Value rigolote");
        getConfig().set("test", mess);

        // https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    private void registers() {
        registerManagers();
        registerInterfaces();
        registerCommands();
        registerEvents();

        registerShapes();
    }

    private void registerManagers() {
        teleportManager = new TeleportManager();
    }

    private void registerCommands() {
        registerCommandExecutor(new SpawnCommands(), new CustomCommand("spawn"), new CustomCommand("setspawn"));
        registerCommandExecutor(new WarpCommands(), new CustomCommand("setwarp"), new CustomCommand("delwarp"), new CustomCommand("warp"), new CustomCommand("warps"));
        registerCommandExecutor(new TpCommands(), new CustomCommand("tpa"), new CustomCommand("tpyes"), new CustomCommand("tp"));
        registerCommandExecutor(new RtpCommand(), new CustomCommand("rtp"));
        registerCommandExecutor(new HomeCommands(), new CustomCommand("sethome"), new CustomCommand("delhome"), new CustomCommand("home"), new CustomCommand("homes"));

        registerCommandExecutor(new StarterCommand(), new CustomCommand("kitstarter"));

        registerCommandExecutor(new MoneyCommand(), new CustomCommand("money"));
        registerCommandExecutor(new PayCommand(), new CustomCommand("pay"));

        registerCommandExecutor(new EcoCommand(), new CustomCommand("eco"));
        registerTabCompleter(new EcoTabCompleter(), "eco");

        registerCommandExecutor(new InvseeCommand(), new CustomCommand("invsee"));

        registerCommandExecutor(new MessagesCommand(), Arrays.stream(MessagesCommand.CustomCommand.values()).map(cmd -> new CustomCommand(cmd.name().toLowerCase(), cmd.getAliases())).toArray(CustomCommand[]::new));
    }

    private void registerEvents() {
        registerEvent(new DisconnectEvent());
        registerEvent(new TeleportEvent());
        registerEvent(new ChestEvents());
        registerEvent(new JoinEvent());
        registerEvent(new MinecartHopperEvent());
        registerEvent(new ShulkerBoxInvEvent());
        registerEvent(new AfkEvent());
        registerEvent(new NPCsEvent());
    }

    private void registerShapes() {
        new Recipes(this);
    }

    private void registerCommandExecutor(CommandExecutor executor, CustomCommand... cmds) {
        for (CustomCommand cmd : cmds) {
            getCommand(cmd.getCommand()).setExecutor(executor);

            if(cmd.getAliases().size() > 0) {
                getCommand(cmd.getCommand()).setAliases(cmd.getAliases());
            }
        }
    }

    private void registerTabCompleter(TabCompleter tabCompleter, String... cmds) {
        for (String cmd : cmds) {
            getCommand(cmd).setTabCompleter(tabCompleter);
        }
    }

    private void registerEvent(Listener listener) {
        this.pluginManager.registerEvents(listener, this);
    }

    public static AntikBase getInstance() {
        return instance;
    }

    private void registerInterfaces() {
        warpInterface = new WarpInterface();
        spawnInterface = new SpawnInterface();
        homeInterface = new HomeInterface();
    }

    public WarpInterface getWarpInterface() {
        return warpInterface;
    }

    public SpawnInterface getSpawnInterface() {
        return spawnInterface;
    }

    public HomeInterface getHomeInterface() {
        return homeInterface;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

}
