package antikbase;

import antikbase.commands.*;
import antikbase.economy.commands.EcoCommand;
import antikbase.economy.commands.EcoTabCompleter;
import antikbase.economy.commands.MoneyCommand;
import antikbase.economy.commands.PayCommand;
import antikbase.economy.events.JoinEvent;
import antikbase.events.DisconnectEvent;
import antikbase.events.ShulkerBoxInvEvent;
import antikbase.events.chest.ChestEvents;
import antikbase.events.TeleportEvent;
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

    private Economy economy;

    private PluginManager pluginManager;

    private static final Logger log = Logger.getLogger("Minecraft");

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
        registerCommandExecutor(new SpawnCommands(), "spawn", "setspawn");
        registerCommandExecutor(new WarpCommands(), "setwarp", "delwarp", "warp", "warps");
        registerCommandExecutor(new TpCommands(), "tpa", "tpyes", "tp");
        registerCommandExecutor(new RtpCommand(), "rtp");
        registerCommandExecutor(new HomeCommands(), "sethome", "delhome", "home", "homes");

        registerCommandExecutor(new StarterCommand(), "kitstarter");

        registerCommandExecutor(new MoneyCommand(), "money");
        registerCommandExecutor(new PayCommand(), "pay");

        registerCommandExecutor(new EcoCommand(), "eco");
        registerTabCompleter(new EcoTabCompleter(), "eco");

        registerCommandExecutor(new InvseeCommand(), "invsee");

        registerCommandExecutor(new MessagesCommand(), Arrays.stream(MessagesCommand.CustomCommand.values()).map(cmd -> cmd.name().toLowerCase()).toArray(String[]::new));
    }

    private void registerEvents() {
        registerEvent(new DisconnectEvent());
        registerEvent(new TeleportEvent());
        registerEvent(new ChestEvents());
        registerEvent(new JoinEvent());
        registerEvent(new MinecartHopperEvent());
        registerEvent(new ShulkerBoxInvEvent());
    }

    private void registerShapes() {
        new Recipes(this);
    }

    private void registerCommandExecutor(CommandExecutor executor, String... cmds) {
        for (String cmd : cmds) {
            getCommand(cmd).setExecutor(executor);
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
