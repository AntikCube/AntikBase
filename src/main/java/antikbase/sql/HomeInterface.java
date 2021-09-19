package antikbase.sql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HomeInterface extends SQL {

    private final String table = "Homes";

    public HomeInterface() {
        connectToDb("localhost:3306/antikcubes", null, null);
    }

    @Override
    public void createTables() {
        this.executeAsync("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer not null primary key auto_increment," +
                "user text not null," +
                "name text not null," +
                "world text not null," +
                "x double not null," +
                "y double not null," +
                "z double not null," +
                "pitch float not null," +
                "yaw float not null" +
                ")", table);
    }

    public CompletableFuture<Boolean> canSetHome(Player player, int maxHomes) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        fetchAsync(home -> {
            future.complete(home.size() < maxHomes);
        }, "SELECT * FROM %s WHERE user = '%s';", table, player.getName());
        return future;
    }

    public CompletableFuture<Integer> getHomesCount(Player player) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        fetchAsync(home -> {
            future.complete(home.size());
        }, "SELECT * FROM %s WHERE user = '%s';", table, player.getName());
        return future;
    }


    public void createHome(Player player, String homeName) {
        Location location = player.getLocation();

        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync("INSERT INTO %s (`id`, `user`, `name`, `world`, `x`, `y`, `z`, `pitch`, `yaw`) VALUES (NULL, '%s', '%s', '%s', %s, %s, %s, %s, %s);", table, player.getName(), homeName, worldName, x, y, z, pitch, yaw);
    }

    public void setHome(Player player, String homeName) {
        Location location = player.getLocation();

        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync("UPDATE %s SET world = '%s', x = %s, y = %s, z = %s, pitch = %s, yaw = %s WHERE user = '%s' AND name = '%s';", table, worldName, x, y, z, pitch, yaw, player.getName(), homeName);
    }

    public void removeHome(OfflinePlayer player, String homeName) {
        executeAsync("DELETE FROM Homes WHERE user = '%s' AND name = '%s';", player.getName(), homeName);
    }

    public CompletableFuture<Boolean> homeExist(OfflinePlayer player, String homeName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        fetchAllAsync(data -> future.complete(data.size() > 0), "SELECT * FROM %s WHERE user = '%s' AND name = '%s';", table, player.getName(), homeName);
        return future;
    }

    public CompletableFuture<Location> getHomeLocation(Player player, String homeName) {
        CompletableFuture<Location> future = new CompletableFuture<>();
        fetchAsync(home -> {
            String worldName = home.get("world");
            double x = Double.parseDouble(home.get("x"));
            double y = Double.parseDouble(home.get("y"));
            double z = Double.parseDouble(home.get("z"));
            float pitch = Float.parseFloat(home.get("pitch"));
            float yaw = Float.parseFloat(home.get("yaw"));

            future.complete(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
        }, "SELECT * FROM %s WHERE user = '%s' AND name = '%s';", table, player.getName(), homeName);
        return future;
    }

    public CompletableFuture<List<String>> getHomesList(OfflinePlayer player) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        fetchAllAsync(map ->
                future.complete(map.stream()
                        .map(e -> e.get("name"))
                        .collect(Collectors.toList())), "SELECT * FROM %s WHERE user = '%s';", table, player.getName());
        return future;
    }
}
