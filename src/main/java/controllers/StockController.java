/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author rifat
 */
public class StockController implements Initializable {
    @FXML
    private AnchorPane acHeadStore;
    @FXML
    private StackPane spMainContent;
    @FXML
    public BorderPane bpStore;
    @FXML
    private Label lblHeader;
    @FXML
    private ToggleButton btnStock;
    @FXML
    private ToggleButton btnCategory;

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup toggleGroup = new ToggleGroup();
        btnStock.setSelected(true);
        btnStock.fire();
        btnStock.setToggleGroup(toggleGroup);
        btnCategory.setToggleGroup(toggleGroup);
    }


    @FXML
    public void btnStockOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Store");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(("/views/products.fxml")));
        Parent newRoot = loader.load();
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(newRoot);

        ProductsController productsController = loader.getController();
        productsController.prepareTable();
        productsController.apCombobox.getStylesheets().add("/style/StoreCombobox.css");
    }

    @FXML
    private void btnCategoryOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Category");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(("/views/categories.fxml")));
        Parent newRoot = loader.load();
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(newRoot);

        CategoryController controller = loader.getController();
        controller.prepareTable();
    }

}
