package controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private StackPane acContent;
    @FXML
    private ScrollPane leftSideBarScroolPan;
    @FXML
    private ToggleButton sideMenuToogleBtn;
    @FXML
    private ImageView imgMenuBtn;
    @FXML
    private AnchorPane acDashBord;
    @FXML
    private AnchorPane acHead;
    @FXML
    private AnchorPane acMain;
    @FXML
    private Button btnHome;
    @FXML
    private ImageView imgHomeBtn;
    @FXML
    private Button btnStore;
    @FXML
    private ImageView imgStoreBtn;
    @FXML
    private Button btnAbout;
    @FXML
    private ImageView imgAboutBtn;
    @FXML
    private Label lblUsrName;
    @FXML
    private Label lblUsrNamePopOver;
    @FXML
    private Circle imgUsrTop;
    @FXML
    private Circle circleImgUsr;

    Image menuImage = new Image("/icon/menu.png");
    Image menuImageRed = new Image("/icon/menuRed.png");

    String defultStyle = "-fx-border-width: 0px 0px 0px 5px;"
            + "-fx-border-color:none";

    String activeStyle = "-fx-border-width: 0px 0px 0px 5px;"
            + "-fx-border-color:#FF4E3C";

    Image home = new Image("/icon/home2.png");
    Image stock = new Image("/icon/store2.png");
    Image about = new Image("/icon/about2.png");


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgMenuBtn.setImage(menuImage);
        Image usrImg = new Image("/image/user.jpg");

        imgUsrTop.setFill(new ImagePattern(usrImg));
        circleImgUsr.setFill(new ImagePattern(usrImg));
    }

    public void setUsername(String text) {
        lblUsrName.setText(text);
        lblUsrNamePopOver.setText(text);
    }

    @FXML
    private void hideSideMenu(ActionEvent event) {
        if (sideMenuToogleBtn.isSelected()) {
            imgMenuBtn.setImage(menuImageRed);
            TranslateTransition sideMenu = new TranslateTransition(Duration.millis(200.0), acDashBord);
            sideMenu.setByX(-130);
            sideMenu.play();
            acDashBord.getChildren().clear();
        } else {
            imgMenuBtn.setImage(menuImage);
            TranslateTransition sideMenu = new TranslateTransition(Duration.millis(200.0), acDashBord);
            sideMenu.setByX(130);
            sideMenu.play();
            acDashBord.getChildren().add(leftSideBarScroolPan);
        }
    }

    @FXML
    private void btnLogOut(ActionEvent event) throws IOException {
        acContent.getChildren().clear();
        acContent.getChildren().add(FXMLLoader.load(getClass().getResource("/views/login.fxml")));
        acDashBord.getChildren().clear();
        acContent.setMaxHeight(400);
        acContent.setMaxWidth(400);
        acHead.getChildren().clear();
        acHead.setMaxHeight(0.0);
    }

    //full-screen
    @FXML
    private void acMain(KeyEvent event) {
        if (event.getCode() == KeyCode.F11) {
            Stage stage = (Stage) acMain.getScene().getWindow();
            stage.setFullScreen(true);
        }
    }

    @FXML
    public void btnHomeOnClick(ActionEvent event) throws IOException {
        homeActive();
        Parent newRoot = new FXMLLoader(getClass().getResource(("/views/home.fxml"))).load();
        acContent.getChildren().clear();
        acContent.getChildren().add(newRoot);

    }

    @FXML
    private void btnAboutOnClick(ActionEvent event) throws IOException {
        aboutActive();
        Parent newRoot = new FXMLLoader(getClass().getResource(("/views/about.fxml"))).load();
        acContent.getChildren().clear();
        acContent.getChildren().add(newRoot);

    }

    @FXML
    private void btnStoreOnClick(ActionEvent a) throws IOException {
        storeActive();
        Parent newRoot = new FXMLLoader(getClass().getResource(("/views/stock.fxml"))).load();
        acContent.getChildren().clear();
        acContent.getChildren().add(newRoot);
    }


    private void homeActive() {
        imgStoreBtn.setImage(stock);
        imgAboutBtn.setImage(about);
        btnHome.setStyle(activeStyle);
        btnStore.setStyle(defultStyle);
        btnAbout.setStyle(defultStyle);
    }

    private void storeActive() {
        imgHomeBtn.setImage(home);
        imgAboutBtn.setImage(about);
        btnHome.setStyle(defultStyle);
        btnStore.setStyle(activeStyle);
        btnAbout.setStyle(defultStyle);
    }

    private void aboutActive() {
        imgHomeBtn.setImage(home);
        imgStoreBtn.setImage(stock);
        btnHome.setStyle(defultStyle);
        btnStore.setStyle(defultStyle);
        btnAbout.setStyle(activeStyle);
    }
}
