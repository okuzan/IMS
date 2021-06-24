package controllers;

import product.Product;
import product.DBConnection;
import product.SQLOperations;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class ProductViewController implements Initializable {

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
    private SQLOperations sql;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection con = DBConnection.getConnection();
        sql = new SQLOperations(con);
//        List<String> producers = sql.getProducers();
//        for (String producer : producers) producerBox.getItems().add(producer);
        List<String> categories = sql.getCategory();
        for (String category : categories) categoryBox.getItems().add(category);

    }

    public void updateMode(int id) {
        lblHeader.setText("ITEM DETAILS");
        updateBtn.setVisible(true);
        addBtn.setVisible(false);
        itemId = id;

        Product product = sql.getProduct(itemId);
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


        if (!nameField.getText().isEmpty()) name = nameField.getText();
        if (!categoryBox.getSelectionModel().isEmpty()) category = sql.getCategoryId(categoryBox.getValue());
        if (!descArea.getText().isEmpty()) desc = descArea.getText();
//        String producerTxt = producerBox.getValue();
//        if (producerTxt != null) producer = producerTxt;
        if(!producerField.getText().isEmpty()) producer = producerField.getText();
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
            sql.updateProduct(itemId, category, name, desc, producer, amount, price);
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
            showAlert(Alert.AlertType.ERROR, "Check your data", "Illegal input format!");
            return;
        }
        if (categoryBox.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Check your data", "Please elect category!");
            return;
        } else {
            category = sql.getCategoryId(categoryBox.getValue());
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
            sql.insertProductData(product);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Your name was used before?");
        }

        Stage stage = (Stage) categoryBox.getScene().getWindow();
        stage.close();
    }
}
