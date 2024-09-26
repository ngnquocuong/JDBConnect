package com.example.jdbconnect;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String classes = "net.sourceforge.jtds.jdbc.Driver";
    private static final String ip = "10.0.2.2";
    private static final String port = "1433";
    private static final String db = "ldbc";
    private static final String user = "sa";
    private static final String pass = "12345";

    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        try {
            Class.forName(classes);
            String conURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + db;
            connection = DriverManager.getConnection(conURL, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
