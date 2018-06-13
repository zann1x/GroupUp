package controller.detail.profile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Session;
import controller.FxmlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class FriendsController extends FxmlController {
	
	@FXML
	private ComboBox<String> cb_pseudonym;
	@FXML
	private ListView<String> lv_friendlist;
	@FXML
	private Label lbl_error;
	
	private ObservableList<String> originalItemList;
	private String oldText = "";

    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
		super.initForShow();

    	fillUpComboBox();
    	fillListView();
    	
    	cb_pseudonym.getEditor().setOnMouseClicked(event -> {
    		if(!cb_pseudonym.isShowing()){
    			cb_pseudonym.show();
    		}
    		cb_pseudonym.arm();
    		event.consume();
    	});
    	
    	cb_pseudonym.getEditor().setOnKeyReleased(event -> {
    		blendOut();
    		autoFill();
    	});
    }
    
    @FXML
	private void add(){
  		
	}
    
    @FXML
	private void remove(){
  		
	}
  	
  	//keeping things up there clean & tiny
    private void fillUpComboBox(){
  		List<String> pseudonyms = new ArrayList<>();
    	try {
    		pseudonyms = Session.getInstance().getPlayer().getAllPseudonyms();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	if(pseudonyms.isEmpty()){
    		lbl_error.setText("A problem occured, pseudonyms couldn't be loaded");
    	}
    	
    	originalItemList = FXCollections.observableArrayList(pseudonyms);
    	ObservableList<String> pseudonymList = FXCollections.observableArrayList(pseudonyms);
    	cb_pseudonym.setItems(pseudonymList);
  	}
    
    private void fillListView(){
    	List<String> friends = new ArrayList<>();
    	try {
    		friends = Session.getInstance().getPlayer().getAllPseudonyms();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	if(friends.isEmpty()){
    		lbl_error.setText("A problem occured, pseudonyms couldn't be loaded");
    	}
    	
  	}
    
    private void blendOut(){
    	ObservableList<String> itemList = cb_pseudonym.getItems();
    	String currentText = cb_pseudonym.getEditor().getText();
    	if(oldText.length() <= currentText.length()){
    		oldText = currentText;
	    	for (int i = 0; i < itemList.size(); i++){
	    		String regex = currentText + ".*";
	    		if(!itemList.get(i).matches(regex)){
	    			itemList.remove(i);
	    		}	
	    	}
    	} else {
    		cb_pseudonym.getItems().setAll(originalItemList);
    		oldText = "";
    		blendOut();
    	}
    }
    
    private void autoFill(){
    	//TODO: shit i don't have any idea how to
    }
}
