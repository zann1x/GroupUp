package view;

import application.MainApplication;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class DefaultPopup extends Popup {

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
