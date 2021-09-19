package antikbase.sql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WarpInterface extends SQL {

    private final String table = "Warps";

    public WarpInterface() {
        this.connectToDb("localhost:3306/antikcubes", null, null);
    }

    @Override
    public void createTables() {
        executeAsync("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer not null primary key auto_increment," +
                "name text not null," +
                "world text not null," +
                "x double not null," +
                "y double not null," +
                "z double not null," +
                "pitch float not null," +
                "yaw float not null" +
                ")", table);
    }

    public void createWarp(Player player, String warpName) {
        Location location = player.getLocation();

        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync(
            "INSERT INTO %s (`id`, `name`, `world`, `x`, `y`, `z`, `pitch`, `yaw`) VALUES (NULL, '%s', '%s', %s, %s, %s, %s, %s);",
                table, warpName, worldName, x, y, z, pitch, yaw
        );
    }

    public void setWarp(Player player, String warpName) {
        Location location = player.getLocation();

        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync(
                "UPDATE %s SET world=%s, x=%s, y=%s, z=%s, pitch=%s, yaw=%s WHERE name=%s;",
                table, worldName, x, y, z, pitch, yaw, warpName
        );
    }

    public void removeWarp(String homeName) {
        executeAsync("DELETE FROM %s WHERE name = '%s';", table, homeName);
    }

    public CompletableFuture<Boolean> warpExist(String warpName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        fetchAllAsync(data -> future.complete(data.size() > 0), "SELECT * FROM %s WHERE name = '%s';", table, warpName);
        return future;
    }

    public CompletableFuture<Location> getWarpLocation(String warpName) {
        CompletableFuture<Location> future = new CompletableFuture<>();
        fetchAsync(home -> {
            String worldName = home.get("world");
            double x = Double.parseDouble(home.get("x"));
            double y = Double.parseDouble(home.get("y"));
            double z = Double.parseDouble(home.get("z"));
            float pitch = Float.parseFloat(home.get("pitch"));
            float yaw = Float.parseFloat(home.get("yaw"));

            future.complete(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
        }, "SELECT * FROM %s WHERE name = '%s';", table, warpName);
        return future;
    }

    public CompletableFuture<List<String>> getWarpsList() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        fetchAllAsync(warpsMap ->
                future.complete(warpsMap.stream()
                        .map(e -> e.get("name"))
                        .collect(Collectors.toList())), "SELECT * FROM %s;", table);
        return future;
    }
}
