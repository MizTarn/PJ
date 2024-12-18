package client;

import java.util.ArrayList;
import java.util.Objects;

import card.Card;
import javafx.application.Platform;
import view.ErrorController;
import view.baccarat.BaccaratBaseController;
import view.durak.DurakBaseController;
import view.durak.DurakGameController;

public class ClientMessage {
	private GameUpdateListener client;

	public ClientMessage(GameUpdateListener client) {
		this.client = client;

	}

	public void parseData(String data) {
		System.out.println("data trong mess la: " + data);
		String[] params = data.split("#");
		String command = params[0];
		switch (command) {
		case "waiting", "join_session_success" -> setWait(params);
		case "started" -> client.setGame2Controller(client.getUIManager().openGameGUI());
		case "started_base" -> client.setGameBaseController(client.getUIManager().openGameBase());
		case "join_session_failed" -> Platform.runLater(() -> ErrorController.showJoinFailedDialog());
		case "start_game" -> handleStartGame(params, client.getGame2Controller());
		case "start_game_base" -> handleStartGame(params, client.getGameBaseController());
		case "read_game" -> handleReadGame(client.getGame2Controller(), params);
		case "read_game_base" -> handleReadGame(client.getGameBaseController(), params);
		case "started_baccarat" -> client.setBaccaratGameController(client.getUIManager().openBaccaratGame());
		case "start_game_baccarat_gui" -> handleStartBaccaratGame( client.getBaccaratGameController(), params);
		case "read_game_baccarat_gui" -> handleReadBaccaratGame( client.getBaccaratGameController(),params);
		case "start_game_baccarat_base" -> handleStartBaccaratGame( client.getBaccaratGameBaseController(), params);
		case "read_game_baccarat_base" -> handleReadBaccaratGame( client.getBaccaratGameBaseController(),params);
		case "started_baccarat_base" -> client.setBaccaratGameBaseController(client.getUIManager().openBaccaratGameBase());
		default -> System.err.println("Unknown command: " + command);
		}

	}
	
	public void setWait(String [] params) {
		String s = params[1];
		client.setWaitController(client.getUIManager().openWait());
		client.getWaitController().setID(s);
		
	}
	
	public void handleStartGame(String[] params, DurakBaseController controller) {
		String[] cards_hand = params[1].split(",");
		client.setHandFromData(cards_hand, controller);
		String strTrumpCard = params[2];
		Card trumpCard = new Card(strTrumpCard);
		controller.setTrumpCardImage(trumpCard);
		String turn = params[3];
		client.setTurn(Boolean.parseBoolean(turn));
		controller.setDisableActions(client.isTurn());
		client.setIdPlayer(params[5]);
		client.setPlayer((DurakGameController) (controller));
	}

	public void handleReadGame(DurakBaseController controller, String[] params) {
		// Xử lý logic "read_game"
		// read_game#card1, card2, card3
		// ...#usedTrump#sizeDeck#endGame,nickname#hand#textAction#permission

		if (!Objects.equals(params[1], "null")) {
			String[] cards_table = params[1].split(",");
			client.setTableFromData(cards_table, controller);
		} else {
			controller.setTable(new ArrayList<>());
		}
		String bool = params[2];
		if (Boolean.parseBoolean(bool)) {
			controller.trumpCardUsed();
		}

		controller.setSize(Integer.parseInt(params[3]));
		String[] end = params[4].split(",");
		System.out.println("username cua client la " + client.getUsername());
		if (Boolean.parseBoolean(end[0])) {
//			client.setEndController(client.getUIManager().openEnd());
			String name = end[1];
			if (Objects.equals(name, client.getUsername()) || Objects.equals(name, "you")) {
//				String ending = "You Won";
//				client.getEndController().setTextState(ending);
				client.setEndController(client.getUIManager().openWin());
			} else {
//				String ending = "You Lost";
//				client.getEndController().setTextState(ending);
				client.setEndController(client.getUIManager().openLose());
			}
		}
		if (!Objects.equals(params[5], "null")) {
			String[] cards_hand = params[5].split(",");
			client.setHandFromData(cards_hand, controller);
		} else {
			controller.setCardsInHand(null);
		}
		String textAction = params[6];
		if (Objects.equals(textAction, "null")) {
			controller.setTextAction("");
		} else {
			controller.setTextAction(textAction);
		}
		String turn = params[7];
		client.setTurn(Boolean.parseBoolean(turn));
		controller.setDisableActions(Boolean.parseBoolean(turn));
	}
	
	public void handleStartBaccaratGame(BaccaratBaseController controller, String[] params) { //start_game_baccarat#H-Two,C-Eight,D-Seven#false#1#score
//		System.out.println("handle start game baccrat");
//		
//		System.out.println("params la : " + params);
//		for(String s : params) {
//			System.out.println(s);
//		}
//		System.out.println("----------------------");
		String[] cards_hand = params[1].split(",");
//		for(String s : cards_hand) {
//			System.out.println(s);
//		}
		client.setHandFromData(cards_hand, controller);
		String turn = params[2];
		client.setTurn(Boolean.parseBoolean(turn));
		controller.setDisableActions(client.isTurn());
		client.setIdPlayer(params[3]);
		controller.setScore(params[4]);
		
	}
	
	public void handleReadBaccaratGame(BaccaratBaseController controller, String[] params) {
		// Xử lý logic "read_game"
		// read_game_baccarat#card1, card2, card3#endGame,nickname#hand#permission#i
		System.out.println("handle read game baccrat");
		if (!Objects.equals(params[1], "null")) {
			String[] cards_table = params[1].split(",");
			client.setTableFromData(cards_table, controller);
		} else {
			controller.setTable(new ArrayList<>());
		}
		String[] end = params[2].split(",");
		System.out.println("username cua client la " + client.getUsername());
		if (Boolean.parseBoolean(end[0])) {
			String name = end[1];
			if (Objects.equals(name, client.getUsername()) || Objects.equals(name, "you")) {
				client.setEndController(client.getUIManager().openWin());
			} else {
				client.setEndController(client.getUIManager().openLose());
			}
		}
		String turn = params[4];
		client.setTurn(Boolean.parseBoolean(turn));
		controller.setDisableActions(Boolean.parseBoolean(turn));
	}
}
