package util;

import java.util.HashMap;
import java.util.Map;
import application.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

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
        ADD_GAME,
        LINKED_GAMES,

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
    private static final String addGame = detailPath + "/games/add_game.fxml";
    private static final String linked_games = detailPath + "/games/linked_games.fxml";

    private static final String newsOverview = overviewPath + "news.fxml";
    private static final String newsDetail = detailPath + "news.fxml";

    private static final String chatOverview = overviewPath + "chat.fxml";
    private static final String chatDetail = detailPath + "chat.fxml";

    private Map<NodeName, ViewHolder> fxmlMap;

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
        addNode(NodeName.ADD_GAME);
        addNode(NodeName.LINKED_GAMES);

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
            case ADD_GAME:
                loader = new FXMLLoader(MainApplication.class.getResource(addGame));
                break;
            case LINKED_GAMES:
                loader = new FXMLLoader(MainApplication.class.getResource(linked_games));
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
        ViewHolder viewHolder = fxmlMap.get(node);
        viewHolder.getController().initForShow();
        return viewHolder.getNode();
    }

}
