package application.util;

import application.MainApplication;
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

    private static final String overviewPath = "fxml/overview/";
    private static final String detailPath = "fxml/detail/";

    private static final String playerOverview = overviewPath + "player.fxml";
    private static final String playerDetail = detailPath + "player.fxml";
    private static final String teamOverview = overviewPath + "team.fxml";
    private static final String teamDetail = detailPath + "team.fxml";
    private static final String gamesOverview = overviewPath + "games.fxml";
    private static final String gamesDetail = detailPath + "games.fxml";
    private static final String newsOverview = overviewPath + "news.fxml";
    private static final String newsDetail = detailPath + "news.fxml";
    private static final String chatOverview = overviewPath + "chat.fxml";
    private static final String chatDetail = detailPath + "chat.fxml";

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
