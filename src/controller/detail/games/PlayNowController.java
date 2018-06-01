package controller.detail.games;

import application.Session;
import controller.FxmlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import model.Game;
import model.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayNowController extends FxmlController {

    @FXML
    private BorderPane playNowRoot;

    @FXML
    private ChoiceBox<Game> chb_gameChooser;
    @FXML
    private ChoiceBox<Team> chb_teamChooser;

    @FXML
    private ToggleGroup tg_searchMode;
    @FXML
    private RadioButton rbtn_alone;
    @FXML
    private RadioButton rbtn_group;
    @FXML
    private RadioButton rbtn_team;

    @FXML
    private Label lbl_allOptionsSet;

    private ObservableList<Game> availableGames;
    private ObservableList<Team> availableTeams;

    @Override
    protected void initialize() {
        availableGames = FXCollections.observableArrayList();
        availableTeams = FXCollections.observableArrayList();
    }

    private void initTeamChooser() {
        if (rbtn_alone.isSelected() || rbtn_group.isSelected()) {
            chb_teamChooser.setDisable(true);
        } else if (rbtn_team.isSelected()) {
            chb_teamChooser.setDisable(false);
        }

        tg_searchMode.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals(rbtn_alone) || newValue.equals(rbtn_group))
                    chb_teamChooser.setDisable(true);
                else if (newValue.equals(rbtn_team))
                    chb_teamChooser.setDisable(false);
            }
        });

        try {
            List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();
            List<Team> teams = new ArrayList<>();
            for (int teamId : teamIds) {
                Team team = new Team(teamId);
                if (team.isActive())
                    teams.add(team);
            }
            teams.sort(Comparator.comparing(Team::getName));
            availableTeams.setAll(teams);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chb_teamChooser.setItems(availableTeams);
    }

    private void initGameChooser() {
        try {
            List<Game> games = Game.getAllGames();
            games.sort(Comparator.comparing(Game::getName));
            availableGames.setAll(games);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chb_gameChooser.setItems(availableGames);
    }

    @Override
    public void initForShow() {
        if (playNowRoot != null) {
            availableGames.clear();
            availableTeams.clear();
            rbtn_alone.setSelected(true);
            lbl_allOptionsSet.setText("");

            initTeamChooser();
            initGameChooser();
        }
    }

    @FXML
    public void startSearch() {
        if (chb_gameChooser.getSelectionModel().getSelectedItem() == null ||
                (rbtn_team.isSelected() && chb_teamChooser.getSelectionModel().getSelectedItem() == null)) {
            lbl_allOptionsSet.setTextFill(Paint.valueOf("RED"));
            lbl_allOptionsSet.setText("Not all options are set yet!");
        } else {
            lbl_allOptionsSet.setTextFill(Paint.valueOf("GREEN"));
            lbl_allOptionsSet.setText("under construction...");
        }
    }

}
