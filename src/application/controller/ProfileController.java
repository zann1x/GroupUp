package application.controller;

import application.util.navigation.ViewNavigator;
import javafx.fxml.FXML;

public class ProfileController extends FxmlController {

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
    }

    @Override
    protected void showDetail() {
        super.showDetail(ViewNavigator.NodeName.Account, "Profile detail");
    }
    
    @FXML
    private void editAccount(){
    	super.showDetail(ViewNavigator.NodeName.Account, "Account Settings");
    }
    
    @FXML
    private void editFriends(){
    	super.showDetail(ViewNavigator.NodeName.Friends, "Friends");
    }
    
    @FXML
    private void editRank(){
    	super.showDetail(ViewNavigator.NodeName.Rank, "Rank Options");
    }
}
