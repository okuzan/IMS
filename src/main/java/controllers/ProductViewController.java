package controllers;

import https.HTTPSClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import product.Product;
import product.SQLOperations;

import java.net.URL;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class ProductViewController implements Initializable {
    @FXML
    public TextField nameField;
    public TextField priceField;
    public TextField amountField;
    public TextField producerField;
    public TextArea descArea;
    private Integer itemId;
    public ComboBox<String> categoryBox;
    public Button updateBtn;
    public Button addBtn;
    public Label lblHeader;
    //    private SQLOperations sql;
    public Product product;
    public Integer categoryId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Connection con = DBConnection.getConnection();
//        sql = new SQLOperations(con);
//        List<String> categories = sql.getCategoryTitles();
//        for (String category : categories) categoryBox.getItems().add(category);
        new HTTPSClient(16, this);
    }

    public void updateMode(int id) {
        lblHeader.setText("ITEM DETAILS");
        updateBtn.setVisible(true);
        addBtn.setVisible(false);
        itemId = id;

//        Product product = sql.getProduct(itemId);
        System.out.println("sTART");
        new HTTPSClient(17, this, itemId);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nameField.setPromptText(product.getName());
        amountField.setPromptText(String.valueOf(product.getAmount()));
        producerField.setPromptText(String.valueOf(product.getProducer()));
        priceField.setPromptText(String.valueOf(product.getPrice()));
        descArea.setPromptText(product.getDescription());

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Double price = null;
        Double amount = null;
        Integer category = null;
        String name = null;
        String desc = null;
        String producer = null;

        categoryId = null;
        new HTTPSClient(14, this, categoryBox.getValue());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!nameField.getText().isEmpty()) name = nameField.getText();
        if (!categoryBox.getSelectionModel().isEmpty()) category =
                categoryId;
        System.out.println(categoryId + " cat id");
//                sql.getCategoryId(categoryBox.getValue());
        if (!descArea.getText().isEmpty()) desc = descArea.getText();
//        String producerTxt = producerBox.getValue();
//        if (producerTxt != null) producer = producerTxt;
        if (!producerField.getText().isEmpty()) producer = producerField.getText();
        if (!priceField.getText().trim().isEmpty())
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
                return;
            }
        if (!amountField.getText().trim().isEmpty())
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
                return;
            }
        try {
//            sql.updateProduct(itemId, category, name, desc, producer, amount, price);
            Product p = new Product(itemId, category, name, desc, producer, amount, price);
            new HTTPSClient(18, this, p);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Something's wrong");
        }

        Stage stage = (Stage) categoryBox.getScene().getWindow();
        stage.close();

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        double price;
        double amount;
        Integer category;
        String name;
        String desc;
        String producer;
        System.out.println("HERE");
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Name field cannot be empty!");
            return;
        } else {
            name = nameField.getText();
        }
        try {
            price = Double.parseDouble(priceField.getText());
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Check your data", "Can't parse numbers!");
            return;
        }
        if (categoryBox.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Check your data", "Please select category!");
            return;
        } else {
            new HTTPSClient(14, this, categoryBox.getValue());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            category = categoryId;
//                    sql.getCategoryId(categoryBox.getValue());
        }
        desc = descArea.getText();

        if (!producerField.getText().isEmpty()) {
            producer = producerField.getText().trim();
        } else {
            showAlert(Alert.AlertType.ERROR, "Check your data", "Please specify producer!");
            return;
        }
        Product product = new Product(category, name, desc, producer, amount, price);
        try {
//            sql.insertProductData(product);
            new HTTPSClient(19, this, product);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Your name was used before?");
        }

        Stage stage = (Stage) categoryBox.getScene().getWindow();
        stage.close();
    }
}
