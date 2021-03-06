package model;

import application.MainApplication;
import application.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Group extends Party {

    public Group() {
        super();
    }

    public Group(int id) throws SQLException {
        super(id);
    }

    @Override
    public List<Integer> getPlayerIds() throws SQLException {
        sql = "SELECT * FROM group_player_mapping WHERE groupid = ?;";
        return super.getPlayerIds();
    }

    @Override
    protected void getData() throws SQLException {
        sql = "SELECT * FROM `group` WHERE id = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.first()) {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
        }
    }

    @Override
    public List<Integer> getLeaderIds() throws SQLException {
        sql = "SELECT * FROM group_player_mapping WHERE groupid = ? AND leader = true;";
        return super.getLeaderIds();
    }

    @Override
    public void addLeader(Player player) throws SQLException {
        sql = "UPDATE group_player_mapping SET leader = true WHERE groupid = ? AND playerid = ?;";
        super.addLeader(player);
    }

    @Override
    public void removeLeader(Player player) throws SQLException {
        sql = "UPDATE group_player_mapping SET leader = false WHERE groupid = ? AND playerid = ?;";
        super.removeLeader(player);
    }

    @Override
    protected void create(String name) throws SQLException {
        // delete the group with the player's name if one still exists because of errors at closing the last session
        sql = "DELETE FROM `group` WHERE name = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setString(1, name);
        statement.executeUpdate();

        // get the lowest id that is currently not used
        sql = "SELECT MIN(g1.id + 1) AS nextlowestid FROM `group` g1 " +
                "LEFT JOIN `group` g2 ON g1.id + 1 = g2.id " +
                "WHERE g2.id IS NULL;";
        statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.first()) {
            int lowestIdAvailable = resultSet.getInt("nextlowestid");

            sql = "INSERT INTO `group`(id, name) VALUE(?, ?);";
            statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
            statement.setInt(1, lowestIdAvailable);
            statement.setString(2, name);
            statement.executeUpdate();
        }

        sql = "SELECT * FROM `group` WHERE name = ?;";
        super.create(name);
    }

    public void create(Player player) throws SQLException {
        create(player.getPseudonym());
        addPlayer(player, true);
    }

    public static List<Group> getPendingGroupInvites(Player player) throws SQLException {
        String sql = "SELECT groupid FROM group_player_mapping WHERE playerid = ? AND pendingjoin = true;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, player.getId());
        ResultSet resultSet = statement.executeQuery();

        List<Group> invitedGroups = new ArrayList<>();
        while (resultSet.next()) {
            int groupId = resultSet.getInt("groupid");
            invitedGroups.add(new Group(groupId));
        }
        return invitedGroups;
    }

    public List<Integer> getInvitedPlayerIds() throws SQLException {
        sql = "SELECT playerid FROM group_player_mapping WHERE groupid = ? AND pendingjoin = true;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        List<Integer> invitedPlayerIds = new ArrayList<>();
        while (resultSet.next())
            invitedPlayerIds.add(resultSet.getInt("playerid"));
        return invitedPlayerIds;
    }

    public void acceptInvite(Player player) throws SQLException {
        sql = "UPDATE group_player_mapping SET pendingjoin = 0 WHERE groupid = ? AND playerid = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        int cnt = statement.executeUpdate();
        if (cnt != 0) {
            Group group = new Group(player.getGroupId());
            group.removePlayer(player);
        }
    }

    public void declineInvite(Player player) throws SQLException {
        sql = "DELETE FROM group_player_mapping WHERE groupid = ? AND playerid = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, player.getId());
        statement.executeUpdate();
    }

    public void sendInvite(Player player) throws SQLException {
        sql = "INSERT INTO group_player_mapping (groupid, playerid, leader, pendingjoin) VALUE(?, ?, ?, true);";
        super.addPlayer(player, false);
    }

    @Override
    public void addPlayer(Player player, boolean isLeader) throws SQLException {
        sql = "INSERT INTO group_player_mapping (groupid, playerid, leader) VALUE(?, ?, ?);";
        super.addPlayer(player, isLeader);
    }

    @Override
    public void removePlayer(Player player) throws SQLException {
        sql = "DELETE FROM group_player_mapping WHERE groupid = ? AND playerid = ?;";
        super.removePlayer(player);
    }

    private void removePlayerFromAllGroups(Player player) throws SQLException {
        sql = "DELETE FROM group_player_mapping WHERE playerid = ?;";
        PreparedStatement statement = MainApplication.instance.getDbConnector().prepareStatement(sql);
        statement.setInt(1, player.getId());
        statement.executeUpdate();
    }

    @Override
    public void delete() throws SQLException {
        sql = "DELETE FROM `group` WHERE id = ?;";
        super.delete();
    }

    @Override
    public void rename(String name) throws SQLException {
        sql = "UPDATE `group` SET name = ? WHERE id = ?;";
        super.rename(name);
    }

    private void ensureGroupConsistency() throws SQLException {
        if (getLeaderIds().isEmpty()) {
            Player newLeader = getPlayers().get(0);
            addLeader(newLeader);
            rename(newLeader.getPseudonym());
        }
    }

    public void leaveGroup(Player player) throws SQLException {
        removePlayer(player);
        ensureGroupConsistency();

        Group group = new Group();
        group.create(player);
    }

    public void exitOnLogout() throws SQLException {
        removePlayerFromAllGroups(Session.getInstance().getPlayer());

        if (getSize() != 0) {
            ensureGroupConsistency();
        } else {
            delete();
        }
    }

}
