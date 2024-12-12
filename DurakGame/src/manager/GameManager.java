package manager;

import java.util.stream.Collectors;

import card.Deck;
import player.Player;

public class GameManager {
	private Table tableManager;
	private PlayerManager playerManager;
	private TurnManager turnManager;
//	private boolean changeMove;

//	private int currentActivePlayer;
	private boolean gameStarted;

	public GameManager() {
		tableManager = new Table();
		playerManager = new PlayerManager();
		turnManager = new TurnManager(playerManager);
//		changeMove = false; 

		gameStarted = false;
//		currentActivePlayer = 0;
	}

	public GameManager(Table tableManager, PlayerManager players, TurnManager turnManager) {
		this.tableManager = tableManager;
		this.playerManager = players;
		this.turnManager = turnManager;
//		usedTrumpCard = false;
//		turnManager.setCurrentActivePlayer(currentActivePlayer);
		gameStarted = false;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public Table getTableManager() {
		return tableManager;
	}

	public void setTableManager(Table tableManager) {
		this.tableManager = tableManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public int numberPlayers() {
		return playerManager.getPlayers().size();
	}

	public boolean checkWin() {
		if (!tableManager.getDeck().isEmpty() || !tableManager.isUsedTrumpCard()) {
			return false;
		}
		for (Player p : playerManager.getPlayers()) {
			if (p.getHand().getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;

	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public void setTurnManager(TurnManager turnManager) {
		this.turnManager = turnManager;
	}

	public void startGame() {
		tableManager.setDeck(new Deck());
		tableManager.getDeck().shuffle();
		tableManager.newTable();
//		this.usedTrumpCard = false;
		try {
			for (int i = 0; i < 8; i++) {
				for (Player p : playerManager.getPlayers()) {
					p.getHand().addCard(tableManager.getDeck().drawCard());
				}
			}
			tableManager.setTrumpCard(tableManager.getDeck().drawCard());
			turnManager.setCurrentActivePlayer(this.whoMakeFirstMove());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int whoMakeFirstMove() {
		return 0;
	}

	public void changeMove() {
		if (tableManager.isChangeMove()) {
			turnManager.nextPlayer();
			;

		}
 
	}

	public String getPlayerCard(int indexPlayer) {
		if (getPlayerByIndex(indexPlayer).getHand().getCardsInHand().isEmpty()) {
			return "null";
		}
		return getPlayerByIndex(indexPlayer).getHand().getCardsInHand().stream().map(Object::toString)
				.collect(Collectors.joining(","));
	}

	public String getPermission(int indexPlayer) {
		if (indexPlayer == turnManager.getCurrentActivePlayer()) {
			return "true";
		}
		return "false";
	}

	public String getTurn(int indexPlayer) {
		if (playerManager.getPlayers().get(indexPlayer).isAttacker()) {
			return "attacker";
		}
		return "defender";
	} 

	public Player getPlayerByIndex(int i) {
		return playerManager.getPlayers().get(i);
	}

	public Player getWinner() {
		for (Player p : playerManager.getPlayers()) {
			if (p.getHand().getCardsInHand().isEmpty()) {
				return p;
			}
		}
		return null;
	}

	public String getTextAction(int indexPlayer) {
		if (!tableManager.getTable().isEmpty()) {
			if (getPlayerByIndex(indexPlayer).isAttacker()) {
				return "Pass";
			} else if (tableManager.getDefendingCards().isEmpty()) {
				return "Discard";
			} else {
				return "Take";
			}
		}
		return "null";
	}

	private String getEndGame() {
		String data = "";
		boolean result = checkWin();
		System.out.println("Ket thuc luot - " + result);
		String winner_state = result ? getWinner().getName() : "null";
		System.out.println("gui du lieu ket thuc luot...");
		data += result + "," + winner_state;
		return data;
	}

	public String getGameStateAndPermission(int i) {
		// read_game#card1, card2, card3
		// ...#usedTrump#sizeDeck#endGame,nickname#hand#textAction#permission#turn
		String data = "read_game#"; 
		if (tableManager.isEmpty()) {
			data += "null#";
		} else {
			data += tableManager.getTable().stream().map(Object::toString).collect(Collectors.joining(",")) + "#";
		}
		data += String.valueOf(tableManager.isUsedTrumpCard()) + "#";
		data += tableManager.getDeck().getSizeOfDeck() + "#";
		data += getEndGame() + "#";
		data += getPlayerCard(i) + "#";
		data += getTextAction(i) + "#";
		data += getPermission(i) + "#";
		data += getTurn(i);
		System.out.println("data trong phan sendGameState... la: " + data);
		return data;
	}
	
	public String getGameStart(int i) { 
		String data = "start_game#" + getPlayerCard(i) + "#"
				+ getTableManager().getTrumpCard().toString() + "#"
				+ getPermission(i) + "#" + getTurn(i) + "#"
				+ String.valueOf(i);
		return data;
	}

}
