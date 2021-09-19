package antikbase.sql;


import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public abstract class SQL {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static Connection connection;
    private String ip;
    private String login;
    private String password;

    public String connectToDb(String ip, String login, String password) {
        this.ip = "jdbc:mysql://" + ip;
        this.login = login;
        this.password = password;

        connect();
        this.createTables();
        disconnect();

        return ip;
    }

    public abstract void createTables();

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(this.ip, this.login, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeAsync(Runnable callback, String sql, Object... params) {
        executorService.submit(() -> {
            execute(String.format(sql, params));
            callback.run();
        });
    }

    public void executeAsync(String sql, Object... params) {
        executorService.submit(() -> execute(String.format(sql, params)));
    }

    public void fetchAllAsync(Consumer<List<Map<String, String>>> callback, String sql, Object... params) {
        executorService.submit(() -> callback.accept(fetchAll(String.format(sql, params))));
    }

    public void fetchAsync(Consumer<Map<String, String>> callback, String sql, Object... params) {
        executorService.submit(() -> callback.accept(fetch(String.format(sql, params))));
    }


    public void execute(String str) {
        connect();
        PreparedStatement request;
        try {
            request = connection.prepareStatement(str);
            request.execute();
            request.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public int executeID(String str) {
        connect();
        PreparedStatement request;
        int id = 0;
        try {
            request = connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            request.execute();
            ResultSet result = request.getGeneratedKeys();

            while (result.next()) {
                id = result.getInt(1);
            }

            request.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return id;
    }

    public List<Map<String, String>> fetchAll(String str) {
        connect();
        PreparedStatement request;
        try {
            request = connection.prepareStatement(str);
            ResultSet result = request.executeQuery();
            List<Map<String, String>> list = new ArrayList<>();
            while (result.next()) {
                Map<String, String> map = new HashMap<>();

                for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                    map.put(result.getMetaData().getColumnName(i + 1), result.getString(i + 1));
                }
                list.add(map);
            }
            request.close();
            disconnect();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return null;
    }

    public Map<String, String> fetch(String str) {
        connect();
        PreparedStatement request;
        try {
            request = connection.prepareStatement(str);
            ResultSet result = request.executeQuery();
            Map<String, String> map = new HashMap<>();
            while (result.next()) {
                for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                    map.put(result.getMetaData().getColumnName(i + 1), result.getString(i + 1));
                }
            }
            request.close();
            disconnect();
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return null;
    }
}

