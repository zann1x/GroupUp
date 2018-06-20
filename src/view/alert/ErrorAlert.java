package view.alert;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.util.Duration;

public class ErrorAlert {

    private static final String ERR_MSG = "An error occured!";
    private static final String DB_CONN_ERR = "A connection error occured!";

    private static Alert alert;

    static {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
    }

    public static void showAlert() {
        showAlert(ERR_MSG +
                "\nFor further information contact the developer who didn't want to specify this message any further.");
    }

    public static void showAlert(String content) {
        alert.setContentText(ERR_MSG + "\n" + content);
        showAndWait();
    }

    public static void showConnectionAlert() {
        alert.setContentText(DB_CONN_ERR);
        showAndWait();
    }

    private static void showAndWait() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5.0d), event -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
        alert.showAndWait();
    }

}
