package product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLOperations {
    private final Connection con;

    public SQLOperations(Connection connection) {
        con = connection;
//        try {
//            initialization(name);
//            Statement s = con.createStatement();
//            s.executeUpdate("PRAGMA foreign_keys = ON;");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

//    public Connection getCon() {
//        if (con == null) {
//            initialization("WarehouseDB");
//            return con;
//        }else{
//
//        }
//    }

    public void initialization(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
//            con = DriverManager.getConnection("jdbc:sqlite::memory:");
//            con = DriverManager.getConnection("jdbc:sqlite:" + "src/main/resources/" + name);
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
        } catch (ClassNotFoundException e) {
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
    }

    public Category insertCategory(Category category) {
        try (PreparedStatement statement = con.prepareStatement(
                "INSERT INTO categories(title, description) VALUES (?,?)")) {
            statement.setString(1, category.getTitle());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();
            statement.close();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = resultSet.getInt("last_insert_rowid()");
            category.setId(id);

            return category;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;

        }
    }

    public Product insertProductData(Product product) {
        try (PreparedStatement statement = con.prepareStatement(
                "INSERT INTO warehouse(name, category, description, producer, amount, price) VALUES (?,?,?,?,?,?)")) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getCategory());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getProducer());
            statement.setDouble(5, product.getAmount());
            statement.setDouble(6, product.getPrice());

            statement.executeUpdate();
            statement.close();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = resultSet.getInt("last_insert_rowid()");
            product.setId(id);

            return product;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public User insertUser(User user) {
        try (PreparedStatement statement = con.prepareStatement("INSERT INTO users(login, password) VALUES (?,?)")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            statement.close();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = resultSet.getInt("last_insert_rowid()");
            user.setId(id);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Can't insert product", e);
        }
    }

    public User getUserByLogin(String login) {
        try (Statement st = con.createStatement();
             ResultSet res = st.executeQuery("SELECT * FROM users WHERE login ='" + login + "'");
        ) {
            if (res.next())
                return new User(res.getInt("id"), res.getString("login"), res.getString("password"));
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public List<Product> getAllProducts() {
        try (Statement st = con.createStatement();
             ResultSet res = st.executeQuery("SELECT * FROM warehouse");
        ) {
            return getProducts(res);
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public List<Category> showCategories() {
        try (Statement st = con.createStatement();
             ResultSet res = st.executeQuery("SELECT * FROM categories");
        ) {
            List<Category> categories = new ArrayList<>();
            while (res.next()) categories.add(
                    new Category(
                            res.getInt("id"),
                            res.getString("title"),
                            res.getString("description")
                    )
            );
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public boolean deleteCategory(int id) {
        try {
            String sql = "DELETE FROM categories WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            int i = statement.executeUpdate();
            statement.close();
            return i != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public boolean updateCategory(int id, String name, String desc) {
        try {
            if (name == null && desc == null) return false;
            StringBuilder sb = new StringBuilder();
            List<String> values = new ArrayList<>();
            sb.append("UPDATE categories SET");
            if (name != null) {
                sb.append(" title = ? ,");
                values.add(name);
            }
            if (desc != null) {
                sb.append(" description = ? ,");
                values.add(desc);
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append(" WHERE id = ?");
            System.out.println(sb.toString());
            PreparedStatement statement = con.prepareStatement(sb.toString());
            int j = 1;
            for (String s : values) statement.setString(j++, s);
            statement.setInt(j, id);
            int i = statement.executeUpdate();
            statement.close();
            return i != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public int addCategory(String name) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO categories(title) VALUES (?)");
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.getInt("last_insert_rowid()");
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public Product getProduct(int id) {
        try {
            String sql = "SELECT * FROM warehouse WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            boolean found = statement.execute();
            if (!found) System.out.println("NOT FOUND");
            ResultSet res = statement.getResultSet();
            if (!res.next()) {
                System.out.println("ResultSet in empty in Java");
                return null;
            } else {
                return new Product(
                        res.getInt("id"),
                        res.getInt("category"),
                        res.getString("name"),
                        res.getString("description"),
                        res.getString("producer"),
                        res.getDouble("amount"),
                        res.getDouble("price")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public boolean deleteProduct(int id) {
        try {
            String sql = "DELETE FROM warehouse WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            int i = statement.executeUpdate();
            return i != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public boolean isLoginFree(String login) {
        try {
            String sql = "SELECT * FROM users WHERE login = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            return !rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean login(String login, String password) {
        try {
            String sql = "SELECT password FROM users WHERE login = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, login);
            boolean found = statement.execute();
            if (!found) System.out.println("NOT FOUND");
            ResultSet res = statement.getResultSet();
            if (!res.next()) {
                System.out.println("ResultSet in empty in Java");
            } else {
                return res.getString("password").equals(password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(int id, Integer groupId, String name, String description, String producer, Double
            amount, Double price) {
        try {
            if (name == null && price == null && amount == null && description == null && producer == null && groupId == null) {
                //                throw new RuntimeException("No new data in update!");
                System.out.println("NOTHING NEW");
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE warehouse SET ");
            List<Object> values = new ArrayList<>();
            if (name != null) {
                sb.append(" name = ? ,");
                values.add(name);
            }
            if (price != null) {
                sb.append(" price = ? ,");
                values.add(price);
            }
            if (groupId != null) {
                sb.append(" category = ? ,");
                values.add(groupId);
            }
            if (amount != null) {
                sb.append(" amount = ? ,");
                values.add(amount);
            }
            if (description != null) {
                sb.append(" description = ? ,");
                values.add(description);
            }
            if (producer != null) {
                sb.append(" producer = ? ,");
                values.add(producer);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" WHERE id = ?");
            System.out.println(sb);

            PreparedStatement statement = con.prepareStatement(sb.toString());
            statement.setInt(1, id);
            int i = 1;
            for (Object obj : values) {
                System.out.println("name: " + obj.getClass().getName());
                switch (obj.getClass().getName()) {
                    case "java.lang.String":
                        System.out.printf("i: %s, string\n", i);
                        statement.setString(i++, (String) obj);
                        break;
                    case "java.lang.Double":
                        System.out.printf("i: %s, double\n", i);
                        statement.setDouble(i++, (Double) obj);
                        break;
                    case "java.lang.Integer":
                        System.out.printf("i: %s, int\n", i);
                        statement.setInt(i++, (Integer) obj);
                        break;
                }
            }
            statement.setInt(i, id);
            int exitCode = statement.executeUpdate();
            return exitCode != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public boolean checkData(Product product) {
        if (product.getAmount() != null)
            if (product.getAmount() < 0)
                return false;
        if (product.getPrice() != null)
            if (product.getPrice() < 0)
                return false;
        if (product.getName() != null)
            return !product.getName().equals("");
        return true;
    }

    public Integer getCategoryId(String title) {
        try {
            String sql = "SELECT id FROM categories WHERE title = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
//            statement.close();
            int res = resultSet.getInt("id");
            return (res == 0 ? null : res);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public List<Product> getByCriteria(ProductFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM warehouse WHERE 1=1 ");

        if (filter.getName() != null) sb.append(" AND name like '%").append(filter.getName()).append("%'");
        if (filter.getCategory() != null) sb.append(" AND category = ").append(filter.getCategory());
        if (filter.getProducer() != null)
            sb.append(" AND producer like '%").append(filter.getProducer()).append("%'");
        if (filter.getMinPrice() != null) sb.append(" AND price >= ").append(filter.getMinPrice());
        if (filter.getMaxPrice() != null) sb.append(" AND price <= ").append(filter.getMaxPrice());
        if (filter.getMinAmount() != null) sb.append(" AND amount >= ").append(filter.getMinAmount());
        if (filter.getMaxAmount() != null) sb.append(" AND amount <= ").append(filter.getMaxAmount());

        try (Statement st = con.createStatement(); ResultSet res = st.executeQuery(sb.toString())) {
            return getProducts(res);
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public double getSum() {
        try {
            String sql = "SELECT SUM(price*amount) AS TotalSum FROM warehouse";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getDouble("TotalSum");
        } catch (Exception e) {
            return -1;
        }
    }

    public double getCategorySum(int id) {
        try {
            String sql = "SELECT SUM(price*amount) AS TotalSum FROM warehouse WHERE category = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getDouble("TotalSum");
        } catch (Exception e) {
            return -1;
        }
    }

    public List<String> getProducers() {
        try (Statement st = con.createStatement();
             ResultSet res = st.executeQuery("SELECT DISTINCT producer FROM warehouse");
        ) {
            List<String> producers = new ArrayList<>();
            while (res.next()) producers.add(
                    res.getString("producer")
            );
            st.close();
            return producers;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public List<String> getCategory() {
        try (Statement st = con.createStatement();
             ResultSet res = st.executeQuery("SELECT title FROM categories");
        ) {
            List<String> producers = new ArrayList<>();
            while (res.next()) producers.add(
                    res.getString("title")
            );
            st.close();

            return producers;
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    public Category getCategory(String title) {
        try {
            String sql = "SELECT * FROM categories WHERE title = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet res = statement.executeQuery();
            return new Category(
                    res.getInt("id"),
                    res.getString("title"),
                    res.getString("description"));
        } catch (SQLException e) {
            throw new RuntimeException("Can't select all products!", e);
        }
    }

    private List<Product> getProducts(ResultSet res) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (res.next()) products.add(
                new Product(
                        res.getInt("id"),
                        res.getInt("category"),
                        res.getString("name"),
                        res.getString("description"),
                        res.getString("producer"),
                        res.getDouble("amount"),
                        res.getDouble("price")
                )
        );
        return products;
    }

    public static void main(String[] args) {
        new DBConnection("WarehouseDB");
        SQLOperations sqlMain = new SQLOperations(DBConnection.getConnection());
        System.out.println(sqlMain.isLoginFree("qwerty"));
        System.out.println(sqlMain.isLoginFree("qwerty2"));
        System.out.println(sqlMain.isLoginFree("login"));
        System.out.println(sqlMain.isLoginFree("fdsfs"));
//        System.out.println(sqlMain.login("qwerty", "qwerty2"));
//        System.out.println(sqlMain.getSum());
//        System.out.println(sqlMain.getCategorySum(1));
//        System.out.println(sqlMain.getCategorySum(2));
//        sqlMain.updateCategory(1, "new meat2", "meat cool");
//        sqlMain.initialization("WarehouseDB");
//        sqlMain.insertCategory(new Category("meat", "Only meat here"));
//        sqlMain.insertCategory(new Category("liquid", "Only liquid here"));
//        Database sqlMain = new Database("WarehouseDB");
//        System.out.println(sqlMain.getCategoryId("Meat"));
//        ProductFilter filter = new ProductFilter();
//        filter.setCategory(2);
//        System.out.println((Arrays.toString(sqlMain.getByCriteria(filter).toArray())));
//        filter.setProducer("Hlibokombinat");
//        System.out.println((Arrays.toString(sqlMain.getProducers().toArray())));
//        System.out.println((Arrays.toString(sqlMain.getCategories().toArray())));
//        Statement s = con.createStatement();
//        s.executeUpdate("PRAGMA foreign_keys = ON;");
//        int liqCat = sqlMain.addCategory("Liquids");
//        int meatCat = sqlMain.addCategory("Meat");
//        int id = sqlMain.addCategory("Confectionery");
//        System.out.println(id);
//        System.out.println(liqCat);
//        System.out.println(liqCat);
//        System.out.println(sqlMain.renameCategory(3, "Confectionery"));
//        System.exit(0);
//        sqlMain.insertProductData(new Product(2, "beef", "very tasty and fresh", "Hlibokombinat",
//                15., 14.5));
//        sqlMain.insertProductData(new Product(1, "milk", "very tasty and fresh", "Molokozavod",
//                10., 22.5));
//        sqlMain.insertProductData(new Product(2, "pork", "very tasty and fresh", "Myasokombinat",
//                5., 92.));
//        sqlMain.deleteCategory(2);
//        sqlMain.deleteProduct(2);
//        sqlMain.updateProduct(1,null,null,null,null,15.,null);
    }


    public void close() {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}