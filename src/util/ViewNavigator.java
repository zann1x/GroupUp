package util;

import application.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class ViewNavigator {

    public enum NodeName {
        PROFILE_OVERVIEW,
        Account,
        Friends,
        Rank,

        TEAM_OVERVIEW,
        TEAM_CREATION,
        OWN_TEAMS,

        GAMES_OVERVIEW,
        PLAY_NOW,
        GAME_LINKING,

        NEWS_OVERVIEW,
        NEWS_DETAIL,

        CHAT_OVERVIEW,
        CHAT_DETAIL
    }

    private static final String overviewPath = "../fxml/overview/";
    private static final String detailPath = "../fxml/detail/";

    private static final String profileOverview = overviewPath + "profile.fxml";
    private static final String account = detailPath + "profile/account.fxml";
    private static final String friends = detailPath + "profile/friends.fxml";
    private static final String rank = detailPath + "profile/rank.fxml";

    private static final String teamOverview = overviewPath + "team.fxml";
    private static final String teamCreation = detailPath + "team/team_creation.fxml";
    private static final String teams = detailPath + "team/own_teams.fxml";

    private static final String gamesOverview = overviewPath + "games.fxml";
    private static final String playNow = detailPath + "/games/play_now.fxml";
    private static final String gameLinking = detailPath + "/games/game_linking.fxml";

    private static final String newsOverview = overviewPath + "news.fxml";
    private static final String newsDetail = detailPath + "news.fxml";

    private static final String chatOverview = overviewPath + "chat.fxml";
    private static final String chatDetail = detailPath + "chat.fxml";

    private Map<NodeName, ViewHolder> fxmlMap;
    private NodeName activeNode;

    public ViewNavigator() throws Exception {
        fxmlMap = new HashMap<>();

        addNode(NodeName.PROFILE_OVERVIEW);
        addNode(NodeName.Account);
        addNode(NodeName.Friends);
        addNode(NodeName.Rank);

        addNode(NodeName.TEAM_OVERVIEW);
        addNode(NodeName.TEAM_CREATION);
        addNode(NodeName.OWN_TEAMS);

        addNode(NodeName.GAMES_OVERVIEW);
        addNode(NodeName.PLAY_NOW);
        addNode(NodeName.GAME_LINKING);

        addNode(NodeName.NEWS_OVERVIEW);
        addNode(NodeName.NEWS_DETAIL);

        addNode(NodeName.CHAT_OVERVIEW);
        addNode(NodeName.CHAT_DETAIL);
    }


    private void addNode(NodeName nodeName) throws Exception {
        FXMLLoader loader;
        switch (nodeName) {
            case PROFILE_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(profileOverview));
                break;
            case Account:
                loader = new FXMLLoader(MainApplication.class.getResource(account));
                break;
            case Friends:
                loader = new FXMLLoader(MainApplication.class.getResource(friends));
                break;
            case Rank:
                loader = new FXMLLoader(MainApplication.class.getResource(rank));
                break;
            case TEAM_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(teamOverview));
                break;
            case TEAM_CREATION:
                loader = new FXMLLoader(MainApplication.class.getResource(teamCreation));
                break;
            case OWN_TEAMS:
                loader = new FXMLLoader(MainApplication.class.getResource(teams));
                break;
            case GAMES_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(gamesOverview));
                break;
            case PLAY_NOW:
                loader = new FXMLLoader(MainApplication.class.getResource(playNow));
                break;
            case GAME_LINKING:
                loader = new FXMLLoader(MainApplication.class.getResource(gameLinking));
                break;
            case NEWS_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(newsOverview));
                break;
            case NEWS_DETAIL:
                loader = new FXMLLoader(MainApplication.class.getResource(newsDetail));
                break;
            case CHAT_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(chatOverview));
                break;
            case CHAT_DETAIL:
                loader = new FXMLLoader(MainApplication.class.getResource(chatDetail));
                break;
            default:
                throw new Exception("node type not supported");
        }

        fxmlMap.put(nodeName, new ViewHolder(loader.load(), loader.getController()));
    }

    public Node getNode(NodeName node) {
        if (!node.name().toLowerCase().contains("overview"))
            activeNode = node;
        ViewHolder viewHolder = fxmlMap.get(node);
        viewHolder.getController().initForShow();
        return viewHolder.getNode();
    }

    public void refreshActiveNode() {
        ViewHolder viewHolder = fxmlMap.get(activeNode);
        if (viewHolder != null)
            viewHolder.getController().initForShow();
    }

}
