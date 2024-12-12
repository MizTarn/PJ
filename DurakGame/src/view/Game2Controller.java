package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import application.GameApplication;
import card.Card;
import card.CardComparator;
import card.HandForDurak;

public class Game2Controller extends BaseController{

	@FXML
	private Button btnAction;

	@FXML
	private HBox hand;

	@FXML
	private Label sizeDeck;

	@FXML
	private FlowPane table;

	@FXML
	private ImageView trumpCardImage;

	@FXML
	private Label labelTrumpCard; 
	@FXML
	private Label labelMove;
	private final CardComparator<Node> comparator = new CardComparator<>();

	public void setCardsInHand(HandForDurak updating) {
		hand.getChildren().clear();
		if (updating != null) {
			for (Card cd : updating.getCardsInHand()) { 
				ImageView iv = new ImageView(cd.getImage()); 
				iv.setFitHeight(175);
				iv.setFitWidth(100);
				iv.setUserData(cd);
				iv.setOnMouseClicked(this::drop);
				System.out.println("DROP CAI NAY (O TRONG GAME2CONTROLLER)");
				hand.getChildren().add(iv);
			}
			FXCollections.sort(hand.getChildren(), comparator);
		}
	}


	public void setTrumpCardImage(Card cd) {
		trumpCardImage.setImage(cd.getImage());
	}

	public void trumpCardUsed() {
		trumpCardImage.setImage(null);
		labelTrumpCard.setVisible(false);
	} 

	public void setTable(ArrayList<Card> t) {
		table.getChildren().clear();
		if (!t.isEmpty()) {
			for (Card cd : t) {
				ImageView iv = new ImageView(cd.getImage());
				iv.setFitHeight(175);
				iv.setFitWidth(100);
				iv.setUserData(cd);
				iv.setDisable(true);
				table.getChildren().add(iv);
			}
		}
	}

	public void drop(MouseEvent event) {
		ImageView iv = (ImageView) event.getSource();
		Card cd = (Card) iv.getUserData();
		GameApplication.client.sendGameMove("drop_card", cd);
		System.out.println("da gui drop_card(controller): " + iv.getUserData().toString());
	}
}
