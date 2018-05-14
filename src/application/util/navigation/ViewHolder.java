package application.util.navigation;

import application.controller.FxmlController;
import javafx.scene.Node;

public class ViewHolder {

    private Node node;
    private FxmlController controller;

    public ViewHolder(Node node, FxmlController controller) {
        this.node = node;
        this.controller = controller;
    }

    public FxmlController getController() {
        return controller;
    }

    public Node getNode() {
        return node;
    }

}
