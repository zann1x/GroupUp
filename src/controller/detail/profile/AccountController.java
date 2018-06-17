package controller.detail.profile;

import java.sql.SQLException;

import application.Session;
import controller.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Player;
import view.alert.ErrorAlert;

public class AccountController extends FxmlController {

	@FXML
	private TextField txt_forename;
	@FXML
	private TextField txt_surname;
	@FXML
	private TextField txt_pseudonym;
	@FXML
	private PasswordField pw_password;
	@FXML
	private TextField txt_email;
	
    @Override
    public void initialize() {
    }

    @Override
    public void initForShow() {
		super.initForShow();
		
    	Player player = Session.getInstance().getPlayer();
    	txt_forename.setText(player.getForename());
    	txt_surname.setText(player.getSurname());
    	txt_pseudonym.setText(player.getPseudonym());
    	pw_password.setText("");
    	txt_email.setText(player.getEmail());
    }
	
	@FXML
	private void save(){
		String forename = txt_forename.getText();
		String surname = txt_surname.getText();
		String pseudonym = txt_pseudonym.getText();
		String password = pw_password.getText();
		String email = txt_email.getText();
		
		Player player = Session.getInstance().getPlayer();
		
		try{
			player.updatePlayer(forename, surname, pseudonym, password, email);
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
	        e.printStackTrace();
	    }
	}
}
