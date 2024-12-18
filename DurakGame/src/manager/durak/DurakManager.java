package manager.durak;

import java.util.stream.Collectors;

import card.Deck;
import manager.IGameManager;
import manager.PlayerManager;
import manager.TurnManager;
import player.DurakPlayer;

public class DurakManager implements IGameManager<DurakPlayer>{
	private DurakTable tableManager;
	private PlayerManager<DurakPlayer>  playerManager;
	private TurnManager<DurakPlayer> turnManager;
	private boolean gameStarted;

	public DurakManager() {
		tableManager = new DurakTable();
		playerManager = new PlayerManager<DurakPlayer>();
		turnManager = new TurnManager<DurakPlayer>(playerManager);
		gameStarted = false;
	}

	public DurakManager(DurakTable tableManager, PlayerManager<DurakPlayer>  players, TurnManager<DurakPlayer> turnManager) {
		this.tableManager = tableManager;
		this.playerManager = players;
		this.turnManager = turnManager;
		gameStarted = false;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public DurakTable getTableManager() {
		return tableManager;
	} 

	public void setTableManager(DurakTable tableManager) {
		this.tableManager = tableManager;
	}

	public PlayerManager<DurakPlayer> getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager<DurakPlayer> playerManager) {
		this.playerManager = playerManager;
	}

	public int numberPlayers() {
		return playerManager.getPlayers().size();
	}

	public boolean checkWin() {
		if (!tableManager.getDeck().isEmpty() || !tableManager.isUsedTrumpCard()) {
			return false;
		}
		for (DurakPlayer p : playerManager.getPlayers()) {
			if (p.getHand().getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;

	}

	public TurnManager<DurakPlayer> getTurnManager() {
		return turnManager;
	}

	public void setTurnManager(TurnManager<DurakPlayer> turnManager) {
		this.turnManager = turnManager;
	}

	public void startGame() {
		tableManager.setDeck(new Deck());
		tableManager.getDeck().shuffle();
		tableManager.newTable();
		try {
			for (int i = 0; i < 8; i++) {
				for (DurakPlayer p : playerManager.getPlayers()) {
					System.out.println("player: " + p);
					p.getHand().addCard(tableManager.getDeck().drawCard());
				}
			}
			tableManager.setTrumpCard(tableManager.getDeck().drawCard());
			turnManager.setCurrentActivePlayer(this.whoMakeFirstMove());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int whoMakeFirstMove() {
		return 0;
	}

	public void changeMove() {
		if (tableManager.isChangeMove()) {
			turnManager.nextPlayer();
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

	public DurakPlayer getPlayerByIndex(int i) {
		return playerManager.getPlayers().get(i);
	}

	public DurakPlayer getWinner() {
		for (DurakPlayer p : playerManager.getPlayers()) {
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

	public String getEndGame() {
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
		String data = "";
		if(playerManager.getPlayers().get(i).getGui().equals("gui")) {
			 data = "read_game#";
		}
		else {
			data = "read_game_base#";
		}
		
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
		String data = "";
		if(playerManager.getPlayers().get(i).getGui().equals("gui")) {
			 data = "start_game#" + getPlayerCard(i) + "#" + getTableManager().getTrumpCard().toString() + "#"
					+ getPermission(i) + "#" + getTurn(i) + "#" + String.valueOf(i)+ "#" + String.valueOf(playerManager.getPlayers().size());
		}
		else {
			data = "start_game_base#" + getPlayerCard(i) + "#" + getTableManager().getTrumpCard().toString() + "#"
					+ getPermission(i) + "#" + getTurn(i) + "#" + String.valueOf(i) + "#" + String.valueOf(playerManager.getPlayers().size());
		}
		
		return data;
	}
	

}
