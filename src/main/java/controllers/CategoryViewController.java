package controllers;

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

    public Label lblHeader;
    private SQLOperations sql;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection con = DBConnection.getConnection();
        sql = new SQLOperations(con);
    }

    public void updateMode(String title) {
        lblHeader.setText("CATEGORY DETAILS");
        updateBtn.setVisible(true);
        addBtn.setVisible(false);
        itemTitle = title;

        Category category = sql.getCategory(itemTitle);
        nameField.setPromptText(category.getTitle());
        descArea.setPromptText(category.getDescription());
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
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
        try {
            sql.insertCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
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
        int id = sql.getCategoryId(itemTitle);
        sql.updateCategory(id, name, desc);
        Stage stage = (Stage) descArea.getScene().getWindow();
        stage.close();
    }
}
