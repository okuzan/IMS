package controllers;

import https.HTTPSClient;
import javafx.scene.input.KeyCode;
import product.Category;
import product.Product;
import product.SQLOperations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    public TableView<Category> table;
    public TableColumn<Object, Object> colId;
    public TableColumn<Object, Object> colTitle;
    public TableColumn<Object, Object> colDesc;
    public TextField searchField;
    public Button btnRefresh;
    public Button btnAdd;
    public Button btnUpdate;
    public Button btnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new HTTPSClient(1, this);
        table.setOnKeyPressed(keyEvent -> {
            if (table.getSelectionModel().getSelectedItem() != null)
                if (keyEvent.getCode().equals(KeyCode.DELETE)) btnDeleteOnAction(new ActionEvent());
        });

    }

    public void prepareTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/category.fxml"));
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
    private void tblCatagoryOnClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            prepareTable();
        } else {
            System.out.println("CLICKED");
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (!table.getSelectionModel().isEmpty()) {
            String item = String.valueOf(table.getSelectionModel().getSelectedItem().getTitle());
            viewSelected(item);
        } else {
            System.out.println("EMPTY SELECTION");
        }

    }
    private void viewSelected(String title) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/category.fxml"));
        try {
            fxmlLoader.load();
            CategoryViewController controller = fxmlLoader.getController();
            controller.updateMode(title);
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


    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login Now");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure to delete this item \n to Confirm click ok");
        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String item = String.valueOf(table.getSelectionModel().getSelectedItem().getId());
            System.out.println("Category id" + item);
            //sql.deleteCategory(Integer.parseInt(item));
            new HTTPSClient(2, Integer.parseInt(item));
            btnRefreshOnAction(actionEvent);
        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        String query = searchField.getText();
        if (!query.trim().isEmpty()) {
            table.getItems().clear();
            new HTTPSClient(3, this, query);
            searchField.setText("");
        } else {
            table.getItems().clear();
            new HTTPSClient(1, this);
        }
    }

    public void searchType(KeyEvent keyEvent) {
    }

    public void onEnter(ActionEvent actionEvent) {
        btnRefreshOnAction(actionEvent);
    }
}
