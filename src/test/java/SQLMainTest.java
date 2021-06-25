import org.junit.jupiter.api.Test;
import product.*;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

class SQLOperationsTest {

    @Test
    void shouldInsertProduct() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        int size = sqlMain.getAllProducts().size();
        Category category = new Category("cool", "cat");
        sqlMain.insertCategory(category);
        Product product = new Product(1, "item", "cool", "usa", 1.4, 1.4);
        sqlMain.insertProductData(product);

        System.out.println(sqlMain.getAllProducts());

        assertThat(product.getId()).isNotNull();
        assertThat(sqlMain.getAllProducts().size() - 1).isEqualTo(size);
        assertThat(sqlMain.getProduct(1)).isEqualTo(new Product(1, 1, "item", "cool", "usa", 1.4, 1.4));
    }

    @Test
    void shouldInsertCategory() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        int size = sqlMain.showCategories().size();
        Category category = new Category("cool", "cat");
        sqlMain.insertCategory(category);
        System.out.println(sqlMain.showCategories());
        assertThat(category.getId()).isNotNull();
        assertThat(sqlMain.showCategories().size() - 1).isEqualTo(size);
        assertThat(sqlMain.getCategory("cool")).isEqualTo(new Category(1, "cool", "cat"));
    }

    @Test
    void shouldSelectByFilter() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        Category category = new Category("cool", "cat");
        sqlMain.insertCategory(category);

        Product product1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product2 = sqlMain.insertProductData(new Product(1, "prod2", "", "", 1.1, 2.1));
        ProductFilter filter = new ProductFilter();
        filter.setMinPrice(1.5);
        assertThat(sqlMain.getByCriteria(filter)).contains(product2);
    }

    @Test
    void shouldReadProduct() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        Category category = new Category("cool", "cat");
        sqlMain.insertCategory(category);

        Product product1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product2 = sqlMain.insertProductData(new Product(1, "prod2", "", "", 1.1, 2.1));
        Product product3 = sqlMain.insertProductData(new Product(1, "prod3", "", "", 1.1, 3.1));

        System.out.println(sqlMain.getAllProducts());

        assertThat(sqlMain.getProduct(1)).isEqualTo(product1);
        assertThat(sqlMain.getProduct(2)).isEqualTo(product2);
        assertThat(sqlMain.getProduct(3)).isEqualTo(product3);
    }

    @Test
    void shouldDeleteProduct() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        Category category = new Category("cool", "cat");
        sqlMain.insertCategory(category);

        Product product1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product2 = sqlMain.insertProductData(new Product(1, "prod2", "", "", 1.1, 1.1));
        Product product3 = sqlMain.insertProductData(new Product(1, "prod3", "", "", 1.1, 1.1));

        System.out.println(sqlMain.getAllProducts());
        sqlMain.deleteProduct(2);
        assertThat(sqlMain.getAllProducts()).containsExactly(product1, product3);
        sqlMain.deleteProduct(1);
        assertThat(sqlMain.getAllProducts()).containsExactly(product3);
        sqlMain.deleteProduct(3);
        assertThat(sqlMain.getAllProducts()).isEmpty();
    }

    @Test
    void shouldDeleteCategory() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        Category category1 = sqlMain.insertCategory(new Category("cat1", "desc"));
        Category category2 = sqlMain.insertCategory(new Category("cat2", "desc"));
        Category category3 = sqlMain.insertCategory(new Category("cat3", "desc"));

        System.out.println(sqlMain.showCategories());
        sqlMain.deleteCategory(2);
        assertThat(sqlMain.showCategories()).containsExactly(category1, category3);
        sqlMain.deleteCategory(1);
        assertThat(sqlMain.showCategories()).containsExactly(category3);
        sqlMain.deleteCategory(3);
        assertThat(sqlMain.showCategories()).isEmpty();
    }

    @Test
    void shouldUpdateProduct() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        sqlMain.insertCategory(new Category("cool", "cat"));
        sqlMain.insertCategory(new Category("cool2", "cat"));

        sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod2", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod3", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod4", "", "", 1.1, 1.1));

        Product product1upd = (new Product(1, 1, "prod1", "description", "usa", 10.1, 10.));
        Product product2upd = (new Product(2, 1, "prod2plus", "", "", 11.0, 0.9));
        Product product3upd = (new Product(3, 2, "prod3", "", "", 1.1, 1.1));
        Product product4upd = (new Product(4, 2, "prod4plus", "", "", 101.0, 101.0));

        System.out.println(sqlMain.getAllProducts());

        sqlMain.updateProduct(1, 1, "prod1", "description", "usa", 10.1, 10.);
        sqlMain.updateProduct(2, 1, "prod2plus", null, null, 11.0, 0.9);
        sqlMain.updateProduct(3, 2, null, "", null, null, null);
        sqlMain.updateProduct(4, 2, "prod4plus", "", "", 101.0, 101.0);

        assertThat(sqlMain.getProduct(1)).isEqualTo(product1upd);
        assertThat(sqlMain.getProduct(2)).isEqualTo(product2upd);
        assertThat(sqlMain.getProduct(3)).isEqualTo(product3upd);
        assertThat(sqlMain.getProduct(4)).isEqualTo(product4upd);
    }

    @Test
    void shouldCascadeDelete() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);
        sqlMain.insertCategory(new Category("cool", "cat"));
        sqlMain.insertCategory(new Category("cool2", "cat"));
        sqlMain.insertCategory(new Category("cool3", "cat"));

        Product p1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product p2 = sqlMain.insertProductData(new Product(2, "prod2", "", "", 1.1, 1.1));
        Product p3 = sqlMain.insertProductData(new Product(2, "prod3", "", "", 1.1, 1.1));
        Product p4 = sqlMain.insertProductData(new Product(3, "prod4", "", "", 1.1, 1.1));

        System.out.println(sqlMain.getAllProducts());

        sqlMain.deleteCategory(2);
        assertThat(sqlMain.getAllProducts()).containsExactly(p1, p4);
    }

    @Test
    void shouldUpdateCategory() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        sqlMain.insertCategory(new Category("cat1", "desc"));
        sqlMain.insertCategory(new Category("cat2", "desc"));
        sqlMain.insertCategory(new Category("cat3", "desc"));

        Category category1upd = (new Category(1, "cat1upd", "desc"));
        Category category2upd = (new Category(2, "cat20", "desctop"));
        Category category3upd = (new Category(3, "cat3", "desc3"));

        System.out.println(sqlMain.getAllProducts());

        sqlMain.updateCategory(1, "cat1upd", null);
        sqlMain.updateCategory(2, "cat20", "desctop");
        sqlMain.updateCategory(3, null, "desc3");

        assertThat(sqlMain.getCategoryById(1)).isEqualTo(category1upd);
        assertThat(sqlMain.getCategoryById(2)).isEqualTo(category2upd);
        assertThat(sqlMain.getCategoryById(3)).isEqualTo(category3upd);

    }

    @Test
    void shouldLogin() {
        new DBConnection("", true);
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        sqlMain.insertUser(new User("loginTest", "passTest"));
        assertThat(sqlMain.isLoginFree("loginTest")).isEqualTo(false);
        assertThat(sqlMain.login("loginTest", "passTest")).isEqualTo(true);
        assertThat(sqlMain.login("loginTest1", "passTest")).isEqualTo(false);
        assertThat(sqlMain.login("loginTest", "passTest(")).isEqualTo(false);
    }
}