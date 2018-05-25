package view.popup;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public abstract class VBoxPopup extends DefaultPopup {

    public VBoxPopup() {
        root = new VBox(5.0d);
        root.setPadding(new Insets(5.0d));
        initStage();
    }

    public void addChild(Node node) {
        root.getChildren().add(node);
    }

}
