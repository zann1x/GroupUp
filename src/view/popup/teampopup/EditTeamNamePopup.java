package view.popup.teampopup;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Team;
import view.popup.VBoxPopup;

import java.sql.SQLException;

public class EditTeamNamePopup extends VBoxPopup {

    public EditTeamNamePopup(Team team) {
        Label lbl_newName = new Label("Edit team name");

        TextField tf_newName = new TextField();
        tf_newName.setText(team.getName());
        tf_newName.selectAll();

        Button btn_submit = new Button("Submit");
        btn_submit.setDefaultButton(true);
        btn_submit.setOnAction(event -> {
            try {
                team.rename(tf_newName.getText());
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        Button btn_cancel = new Button("Cancel");
        btn_cancel.setOnAction(event -> stage.close());

        HBox hb_buttons = new HBox();
        hb_buttons.setAlignment(Pos.CENTER_RIGHT);
        hb_buttons.setSpacing(5.0d);
        hb_buttons.getChildren().addAll(btn_submit, btn_cancel);

        addChild(lbl_newName);
        addChild(tf_newName);
        addChild(hb_buttons);
    }

}
