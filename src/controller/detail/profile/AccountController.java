package controller.detail.profile;

import java.sql.SQLException;
import java.util.Locale;

import application.Session;
import controller.FxmlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
	private ComboBox<String> cb_regions;
	@FXML
	private TextField txt_email;
	@FXML
	private Label lbl_success;

	private ObservableList<String> originalItemList;
	private String oldText = "";

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
		cb_regions.getSelectionModel().select(player.getRegion());
		prepareRegionCombobox();
		txt_email.setText(player.getEmail());
	}

	@FXML
	private void save() {
		String forename = txt_forename.getText();
		String surname = txt_surname.getText();
		String pseudonym = txt_pseudonym.getText();
		String password = pw_password.getText();
		String region = cb_regions.getSelectionModel().getSelectedItem();
		String email = txt_email.getText();
		Player player = Session.getInstance().getPlayer();

		try {
			if(password.length() == 0){
				player.updatePlayer(forename, surname, pseudonym, region, email);
			} else {
				player.updatePlayer(forename, surname, pseudonym, password, region, email);				
			}
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
			e.printStackTrace();
		}
		lbl_success.setText("Your profile settings were successfully updated");
		lbl_success.setVisible(true);
		
		try {
			Session.getInstance().getPlayer().refresh();
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
			e.printStackTrace();
		}
		
		initForShow();
	}

	private void prepareRegionCombobox() {
		String[] isos = Locale.getISOCountries();
		String[] countries = new String[isos.length];

		for (int i = 0; i < isos.length; i++) {
			Locale locale = new Locale("", isos[i]);
			countries[i] = locale.getDisplayCountry();
		}

		originalItemList = FXCollections.observableArrayList(countries);
		cb_regions.setItems(FXCollections.observableArrayList(countries));

		// listener for combobox
		cb_regions.getEditor().setOnMouseClicked(event -> {
			if (!cb_regions.isShowing()) {
				cb_regions.show();
			}
			cb_regions.arm();
			event.consume();
		});

		cb_regions.getEditor().setOnKeyReleased(event -> {
			if (event.getCode() != KeyCode.UP & event.getCode() != KeyCode.DOWN & event.getCode() != KeyCode.LEFT
					& event.getCode() != KeyCode.RIGHT & event.getCode() != KeyCode.KP_UP
					& event.getCode() != KeyCode.KP_DOWN & event.getCode() != KeyCode.KP_LEFT
					& event.getCode() != KeyCode.KP_RIGHT) {
				blendOut();
				autoFill();
			} else {
				cb_regions.getEditor().selectPositionCaret(cb_regions.getEditor().getText().length());
			}
		});
	}

	private void blendOut() {
		ObservableList<String> itemList = cb_regions.getItems();
		String currentText = cb_regions.getEditor().getText();
		if (oldText.length() <= currentText.length()) {
			oldText = currentText;
			for (int i = 0; i < itemList.size(); i++) {
				String regex = currentText + ".*";
				if (!itemList.get(i).matches(regex)) {
					itemList.remove(i);
					i = i - 1;
				}
			}
		} else {
			cb_regions.getItems().setAll(originalItemList);
			oldText = "";
			blendOut();
		}
	}

	private void autoFill() {
		// TODO: don't have any idea how to
	}
}
