package application;

import application.connection.db.DbConnector;
import application.connection.db.MySqlConnector;
import application.controller.LoginController;
import application.controller.MainController;
import application.util.SceneNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
	
	private final String DB_Name = "GroupUp";

    public static final String APPL_NAME = "GroupUp!";
    public static final String APPL_VERSION = "v0.0.1";
    public static final String APPL_NAME_EXT = APPL_NAME + " - Esports Group Finder";
    public static final String APPL_NAME_FULL = APPL_NAME_EXT + " " + APPL_VERSION;

    public static volatile MainApplication instance;
    private DbConnector dbConnector;

    private Stage primaryStage;
    private SceneNavigator sceneNavigator;

    private LoginController loginController;
    private MainController mainController;

    public DbConnector getDbConnector() {
        return dbConnector;
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
    public void init() throws Exception {
        instance = this;
        dbConnector = new MySqlConnector(DB_Name);

        dbConnector.open();
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
        primaryStage.setTitle(MainApplication.APPL_NAME);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        dbConnector.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
