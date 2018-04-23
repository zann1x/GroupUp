package application.controller;

import application.MainApplication;
import application.Session;
import application.model.Player;
import application.util.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            Player player;
            if ((player = checkCredentials(txt_username.getText(), password)) != null) {
                Session.create(player);
                MainApplication.instance.getSceneNavigator().activateScene(SceneNavigator.SceneName.MAIN);
            } else {
                System.out.println("wrong login details.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Player checkCredentials(String username, String password) throws SQLException {
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement("SELECT id, name FROM player WHERE name = ? AND password = ?;");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        // player names are unique, therefore i'm blindly assuming only one player was selected
        Player player = null;
        if (resultSet.first()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            assert name.equals(username);
            player = new Player(id, name);
        }
        return player;
    }

    @Override
    protected void showDetail() { }

}
