package application.controller;

import application.MainApplication;
import application.Session;
import application.util.SceneNavigator;
import application.util.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import javax.swing.text.View;
import java.io.IOException;

public class MainController extends FxmlController {

    private ViewNavigator viewNavigator;

    @FXML
    private Pane overviewPane;
    @FXML
    private Pane detailPane;
    @FXML
    private ImageView iv_profile;
    @FXML
    private ImageView iv_team;
    @FXML
    private ImageView iv_games;
    @FXML
    private ImageView iv_news;
    @FXML
    private ImageView iv_chat;
    @FXML
    private ImageView iv_logout;

    @FXML
    private void initialize() {
        try {
            viewNavigator = new ViewNavigator();
            iv_profile.setImage(new Image("file:res/img/profile.png"));
            iv_team.setImage(new Image("file:res/img/team.png"));
            iv_games.setImage(new Image("file:res/img/games.png"));
            iv_news.setImage(new Image("file:res/img/news.png"));
            iv_chat.setImage(new Image("file:res/img/chat.png"));
            iv_logout.setImage(new Image("file:res/img/logout_icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        logout();
        Platform.exit();
    }

    public void logout() {
        overviewPane.getChildren().clear();
        detailPane.getChildren().clear();
        try {
            Session.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        logout();
        MainApplication.getSceneNavigator().activateScene(SceneNavigator.SceneName.LOGIN);
    }

    @FXML
    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        String content = MainApplication.APPL_NAME_EXT + " " + MainApplication.APPL_VERSION + "\n" +
                "Christian Goller, Lukas Zanner";
        alert.setContentText(content);
        alert.initOwner(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public void switchToOverviewNode(ViewNavigator.NodeName nodeName) {
        Node node = viewNavigator.getNode(nodeName);
        overviewPane.getChildren().clear();
        overviewPane.getChildren().add(node);
    }

    public void switchToDetailNode(ViewNavigator.NodeName nodeName) {
        Node node = viewNavigator.getNode(nodeName);
        detailPane.getChildren().clear();
        detailPane.getChildren().add(node);
    }

    @FXML
    public void showPlayer() {
        try {
            switchToOverviewNode(ViewNavigator.NodeName.PLAYER_OVERVIEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showTeam() {
        try {
            switchToOverviewNode(ViewNavigator.NodeName.TEAM_OVERVIEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showGames() {
        try {
            switchToOverviewNode(ViewNavigator.NodeName.GAMES_OVERVIEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showNews() {
        try {
            switchToOverviewNode(ViewNavigator.NodeName.NEWS_OVERVIEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showChat() {
        try {
            switchToOverviewNode(ViewNavigator.NodeName.CHAT_OVERVIEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDetail() {
        System.out.println("not here");
    }
}
