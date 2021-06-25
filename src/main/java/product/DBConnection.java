package product;

import java.sql.*;

public class DBConnection {
    private static Connection con;

    public DBConnection(String name, boolean mock) {
        try {
            if (mock) {
                try {
                    Class.forName("org.sqlite.JDBC");
                    con = DriverManager.getConnection("jdbc:sqlite::memory:");
                    prepareDB(con);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                con = DriverManager.getConnection("jdbc:sqlite:" + "src/main/resources/" + name);
            }
            Statement s = con.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON;");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void prepareDB(Connection con) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "create table if not exists 'warehouse' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " 'category' INTEGER REFERENCES CATEGORIES(ID) ON UPDATE CASCADE ON DELETE CASCADE," +
                        " 'name' text unique," +
                        " 'description' text," +
                        " 'producer' text," +
                        " 'amount' double," +
                        " 'price' double" +
                        ");");
        PreparedStatement st2 = con.prepareStatement(
                "create table if not exists 'categories' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " 'title' text unique," +
                        " 'description' text " +
                        ");");
        PreparedStatement st3 = con.prepareStatement(
                "create table if not exists 'users' (" +
                        "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " 'login' text unique," +
                        " 'password' text" +
                        ");");
        int result2 = st2.executeUpdate();
        int result = st.executeUpdate();
        int result3 = st3.executeUpdate();
    }

    public static Connection getConnection() {
        return con;
    }
}
