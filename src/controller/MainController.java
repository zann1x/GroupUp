package controller;

import application.MainApplication;
import application.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import util.SceneNavigator;
import util.ViewNavigator;

public class MainController extends FxmlController {

    private ViewNavigator viewNavigator;

    @FXML
    private Label lbl_overviewName;
    @FXML
    private Label lbl_detailName;

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
    public void initialize() {
        try {
            viewNavigator = new ViewNavigator();
            iv_profile.setImage(new Image("file:res/img/profile.png"));
            iv_team.setImage(new Image("file:res/img/team.png"));
            iv_games.setImage(new Image("file:res/img/games.png"));
            iv_news.setImage(new Image("file:res/img/news.png"));
            iv_chat.setImage(new Image("file:res/img/chat.png"));
            iv_logout.setImage(new Image("file:res/img/logout_icon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initForShow() {
        showPlayer();
    }

    public void close() {
        logout();
        Platform.exit();
    }

    private void logout() {
        Session session;
        if ((session = Session.getInstance()) != null) {
            session.delete();
        }
        lbl_overviewName.setText("");
        lbl_detailName.setText("");
        overviewPane.getChildren().clear();
        detailPane.getChildren().clear();
    }

    @FXML
    private void handleLogout() {
        logout();
        MainApplication.instance.getSceneNavigator().activateScene(SceneNavigator.SceneName.LOGIN, MainApplication.instance.getLoginController());
    }

    @FXML
    private void showAbout() {
        String sessionName;
        assert (Session.getInstance() != null);
        sessionName = Session.getInstance().toString();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        String content = MainApplication.APPL_NAME_FULL + "\n" +
                "Christian Goller, Lukas Zanner\n\n" +
                "Session: " + sessionName;
        alert.setContentText(content);
        alert.initOwner(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    private void switchToNodeOnPane(ViewNavigator.NodeName nodeName, Pane pane) {
        Node node = viewNavigator.getNode(nodeName);
        pane.getChildren().clear();
        pane.getChildren().add(node);
    }

    public void switchToOverviewNode(ViewNavigator.NodeName nodeName, String headerText) {
        lbl_overviewName.setText(headerText.toUpperCase());
        switchToNodeOnPane(nodeName, overviewPane);
    }

    public void switchToDetailNode(ViewNavigator.NodeName nodeName, String headerText) {
        lbl_detailName.setText(headerText.toUpperCase());
        switchToNodeOnPane(nodeName, detailPane);
    }

    @FXML
    public void showPlayer() {
        switchToOverviewNode(ViewNavigator.NodeName.PROFILE_OVERVIEW, "PROFILE");
    }

    @FXML
    public void showTeam() {
        switchToOverviewNode(ViewNavigator.NodeName.TEAM_OVERVIEW, "TEAM");
    }

    @FXML
    public void showGames() {
        switchToOverviewNode(ViewNavigator.NodeName.GAMES_OVERVIEW, "GAME");
    }

    @FXML
    public void showNews() {
        switchToOverviewNode(ViewNavigator.NodeName.NEWS_OVERVIEW, "NEWS");
    }

    @FXML
    public void showChat() {
        switchToOverviewNode(ViewNavigator.NodeName.CHAT_OVERVIEW, "CHAT");
    }

}
