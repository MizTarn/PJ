package client;

import java.util.ArrayList;
import java.util.Objects;

import card.Card;
import javafx.application.Platform;

public class MessageHandler {
	private GameUpdateListener client;

	public MessageHandler(GameUpdateListener client) {
		this.client = client;
		
	}

	public void parseData(String data) {
		String[] params = data.split("#");
		String command = params[0];
		switch (command) {
		case "waiting", "join_session_success":
			client.setWaitController(client.getUIManager().openWait());
			break;
		case "started": 
			client.setGame2Controller(client.getUIManager().openGameGUI());

			break;
		case "join_session_failed":
			Platform.runLater(() -> client.getMenuController().showJoinFailedDialog());
			break;
		case "start_game":
			handleStartGame(params);
			break;
		case "read_game":
			handleReadGame(params);
			break; 
		default:
			System.err.println("Unknown command: " + command);
		}
	}

	private void handleStartGame(String[] params) {
		String[] cards_hand = params[1].split(",");
		client.setHandFromData(cards_hand, client.getGame2Controller());
		String strTrumpCard = params[2];
		Card trumpCard = new Card(strTrumpCard);
		client.getGame2Controller().setTrumpCardImage(trumpCard);
		String turn = params[3];
		client.setTurn(Boolean.parseBoolean(turn));
		client.getGame2Controller().setDisableActions(client.isTurn());
		client.setIdPlayer(params[5]);
	}

	private void handleReadGame(String[] params) {
		// Xử lý logic "read_game"
		// read_game#card1, card2, card3
		// ...#usedTrump#sizeDeck#endGame,nickname#hand#textAction#permission
		if (!Objects.equals(params[1], "null")) {
			String[] cards_table = params[1].split(",");
			client.setTableFromData(cards_table, client.getGame2Controller());
		} else {
			client.getGame2Controller().setTable(new ArrayList<>());
		}
		String bool = params[2];
		if (Boolean.parseBoolean(bool)) {
			client.getGame2Controller().trumpCardUsed();
		}
		client.getGame2Controller().setSize(Integer.parseInt(params[3]));
		String[] end = params[4].split(",");
		System.out.println("username cua client la " +  client.getUsername());
		if (Boolean.parseBoolean(end[0])) {
			client.setEndController(client.getUIManager().openEnd());
			String name = end[1];
			if (Objects.equals(name, client.getUsername())) {
				String ending = "You Won";
				client.getEndController().setTextState(ending);
			} else {
				String ending = "You Lost";
				client.getEndController().setTextState(ending);
			}
		}
		if (!Objects.equals(params[5], "null")) {
			String[] cards_hand = params[5].split(",");
			client.setHandFromData(cards_hand, client.getGame2Controller());
		} else {
			client.getGame2Controller().setCardsInHand(null);
		}
		String textAction = params[6];
		if (Objects.equals(textAction, "null")) {
			client.getGame2Controller().setTextAction("");
		} else {
			client.getGame2Controller().setTextAction(textAction);
		}
		String turn = params[7]; 
		client.setTurn(Boolean.parseBoolean(turn));
		client.getGame2Controller().setDisableActions(Boolean.parseBoolean(turn));
	}
}
