package product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static Connection con;

    public DBConnection(String name) {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + "src/main/resources/" + "WarehouseDB");
            Statement s = con.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON;");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }
}
