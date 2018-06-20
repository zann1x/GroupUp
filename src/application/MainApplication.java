package application;

import connection.MySqlConnector;
import controller.LoginController;
import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.SceneNavigator;

public class MainApplication extends Application {

    private static final String DB_NAME = "GroupUp";

    public static final String APPL_NAME = "GroupUp!";
    public static final String APPL_VERSION = "v0.0.1";
    public static final String APPL_NAME_EXT = APPL_NAME + " - Esports Group Finder";
    public static final String APPL_NAME_FULL = APPL_NAME_EXT + " " + APPL_VERSION;

    public static volatile MainApplication instance;
    private MySqlConnector mySqlConnector;

    private Stage primaryStage;
    private SceneNavigator sceneNavigator;

    private LoginController loginController;
    private MainController mainController;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public MySqlConnector getDbConnector() {
        return mySqlConnector;
    }

    public SceneNavigator getSceneNavigator() {
        return sceneNavigator;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public MainController getMainController() {
        return mainController;
    }

    private void initScenes() throws Exception {
        FXMLLoader fxmlLoader;
        Scene scene;
        sceneNavigator = new SceneNavigator(primaryStage);

        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("../fxml/login.fxml"));
        scene = new Scene(fxmlLoader.load());
        sceneNavigator.addScene(SceneNavigator.SceneName.LOGIN, scene);
        loginController = fxmlLoader.getController();
        loginController.setStage(primaryStage);

        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("../fxml/main.fxml"));
        scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(MainApplication.class.getResource("../css/main.css").toExternalForm());
        sceneNavigator.addScene(SceneNavigator.SceneName.MAIN, scene);
        mainController = fxmlLoader.getController();
        mainController.setStage(primaryStage);

        sceneNavigator.activateScene(SceneNavigator.SceneName.LOGIN, loginController);
    }

    @Override
    public void init() throws Exception {
        instance = this;
        mySqlConnector = new MySqlConnector(DB_NAME);
        mySqlConnector.open();
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
        initScenes();
        primaryStage.getIcons().add(new Image("file:res/img/play.png"));
        primaryStage.setTitle(MainApplication.APPL_NAME);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        mySqlConnector.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
