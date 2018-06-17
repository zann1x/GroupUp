package view.popup.teampopup;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Paint;
import model.Player;
import model.Team;
import view.popup.VBoxPopup;

public class AddSinglePlayerToTeamPopup extends VBoxPopup {

    public AddSinglePlayerToTeamPopup(List<Team> teams, Player player){
    	Label lbl_heading = new Label("select a Team");
    	
    	ListView<Team> lv_teams = new ListView<>();
    	ObservableList<Team> olTeams = FXCollections.observableArrayList(teams);
    	lv_teams.setItems(olTeams);
    	
    	Label lbl_error = new Label();
    	lbl_error.setVisible(false);
    	
    	lv_teams.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
            	Team team = lv_teams.getSelectionModel().getSelectedItem();
        		try {
    				if(!team.getPlayers().contains(player)){
    					team.addPlayer(player, false);
    					lbl_error.setText("Player was successfully added to Team");
    					lbl_error.setTextFill(Paint.valueOf("green"));
    					lbl_error.setVisible(true);
    				} else {
    					lbl_error.setText("Player is already in that Team");
    					lbl_error.setTextFill(Paint.valueOf("red"));
        				lbl_error.setVisible(true);
    				}
    			} catch (SQLException e) {
    				lbl_error.setText("There was an Problem with database, Player can not be added to Team");
    				lbl_error.setTextFill(Paint.valueOf("red"));
    				lbl_error.setVisible(true);
    				e.printStackTrace();
    			}
            }
        });
    	
    	this.addChild(lbl_heading);
    	this.addChild(lv_teams);
    	this.addChild(lbl_error);
    }
}
