package application.controller;

import application.MainApplication;
import application.Session;
import application.util.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class LoginController extends FxmlController {

    @FXML
    private PasswordField txt_password;

    @FXML
    private void initialize() {
        Platform.setImplicitExit(true);
    }

    @FXML
    public void login() {
        // TODO check login credentials and getFileName player id for session
        txt_password.clear();
        try {
            Session.create(1337);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainApplication.getSceneNavigator().activateScene(SceneNavigator.SceneName.MAIN);
    }

    @Override
    public void showDetail() {
        System.out.println("not here");
    }
}
