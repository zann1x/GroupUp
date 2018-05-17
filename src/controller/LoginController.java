package controller;

import application.MainApplication;
import application.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Player;
import util.SceneNavigator;

public class LoginController extends FxmlController {

    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    private Label lbl_loginSuccess;

    @FXML
    private void handleLogin() {
        String password = txt_password.getText();
        txt_password.clear();
        try {
            Player player = new Player();
            if (player.checkCredentials(txt_username.getText(), password)) {
                lbl_loginSuccess.setText("");
                Session.create(player);
                MainApplication.instance.getSceneNavigator().activateScene(SceneNavigator.SceneName.MAIN, MainApplication.instance.getMainController());
            } else {
                lbl_loginSuccess.setText("wrong login details!");
            }
        } catch (Exception e) {
        	lbl_loginSuccess.setText("There was a Problem with DB-Connection");;
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @FXML
    private void handleLoginOnEnterKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            handleLogin();
    }
}