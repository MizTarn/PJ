package view.baccarat;

import java.util.ArrayList;
import java.util.Objects;

import card.Card;
import card.HandForBaccarat;
import client.GameApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public abstract class BaccaratBaseController {
	@FXML
	private Button btnAction;

	@FXML
	private HBox hand;

	@FXML
	private FlowPane table; 
	
	@FXML
	private Label labelMove;
	
	@FXML
	private Text score;

	public void setDisableActions(boolean flag) {
		btnAction.setDisable(!flag);
		for (Node nd : hand.getChildren()) {
			nd.setDisable(!flag);
		}
		labelMove.setVisible(flag);
	}


	public void setTextAction(String str) {
		btnAction.setText(str);
		if (Objects.equals(str, "Play")) {
			System.out.println("DAY LA Play");
			btnAction.setOnAction(this::play);
		} else {
			btnAction.setDisable(true);
		}
	}


	@FXML
	public void play(ActionEvent event) {
		System.out.println("gui du lieu play");
		GameApplication.client.sendGameMove("play", null);
	}

	public void setScore(String string) {
		score.setText("Your score: " + string);
		
	}
	
	public void setCardsInHand(HandForBaccarat updating) {
	}

	public void setTable(ArrayList<Card> t) {
	}

	
	
}
