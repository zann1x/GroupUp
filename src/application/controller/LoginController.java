package application.controller;

import application.MainApplication;
import application.Session;
import application.model.Player;
import application.util.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends FxmlController {

    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    @FXML
    private void initialize() {
        Platform.setImplicitExit(true);
    }

    @FXML
    public void login() {
        String password = txt_password.getText();
        txt_password.clear();
        try {
            Player player = new Player();
            if ((player.checkCredentials(txt_username.getText(), password))) {
                Session.create(player);
                MainApplication.instance.getSceneNavigator().activateScene(SceneNavigator.SceneName.MAIN);
            } else {
                System.out.println("wrong login details.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void showDetail() { }

}
