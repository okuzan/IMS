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
        Product product = new Product(1, "item", "cool", "usa", 1.4, 1.4);
        sqlMain.insertProductData(product);

        System.out.println(sqlMain.getAllProducts());

        assertThat(product.getId()).isNotNull();
        assertThat(sqlMain.getAllProducts().size() - 1).isEqualTo(size);
        assertThat(sqlMain.getProduct(1)).isEqualTo(new Product(1, 1, "item", "cool", "usa", 1.4, 1.4));
    }

    @Test
    void shouldSelectByFilter() {
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        Product product1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product2 = sqlMain.insertProductData(new Product(1, "prod2", "", "", 1.1, 2.1));
        ProductFilter filter = new ProductFilter();
        filter.setMinPrice(1.5);
        assertThat(sqlMain.getByCriteria(filter)).contains(product2);
    }

    @Test
    void shouldReadProduct() {
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

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
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        Product product1 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product2 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        Product product3 = sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));

        System.out.println(sqlMain.getAllProducts());
        sqlMain.deleteProduct(2);
        assertThat(sqlMain.getAllProducts()).containsExactly(product1, product3);
        sqlMain.deleteProduct(1);
        assertThat(sqlMain.getAllProducts()).containsExactly(product3);
        sqlMain.deleteProduct(3);
        assertThat(sqlMain.getAllProducts()).isEmpty();
    }

    @Test
    void shouldUpdateProduct() {
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));
        sqlMain.insertProductData(new Product(1, "prod1", "", "", 1.1, 1.1));

        Product product1upd = (new Product(1, null, "description", "usa", 10.1, 10.));
        Product product2upd = (new Product(1, "prod2plus", null, null, 11.0, 0.9));
        Product product3upd = (new Product(3, null, "", null, null, null));
        Product product4upd = (new Product(2, "prod4plus", "", "", 101.0, 101.0));

        System.out.println(sqlMain.getAllProducts());

        sqlMain.updateProduct(1, 1, null, "description", "usa", 10.1, 10.);
        sqlMain.updateProduct(2, 1, "prod2plus", null, null, 11.0, 0.9);
        sqlMain.updateProduct(3, 3, null, "", null, null, null);
        sqlMain.updateProduct(4, 2, "prod4plus", "", "", 101.0, 101.0);

        assertThat(sqlMain.getProduct(1)).isEqualTo(product1upd);
        assertThat(sqlMain.getProduct(2)).isEqualTo(product2upd);
        assertThat(sqlMain.getProduct(3)).isEqualTo(product3upd);
        assertThat(sqlMain.getProduct(4)).isEqualTo(product4upd);
    }

    @Test
    void shouldLogin() {
        Connection con = DBConnection.getConnection();
        SQLOperations sqlMain = new SQLOperations(con);

        sqlMain.insertUser(new User("loginTest", "passTest"));
        assertThat(sqlMain.isLoginFree("loginTest")).isEqualTo(false);
        assertThat(sqlMain.login("loginTest", "passTest")).isEqualTo(true);
        assertThat(sqlMain.login("loginTest1", "passTest")).isEqualTo(false);
        assertThat(sqlMain.login("loginTest", "passTest(")).isEqualTo(false);
    }

}