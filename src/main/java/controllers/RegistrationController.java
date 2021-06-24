package controllers;

import https.HTTPSClient;
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
import org.apache.commons.logging.Log;
import product.DBConnection;
import product.SQLOperations;
import product.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static controllers.ProductsController.showAlert;

public class RegistrationController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField fullName;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField rePassField;

    public void btnRegister(ActionEvent actionEvent) throws InterruptedException {
        String login = usernameField.getText();
        String pass = passField.getText();
        String passSnd = rePassField.getText();

        if (!passSnd.equals(pass)) {
            showAlert(Alert.AlertType.ERROR, "Wrong input", "Your passwords don't match!");
            passField.setText("");
            rePassField.setText("");
            return;
        }
        HTTPSClient httpsClient = new HTTPSClient(8, login);
        Thread.sleep(500);
        if (!httpsClient.checkRc) {
            showAlert(Alert.AlertType.ERROR, "Wrong input", "Your username is already taken!");
            usernameField.setText("");
            return;
        }
        HTTPSClient httpsClient2 = new HTTPSClient(9, new User(login, pass));
        try {
            Stage stage = (Stage) passField.getScene().getWindow();
            stage.close();

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void clearUsername(ActionEvent actionEvent) {
        usernameField.setText("");
    }

    public void clearPassword(ActionEvent actionEvent) {
        passField.setText("");
    }

    public void clearRePassword(ActionEvent actionEvent) {
        rePassField.setText("");
    }

    public void loginLink(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) passField.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/login.fxml"));
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

    public void clearFullName(ActionEvent actionEvent) {
        fullName.clear();
    }
}
