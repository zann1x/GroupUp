package view.popup;

import application.MainApplication;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class Popup {

    protected Stage stage;
    protected Scene scene;
    protected Pane root;

    public void initStage() {
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainApplication.instance.getPrimaryStage());
        stage.initStyle(StageStyle.UTILITY);
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
