package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import product.DBConnection;
import product.SQLOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class LoginController implements Initializable {
    public TextField usernameField;
    @FXML
    private PasswordField passField;
    SQLOperations sql;

    public void btnLogin(ActionEvent actionEvent) {
        String pass = passField.getText();
        String login = usernameField.getText();
        if (sql.login(login, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/app.fxml"));
                loader.load();
                Controller controller = loader.getController();
                controller.setUsername(login);
                Parent parent = loader.getRoot();
                Scene adminPanelScene = new Scene(parent);
                Stage adminPanelStage = new Stage();
                adminPanelStage.setMaximized(true);
                adminPanelStage.setScene(adminPanelScene);
                adminPanelStage.getIcons().add(new Image("/image/icon.png"));
                adminPanelStage.setTitle(login);
                adminPanelStage.show();

                Stage stage = (Stage) passField.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Wrong credentials", "Try again!");
            passField.setText("");
        }
    }

    public void pfUserNameOnHitEnter(ActionEvent actionEvent) {
    }

    public void pfUserPassOnHitEnter(ActionEvent actionEvent) {
    }

    public void hlCreateAnAccount(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection con = DBConnection.getConnection();
        sql = new SQLOperations(con);

    }

    public void clearUsername(ActionEvent actionEvent) {
        usernameField.setText("");
    }

    public void clearPassword(ActionEvent actionEvent) {
        passField.setText("");
    }

    public void registerLink(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) passField.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/registration.fxml"));
            loader.load();
            Parent parent = loader.getRoot();
            Scene adminPanelScene = new Scene(parent);
            Stage adminPanelStage = new Stage();
//            adminPanelStage.setMaximized(true);
            adminPanelStage.setScene(adminPanelScene);
            adminPanelStage.getIcons().add(new Image("/image/icon.png"));
            adminPanelStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
