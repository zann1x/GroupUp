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
        PLAY_NOW
    }

    private static final String overviewPath = "../fxml/overview/";
    private static final String detailPath = "../fxml/detail/";

    private static final String profileOverview = overviewPath + "profile.fxml";
    private static final String account = detailPath + "profile/account.fxml";
    private static final String friends = detailPath + "profile/friends.fxml";
    private static final String rank = detailPath + "profile/rank.fxml";

    private static final String teamOverview = overviewPath + "team.fxml";
    private static final String teamCreation = detailPath + "team/team_creation.fxml";
    private static final String ownTeams = detailPath + "team/own_teams.fxml";

    private static final String gamesOverview = overviewPath + "games.fxml";
    private static final String playNow = detailPath + "/games/play_now.fxml";

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
                loader = new FXMLLoader(MainApplication.class.getResource(ownTeams));
                break;
            case GAMES_OVERVIEW:
                loader = new FXMLLoader(MainApplication.class.getResource(gamesOverview));
                break;
            case PLAY_NOW:
                loader = new FXMLLoader(MainApplication.class.getResource(playNow));
                break;
            default:
                throw new Exception("Supplied node type could not be loaded!");
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
