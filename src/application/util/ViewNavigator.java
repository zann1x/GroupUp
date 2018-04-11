package application.util;

import application.MainApplication;
import application.controller.FxmlController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;

public class ViewNavigator {

    public enum NodeName {
        PLAYER_OVERVIEW,
        PLAYER_DETAIL,
        TEAM_OVERVIEW,
        TEAM_DETAIL,
        GAMES_OVERVIEW,
        GAMES_DETAIL,
        NEWS_OVERVIEW,
        NEWS_DETAIL,
        CHAT_OVERVIEW,
        CHAT_DETAIL
    }

    private String playerOverview = "fxml/overview/player.fxml";
    private String playerDetail = "fxml/detail/player.fxml";
    private String teamOverview = "fxml/overview/team.fxml";
    private String teamDetail = "fxml/detail/team.fxml";
    private String gamesOverview = "fxml/overview/games.fxml";
    private String gamesDetail = "fxml/detail/games.fxml";
    private String newsOverview = "fxml/overview/news.fxml";
    private String newsDetail = "fxml/detail/news.fxml";
    private String chatOverview = "fxml/overview/chat.fxml";
    private String chatDetail = "fxml/detail/chat.fxml";

    private HashMap<NodeName, Node> fxmlMap;

    public ViewNavigator() throws IOException {
        fxmlMap = new HashMap<>();
        fxmlMap.put(NodeName.PLAYER_OVERVIEW, FXMLLoader.load(MainApplication.class.getResource(playerOverview)));
        fxmlMap.put(NodeName.PLAYER_DETAIL, FXMLLoader.load(MainApplication.class.getResource(playerDetail)));
        fxmlMap.put(NodeName.TEAM_OVERVIEW, FXMLLoader.load(MainApplication.class.getResource(teamOverview)));
        fxmlMap.put(NodeName.TEAM_DETAIL, FXMLLoader.load(MainApplication.class.getResource(teamDetail)));
        fxmlMap.put(NodeName.GAMES_OVERVIEW, FXMLLoader.load(MainApplication.class.getResource(gamesOverview)));
        fxmlMap.put(NodeName.GAMES_DETAIL, FXMLLoader.load(MainApplication.class.getResource(gamesDetail)));
        fxmlMap.put(NodeName.NEWS_OVERVIEW, FXMLLoader.load(MainApplication.class.getResource(newsOverview)));
        fxmlMap.put(NodeName.NEWS_DETAIL, FXMLLoader.load(MainApplication.class.getResource(newsDetail)));
        fxmlMap.put(NodeName.CHAT_OVERVIEW, FXMLLoader.load(MainApplication.class.getResource(chatOverview)));
        fxmlMap.put(NodeName.CHAT_DETAIL, FXMLLoader.load(MainApplication.class.getResource(chatDetail)));
    }

    public Node getNode(NodeName node) {
        return fxmlMap.get(node);
    }

}
