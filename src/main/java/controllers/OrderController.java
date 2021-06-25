package controllers;

import https.HTTPSClient;
import product.DBConnection;
import product.Product;
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
    public Double amount;

    public Label lblHeader;
//    private SQLOperations sql;

    public void setId(int id) {
        prodId = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Connection con = DBConnection.getConnection();
//        sql = new SQLOperations(con);
    }

    public void reverseMode() {
        lblHeader.setText("ORDER");
        buyBtn.setVisible(true);
        sellBtn.setVisible(false);
    }

    public void buyAction(ActionEvent actionEvent) {
        if (!qField.getText().isEmpty()) {
            double q;
            try {
                q = Double.parseDouble(qField.getText());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong data");
                return;
            }
            new HTTPSClient(20, this, prodId);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            double newQ = sql.getProduct(prodId).getAmount() + q;
            double newQ = amount + q;
//            sql.updateProduct(prodId, null, null, null, null, newQ, null);
            new HTTPSClient(18, this, new Product(prodId, null, null, null, null, newQ, null));

        }
        Stage stage = (Stage) qField.getScene().getWindow();
        stage.close();
    }


    public void sellAction(ActionEvent actionEvent) {
        if (!qField.getText().isEmpty()) {
            double q;
            try {
                q = Double.parseDouble(qField.getText());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong data");
                return;
            }
            new HTTPSClient(20, this, prodId);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double newQ = amount - q;
            System.out.println("NEWQ " + newQ);
            if (newQ >= 0) {
                Product p = new Product(prodId, null, null, null, null, newQ, null);
//                sql.updateProduct(prodId, null, null, null, null, newQ, null);
                new HTTPSClient(18, this, p);
            } else {
                showAlert(Alert.AlertType.ERROR, "Not enough products!", "You don't have enough products\nto proceed with the operation");
            }
        }
        Stage stage = (Stage) qField.getScene().getWindow();
        stage.close();
    }
}
