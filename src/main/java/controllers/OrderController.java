package controllers;

import product.DBConnection;
import product.SQLOperations;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class OrderController implements Initializable {
    public TextField qField;
    public Button buyBtn;
    public Button sellBtn;
    private int prodId;

    public Label lblHeader;
    private SQLOperations sql;

    public void setId(int id) {
        prodId = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection con = DBConnection.getConnection();
        sql = new SQLOperations(con);
    }

    public void reverseMode() {
        lblHeader.setText("ORDER");
        buyBtn.setVisible(true);
        sellBtn.setVisible(false);
    }

    public void buyAction(ActionEvent actionEvent) {
        if (!qField.getText().isEmpty()) {
            double q = Double.parseDouble(qField.getText());
            double newQ = sql.getProduct(prodId).getAmount() + q;
            sql.updateProduct(prodId, null, null, null, null, newQ, null);
        }
        Stage stage = (Stage) qField.getScene().getWindow();
        stage.close();
    }


    public void sellAction(ActionEvent actionEvent) {
        if (!qField.getText().isEmpty()) {
            double q = Double.parseDouble(qField.getText());
            double newQ = sql.getProduct(prodId).getAmount() - q;
            if (newQ >= 0) {
                sql.updateProduct(prodId, null, null, null, null, newQ, null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Not enough products!", "You don't have enough products\nto proceed with the operation");
            }
        }
        Stage stage = (Stage) qField.getScene().getWindow();
        stage.close();
    }
}
