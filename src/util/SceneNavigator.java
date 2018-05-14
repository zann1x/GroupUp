package util;

import java.util.HashMap;
import controller.FxmlController;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    public enum SceneName {
        LOGIN,
        MAIN
    }

    private Stage stage;
    private HashMap<SceneName, Scene> sceneMap;

    public SceneNavigator(Stage stage) {
        this.stage = stage;
        sceneMap = new HashMap<>();
    }

    public void addScene(SceneName name, Scene scene) {
        sceneMap.put(name, scene);
    }

    public void activateScene(SceneName name, FxmlController controller) {
        Scene scene = sceneMap.get(name);
        stage.setScene(scene);
        stage.centerOnScreen();
        controller.initForShow();
    }

}
