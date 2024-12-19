package controller.baccarat;

import java.util.ArrayList;

import card.Card;
import card.HandForBaccarat;
import cardprocessor.CardComparator;
import client.GameApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BaccaratGameBaseController extends BaccaratBaseController {

	@FXML
	private HBox hand;

	@FXML
	private FlowPane table;

	private final CardComparator<Node> comparator = new CardComparator<>();

	@Override
	public void setCardsInHand(HandForBaccarat updating) {
		hand.getChildren().clear();
		if (updating != null) {
			for (Card cd : updating.getCardsInHand()) {
				Text iv = new Text(cd.toString() + "  ");
				iv.setStyle("-fx-font-size: 18px; -fx-fill: blue;"); // Định dạng văn bản
				iv.setUserData(cd);
				hand.getChildren().add(iv);
			}
			FXCollections.sort(hand.getChildren(), comparator);
		}
	}
	
	@Override
	public void setTable(ArrayList<Card> t) {
		table.getChildren().clear();
		if (!t.isEmpty()) {
			for (Card cd : t) {
				Text iv = new Text(cd.toString() + "  ");
				iv.setStyle("-fx-font-size: 18px; -fx-fill: blue;"); // Định dạng văn bản
				iv.setUserData(cd);
				iv.setDisable(true);
				table.getChildren().add(iv);
			}
		}
	}
	
	@FXML
	public void play(ActionEvent event) {
		GameApplication.client.sendGameMove("play", null);
	}

}