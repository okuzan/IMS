package controllers;

import product.Product;
import product.DBConnection;
import product.SQLOperations;
import product.ProductFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    @FXML
    public StackPane spProductContent;
    @FXML
    public ComboBox<String> categoryList;
    public TableColumn<Object, Object> colId;
    public TableColumn<Object, Object> colCategory;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colDescription;
    public TableColumn<Object, Object> colProducer;
    public TableColumn<Object, Object> colAmount;
    public TableColumn<Object, Object> colPrice;
    public TextField minPriceField;
    public TextField maxPriceField;
    public TextField minQField;
    public TextField maxQField;
    @FXML
    private ComboBox<String> producerList;
    @FXML
    public TableView<Product> table;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label totalLabel;
    private TableColumn<Object, Object> tblClmProductId;
    private TableColumn<Object, Object> tblClmProductName;
    private TableColumn<Object, Object> tblClmProductquantity;
    private TableColumn<Object, Object> tblClmProductUnit;
    private TableColumn<Object, Object> tblClmProductRMA;
    private TableColumn<Object, Object> tblClmProductSupplyer;
    private TableColumn<Object, Object> tblClmProductBrand;
    private TableColumn<Object, Object> tblClmProductCatagory;
    private TableColumn<Object, Object> tblClmProductPursesPrice;
    private TableColumn<Object, Object> tblClmProductSellPrice;
    private TableColumn<Object, Object> tblClmProductdate;
    private TableColumn<Object, Object> tblClmProductAddBy;
    private TableColumn<Object, Object> tblClmProductdescription;

    @FXML
    private Button btnRefresh;
    @FXML
    public AnchorPane apCombobox;

    private SQLOperations sql;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection con = DBConnection.getConnection();
        sql = new SQLOperations(con);
        List<Product> products = sql.getAllProducts();
        for (Product product : products) table.getItems().add(product);
        List<String> producers = sql.getProducers();
        for (String producer : producers) producerList.getItems().add(producer);
        List<String> categories = sql.getCategory();
        for (String category : categories) categoryList.getItems().add(category);
        btnRefresh.fire();
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        table.getItems().clear();
        String input = tfSearch.getText();
        ProductFilter filter = new ProductFilter(null, input, null, null, null, null, null);
        List<Product> products = sql.getByCriteria(filter);
        for (Product product : products) table.getItems().add(product);

    }

    @FXML
    private void btnAddNewOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/product.fxml"));
        try {
            fxmlLoader.load();
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            Stage nStage = new Stage();
            nStage.setScene(scene);
            nStage.showAndWait();
            btnRefresh.fire();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnUpdateOnAction(ActionEvent event) {
        if (!table.getSelectionModel().isEmpty()) {
            String item = String.valueOf(table.getSelectionModel().getSelectedItem().getId());
            viewSelected(Integer.parseInt(item));
        } else {
            System.out.println("EMPTY SELECTION");
        }
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login Now");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure to delete this item \n to Confirm click ok");
        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String item = String.valueOf(table.getSelectionModel().getSelectedItem().getId());
            System.out.println("Product id" + item);
            sql.deleteProduct(Integer.parseInt(item));
            btnRefreshOnAction(event);
        }
    }


    public void prepareTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProducer.setCellValueFactory(new PropertyValueFactory<>("producer"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void viewSelected(int id) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/product.fxml"));
        try {
            fxmlLoader.load();
            ProductViewController controller = fxmlLoader.getController();
            controller.updateMode(id);
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            Stage nStage = new Stage();
            nStage.setScene(scene);
            nStage.showAndWait();
            btnRefresh.fire();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void miSellSelectedOnAction(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            int id = table.getSelectionModel().getSelectedItem().getId();
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("/views/order.fxml"));
            try {
                fXMLLoader.load();
                OrderController controller = fXMLLoader.getController();
                controller.setId(id);
                Parent parent = fXMLLoader.getRoot();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void miBuySelectedOnAction(ActionEvent actionEvent) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            int id = table.getSelectionModel().getSelectedItem().getId();
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("/views/order.fxml"));
            try {
                fXMLLoader.load();
                OrderController controller = fXMLLoader.getController();
                controller.setId(id);
                controller.reverseMode();
                Parent parent = fXMLLoader.getRoot();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void btnRefreshOnAction(ActionEvent event) {
        tfSearch.clear();
        System.out.println(producerList.getValue());
        ProductFilter filter = new ProductFilter();

        String minPriceText = minPriceField.getText();
        if (!minPriceText.isEmpty()) {
            try {
                filter.setMinPrice(Double.parseDouble(minPriceText));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
            }
            minPriceField.setText("");
        }

        String maxPriceText = maxPriceField.getText();
        if (!maxPriceText.isEmpty()) {
            try {
                filter.setMaxPrice(Double.parseDouble(maxPriceText));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
            }
            maxPriceField.setText("");
        }

        String minQText = minQField.getText();
        if (!minQText.isEmpty()) {
            try {
                filter.setMinAmount(Double.parseDouble(minQText));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
            }
            minQField.setText("");
        }

        String maxQText = maxQField.getText();
        if (!maxQText.isEmpty()) {
            try {
                filter.setMaxAmount(Double.parseDouble(maxQText));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
            }
            maxQField.setText("");
        }

        String producer = producerList.getValue();
        if (producer != null) {
            producerList.getSelectionModel().clearSelection();
            filter.setProducer(producer);
        }

        String category = categoryList.getValue();
        if (category != null) {
            categoryList.getSelectionModel().clearSelection();
            Integer id = sql.getCategoryId(category);
            if (id != null) filter.setCategory(id);
        }

        table.getItems().clear();
        List<Product> products = sql.getByCriteria(filter);
        for (Product product : products) table.getItems().add(product);
        double cost = calculateCost(products);
        totalLabel.setText("Total: " + cost);
    }

    private double calculateCost(List<Product> products) {
        double sum = 0.;
        for (Product product : products) sum += (product.getAmount() * product.getPrice());
        return sum;
    }

    public static void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("_");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
    }


    @FXML
    private void tblViewCurrentStoreOnScroll(ScrollEvent event) {
        if (event.isInertia()) {
            System.out.println("ALT DOWN");
        } else {
            System.out.println("Noting");
        }
    }
}
