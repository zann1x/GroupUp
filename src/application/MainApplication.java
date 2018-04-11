package application;

import application.connection.Connection;
import application.connection.db.SqliteConnection;
import application.controller.MainController;
import application.controller.LoginController;
import application.util.SceneNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    public static final String APPL_NAME = "GroupUp!";
    public static final String APPL_NAME_EXT = APPL_NAME + " - Esports Group Finder";
    public static final String APPL_VERSION = "v0.0.1";

    private Stage primaryStage;
    private static SceneNavigator sceneNavigator;

    private static LoginController loginController;
    private static MainController mainController;

    public static SceneNavigator getSceneNavigator() {
        return sceneNavigator;
    }

    public static LoginController getLoginController() {
        return loginController;
    }

    public static MainController getMainController() {
        return mainController;
    }

    private void initScreens() throws Exception {
        FXMLLoader fxmlLoader;
        Scene scene;
        sceneNavigator = new SceneNavigator(primaryStage);

        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/login.fxml"));
        scene = new Scene(fxmlLoader.load());
        sceneNavigator.addScene(SceneNavigator.SceneName.LOGIN, scene);
        loginController = fxmlLoader.getController();
        loginController.setStage(primaryStage);

        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/main.fxml"));
        scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(MainApplication.class.getResource("css/main.css").toExternalForm());
        sceneNavigator.addScene(SceneNavigator.SceneName.MAIN, scene);
        mainController = fxmlLoader.getController();
        mainController.setStage(primaryStage);

        sceneNavigator.activateScene(SceneNavigator.SceneName.LOGIN);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> { // WindowEvent
            mainController.close();
            event.consume();
        });
        initScreens();
        primaryStage.setTitle(MainApplication.APPL_NAME);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Connection connection = new SqliteConnection("db/test.db");
        try {
            connection.open();
            launch(args);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
