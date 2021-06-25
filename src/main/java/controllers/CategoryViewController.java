package controllers;

import https.HTTPSClient;
import product.Category;
import product.DBConnection;
import product.SQLOperations;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class CategoryViewController implements Initializable {
    public TextField nameField;
    public TextArea descArea;
    public Button updateBtn;
    public Button addBtn;
    private String itemTitle;
    public Category category;

    public Label lblHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateMode(String title) {
        lblHeader.setText("CATEGORY DETAILS");
        updateBtn.setVisible(true);
        addBtn.setVisible(false);
        itemTitle = title;
        HTTPSClient httpsClient = new HTTPSClient(4, this, itemTitle);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nameField.setPromptText(category.getTitle());
        descArea.setPromptText(category.getDescription());
    }


    public void btnAddOnAction(ActionEvent actionEvent) throws InterruptedException {
        String name;
        String desc;
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Name field cannot be empty!");
            return;
        } else {
            name = nameField.getText();
        }
        desc = descArea.getText();

        Category category = new Category(name, desc);
        HTTPSClient httpsClient = new HTTPSClient(6, this, category);
        Thread.sleep(500);
        if (!httpsClient.checkCvc) {
            showAlert(Alert.AlertType.ERROR, "Illegal input", "Your name was used before?");
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String name = null;
        String desc = null;

        if (!nameField.getText().isEmpty()) name = nameField.getText();
        if (!descArea.getText().isEmpty()) desc = descArea.getText();

        new HTTPSClient(7, this, name, desc, itemTitle);
        Stage stage = (Stage) descArea.getScene().getWindow();
        stage.close();
    }
}
