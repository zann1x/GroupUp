package controller.detail.profile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Session;
import controller.FxmlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.Group;
import model.Player;
import model.Team;
import view.alert.ErrorAlert;
import view.popup.teampopup.AddSinglePlayerToTeamPopup;

public class FriendsController extends FxmlController {

	@FXML
	private ComboBox<String> cb_pseudonym;
	@FXML
	private TreeView<Object> tv_friendList;
	@FXML
	private Button btn_remove;
	@FXML
	private Button btn_accept;
	@FXML
	private Button btn_decline;
	@FXML
	private Label lbl_error;

	private ObservableList<String> originalItemList;
	private String oldText = "";
	
	private final String PENDING_FRIENDS_NAME = "pending Friend Requests";
	private final String FRIENDS_NAME = "Friends";

	private ContextMenu friends;

	@Override
	public void initialize() {
	}

	@Override
	public void initForShow() {
		super.initForShow();

		// fill with data
		fillUpComboBox();
		fillTreeView();
		
		// listener for combobox
		cb_pseudonym.getEditor().setOnMouseClicked(event -> {
			if (!cb_pseudonym.isShowing()) {
				cb_pseudonym.show();
			}
			cb_pseudonym.arm();
			event.consume();
		});

		cb_pseudonym.getEditor().setOnKeyReleased(event -> {
			if (event.getCode() != KeyCode.UP & event.getCode() != KeyCode.DOWN & event.getCode() != KeyCode.LEFT
					& event.getCode() != KeyCode.RIGHT & event.getCode() != KeyCode.KP_UP
					& event.getCode() != KeyCode.KP_DOWN & event.getCode() != KeyCode.KP_LEFT
					& event.getCode() != KeyCode.KP_RIGHT) {
				blendOut();
				autoFill();
			} else {
				cb_pseudonym.getEditor().selectPositionCaret(cb_pseudonym.getEditor().getText().length());
			}
		});

		// listener for treeViewItems
		tv_friendList.setOnMouseClicked(event -> {
			TreeItem<Object> treeItem = tv_friendList.getSelectionModel().getSelectedItem();
			if (treeItem != null && treeItem.getParent().getValue().equals(PENDING_FRIENDS_NAME)) {
				btn_remove.setVisible(false);
				btn_accept.setVisible(true);
				btn_decline.setVisible(true);
			} else {
				btn_remove.setVisible(true);
				btn_accept.setVisible(false);
				btn_decline.setVisible(false);
			}

			// set up ContextMenu
			ContextMenu friends = prepareContextMenu();
			if (event.getButton() == MouseButton.SECONDARY) {
				double x = event.getScreenX();
				double y = event.getScreenY();
				friends.show(tv_friendList, x, y);
			}
		});

		tv_friendList.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == false) {
				btn_remove.setVisible(true);
				btn_accept.setVisible(false);
				btn_decline.setVisible(false);
			}
		});

		// counter focusPropertyListener @ tv_friendList
		btn_accept.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == true) {
				btn_decline.setVisible(true);
				btn_accept.setVisible(true);
				btn_remove.setVisible(false);
			}
		});

		btn_accept.setOnMouseReleased(event -> {
			btn_decline.setVisible(false);
			btn_accept.setVisible(false);
			btn_remove.setVisible(true);
		});

		btn_decline.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == true) {
				btn_decline.setVisible(true);
				btn_accept.setVisible(true);
				btn_remove.setVisible(false);
			}
		});

		btn_decline.setOnMouseReleased(event -> {
			btn_decline.setVisible(false);
			btn_accept.setVisible(false);
			btn_remove.setVisible(true);
		});
	}

	@FXML
	private void add() {
		String pseudonym = cb_pseudonym.getSelectionModel().getSelectedItem();

		if (pseudonym != null) {
			try {
				Player newFriend = Session.getInstance().getPlayer().getPlayerByPseudonym(pseudonym);
				Session.getInstance().getPlayer().sendFriendRequest(newFriend);
			} catch (SQLException e) {
				ErrorAlert.showConnectionAlert();
				;
				e.printStackTrace();
			}

			initForShow();
		}
	}

	@FXML
	private void remove() {
		if (tv_friendList.getSelectionModel().getSelectedItem() != null) {
			Player friend = (Player) tv_friendList.getSelectionModel().getSelectedItem().getValue();

			try {
				Session.getInstance().getPlayer().unFriend(friend);
			} catch (SQLException e) {
				ErrorAlert.showConnectionAlert();
				e.printStackTrace();
			}

			initForShow();
		}
	}

	@FXML
	private void accept() {
		Player pendingRequest = (Player) tv_friendList.getSelectionModel().getSelectedItem().getValue();

		try {
			Session.getInstance().getPlayer().acceptFriendRequest(pendingRequest);
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
			e.printStackTrace();
		}

		initForShow();
	}

	@FXML
	private void decline() {
		remove();
		initForShow();
	}

	// keeping things up there clean & tiny
	private void fillUpComboBox() {
		List<String> pseudonyms = new ArrayList<>();
		try {
			pseudonyms = Session.getInstance().getPlayer().getAllUnfriendPseudonyms();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (pseudonyms.isEmpty()) {
			ErrorAlert.showConnectionAlert();
		}

		originalItemList = FXCollections.observableArrayList(pseudonyms);
		ObservableList<String> pseudonymList = FXCollections.observableArrayList(pseudonyms);
		cb_pseudonym.setItems(pseudonymList);
	}
	
	private void blendOut() {
		ObservableList<String> itemList = cb_pseudonym.getItems();
		String currentText = cb_pseudonym.getEditor().getText();
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
			cb_pseudonym.getItems().setAll(originalItemList);
			oldText = "";
			blendOut();
		}
	}

	private void autoFill() {
		// TODO: don't have any idea how to
	}

	private void fillTreeView() {
		List<Player> pendingFriends = loadPendingFriends();
		List<Player> friends = loadFriends();

		TreeItem<Object> rootItem = new TreeItem<>("Friendlist");
		TreeItem<Object> pendingFriendsRoot = new TreeItem<>(PENDING_FRIENDS_NAME);
		TreeItem<Object> friendsRoot = new TreeItem<>(FRIENDS_NAME);

		for (int i = 0; i < pendingFriends.size(); i++) {
			pendingFriendsRoot.getChildren().add(new TreeItem<Object>(pendingFriends.get(i)));
		}

		for (int i = 0; i < friends.size(); i++) {
			friendsRoot.getChildren().add(new TreeItem<Object>(friends.get(i)));
		}

		if (!pendingFriends.isEmpty()) {
			pendingFriendsRoot.setExpanded(true);
			rootItem.getChildren().add(pendingFriendsRoot);
		}

		if (!friends.isEmpty()) {
			friendsRoot.setExpanded(true);
			rootItem.getChildren().add(friendsRoot);
		}

		tv_friendList.setRoot(rootItem);
	}

	private List<Player> loadPendingFriends() {
		List<Player> PendingFriends = new ArrayList<>();
		try {
			PendingFriends = Session.getInstance().getPlayer().getPendingFriendRequest();
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
			e.printStackTrace();
		}

		return PendingFriends;
	}

	private List<Player> loadFriends() {
		List<Player> friends = new ArrayList<>();
		try {
			friends = Session.getInstance().getPlayer().getFriends();
		} catch (SQLException e) {
			ErrorAlert.showConnectionAlert();
			e.printStackTrace();
		}

		return friends;
	}

	private ContextMenu prepareContextMenu() {
		MenuItem addToGroup = new MenuItem("Add to Group");
		MenuItem addToTeam = new MenuItem("Add to Team");

		addToGroup.setOnAction(event -> {
			Player friend = (Player) tv_friendList.getSelectionModel().getSelectedItem().getValue();
			Player mySelf = Session.getInstance().getPlayer();
			try {
				int myGroupId = Session.getInstance().getPlayer().getGroupId();
				Group myGroup = new Group(myGroupId);
				if (myGroup.getLeaderIds().contains(mySelf.getId())) {
					if (Player.getAllLoggedInPlayers().contains(friend)) {
						myGroup.sendInvite(friend);
					} else {
						lbl_error.setText("Your friend isn't currently logged in");
						lbl_error.setVisible(true);
					}
				} else {
					lbl_error.setText("Only Leader can invite players to group");
					lbl_error.setVisible(true);
				}
			} catch (SQLException e) {
				ErrorAlert.showConnectionAlert();
				e.printStackTrace();
			}
			initForShow();
		});

		addToTeam.setOnAction(event -> {
			Player player = (Player) tv_friendList.getSelectionModel().getSelectedItem().getValue();
			
			try {
				List<Integer> teamIds = Session.getInstance().getPlayer().getTeamIds();
				List<Team> teams = new ArrayList<>();
				for (int id : teamIds) {
					Team team = new Team(id);
					teams.add(team);
				}
				
				if (!teams.isEmpty()) {
					AddSinglePlayerToTeamPopup popUp = new AddSinglePlayerToTeamPopup(teams, player);
					popUp.showAndWait();
				} else {
					lbl_error.setText("You are in no Team");
					lbl_error.setVisible(true);
				}
			} catch (SQLException e) {
				ErrorAlert.showConnectionAlert();
				e.printStackTrace();
			}

		});

		// making only one at once showing
		if (friends != null) {
			if (friends.isShowing()) {
				friends.hide();
			}
		}

		friends = new ContextMenu(addToGroup, addToTeam);
		return friends;
	}
}
