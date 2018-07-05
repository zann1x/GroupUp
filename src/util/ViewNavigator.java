package util;

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
        OWN_TEAMS
    }

    private static final String overviewPath = "/fxml/overview/";
    private static final String detailPath = "/fxml/detail/";

    private static final String profileOverview = overviewPath + "profile.fxml";
    private static final String account = detailPath + "profile/account.fxml";
    private static final String friends = detailPath + "profile/friends.fxml";
    private static final String rank = detailPath + "profile/rank.fxml";

    private static final String teamOverview = overviewPath + "team.fxml";
    private static final String teamCreation = detailPath + "team/team_creation.fxml";
    private static final String ownTeams = detailPath + "team/own_teams.fxml";

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
    }


    private void addNode(NodeName nodeName) throws Exception {
        FXMLLoader loader;
        switch (nodeName) {
            case PROFILE_OVERVIEW:
                loader = new FXMLLoader(getClass().getResource(profileOverview));
                break;
            case Account:
                loader = new FXMLLoader(getClass().getResource(account));
                break;
            case Friends:
                loader = new FXMLLoader(getClass().getResource(friends));
                break;
            case Rank:
                loader = new FXMLLoader(getClass().getResource(rank));
                break;
            case TEAM_OVERVIEW:
                loader = new FXMLLoader(getClass().getResource(teamOverview));
                break;
            case TEAM_CREATION:
                loader = new FXMLLoader(getClass().getResource(teamCreation));
                break;
            case OWN_TEAMS:
                loader = new FXMLLoader(getClass().getResource(ownTeams));
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
