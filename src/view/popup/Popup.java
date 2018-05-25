package view.popup;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class Popup {

    protected Stage stage;
    protected Scene scene;
    protected Pane root;

    public void showAndWait() {
        stage.showAndWait();
    }

}
