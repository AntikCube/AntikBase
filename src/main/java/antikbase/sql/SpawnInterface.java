package antikbase.sql;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.concurrent.CompletableFuture;

public class SpawnInterface extends SQL {

    private final String table = "Spawn";

    public SpawnInterface() {
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

    public void createSpawn(Location location) {
        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync("INSERT INTO %s (`id`,  `name`, `world`, `x`, `y`, `z`, `pitch`, `yaw`) VALUES (NULL, 'spawn', '%s', %s, %s, %s, %s, %s);", table, worldName, x, y, z, pitch, yaw);
    }

    public void setSpawn(Location location) {
        String worldName = location.getWorld().getName();
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY();
        double z = location.getBlockZ() + 0.5;
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        executeAsync("UPDATE %s SET world = '%s', x = %s, y = %s, z = %s, pitch = %s, yaw = %s WHERE name = 'spawn';", table, worldName, x, y, z, pitch, yaw);
    }

    public CompletableFuture<Boolean> spawnExist() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        fetchAllAsync(data -> future.complete(data.size() > 0), "SELECT * FROM %s WHERE name = 'spawn';", table);
        return future;
    }

    public CompletableFuture<Location> getSpawnLocation() {
        CompletableFuture<Location> future = new CompletableFuture<>();
        fetchAsync(home -> {
            String worldName = home.get("world");
            double x = Double.parseDouble(home.get("x"));
            double y = Double.parseDouble(home.get("y"));
            double z = Double.parseDouble(home.get("z"));
            float pitch = Float.parseFloat(home.get("pitch"));
            float yaw = Float.parseFloat(home.get("yaw"));

            future.complete(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
        }, "SELECT * FROM %s WHERE name = 'spawn';", table);
        return future;
    }
}
