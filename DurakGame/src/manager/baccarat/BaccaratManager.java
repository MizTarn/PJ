package manager.baccarat;

import java.util.stream.Collectors;

import card.Deck;
import manager.IGameManager;
import manager.PlayerManager;
import manager.TurnManager;
import player.BaccaratPlayer;

public class BaccaratManager implements IGameManager<BaccaratPlayer> {
	private BaccaratTable tableManager;
	private PlayerManager<BaccaratPlayer> playerManager;
	private TurnManager<BaccaratPlayer> turnManager;
	private boolean gameStarted;

	public BaccaratManager() {
		tableManager = new BaccaratTable();
		playerManager = new PlayerManager<BaccaratPlayer>();
		turnManager = new TurnManager<BaccaratPlayer>(playerManager);
		gameStarted = false;
	}

	public BaccaratManager(BaccaratTable tableManager, PlayerManager<BaccaratPlayer> players,
			TurnManager<BaccaratPlayer> turnManager) {
		this.tableManager = tableManager;
		this.playerManager = players;
		this.turnManager = turnManager;
		gameStarted = false;
	}

	public BaccaratTable getTableManager() {
		return tableManager;
	}

	public void setTableManager(BaccaratTable tableManager) {
		this.tableManager = tableManager;
	}

	@Override
	public boolean isGameStarted() {
		// TODO Auto-generated method stub
		return this.gameStarted;
	}

	@Override
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;

	}

	@Override
	public PlayerManager<BaccaratPlayer> getPlayerManager() {
		return this.playerManager;
	}

	@Override
	public void setPlayerManager(PlayerManager<BaccaratPlayer> playerManager) {
		this.playerManager = playerManager;

	}

	@Override
	public int numberPlayers() {
		// TODO Auto-generated method stub
		return this.playerManager.getPlayers().size();
	}

	@Override
	public boolean checkWin() {
		System.out.println("luot choi la : " + turnManager.getCurrentActivePlayer());
		for (BaccaratPlayer p : playerManager.getPlayers()) {
			if (!p.isPlay()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public TurnManager<BaccaratPlayer> getTurnManager() {
		// TODO Auto-generated method stub
		return this.turnManager;
	}

	@Override
	public void setTurnManager(TurnManager<BaccaratPlayer> turnManager) {
		this.turnManager = turnManager;

	}

	@Override
	public void startGame() {
		tableManager.setDeck(new Deck());
		tableManager.getDeck().shuffle();
		tableManager.newBaccaratTable();
		try {
			for (int i = 0; i < 3; i++) {
				for (BaccaratPlayer p : playerManager.getPlayers()) {
					System.out.println("player: " + p);
					p.getHand().addCard(tableManager.getDeck().drawCard());
				}
			}
			turnManager.setCurrentActivePlayer(this.whoMakeFirstMove());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public int whoMakeFirstMove() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void changeMove() {
		if (tableManager.isChangeMove()) {
			turnManager.nextPlayer();
		}

	}

	@Override
	public String getPlayerCard(int indexPlayer) {
		if (this.playerManager.getPlayers().get(indexPlayer).getHand().getCardsInHand().isEmpty()) {
			return "null";
		}
		return this.playerManager.getPlayers().get(indexPlayer).getHand().getCardsInHand().stream()
				.map(Object::toString).collect(Collectors.joining(","));
	}

	@Override
	public String getEndGame() {
		String data = "";
		boolean result = checkWin();
		System.out.println("Ket thuc luot - " + result);
		String winner_state = result ? getWinner().getName() : "null";
		System.out.println("gui du lieu ket thuc luot...");
		data += result + "," + winner_state;
		return data;
	}

	@Override
	public String getGameStateAndPermission(int i) {
		String data = "";
		if (playerManager.getPlayers().get(i).getGui().equals("gui")) {
			data = "read_game_baccarat_gui#";
		} else {
			data = "read_game_baccarat_base#";
		}
		data += tableManager.getTable().stream().map(Object::toString).collect(Collectors.joining(",")) + "#"
				+ getEndGame() + "#" + getPlayerCard(i) + "#" + getPermission(i) + "#" + String.valueOf(i);
		return data;
	}

	@Override
	public String getGameStart(int i) {
		String data;
		if (playerManager.getPlayers().get(i).getGui().equals("gui")) {
			data = "start_game_baccarat_gui#";
		} else {
			data = "start_game_baccarat_base#";
		}
		data += getPlayerCard(i) + "#" + getPermission(i) + "#" + String.valueOf(i) + "#"
				+ String.valueOf(playerManager.getPlayers().get(i).getHand().getScore());

		return data;
	}

	public BaccaratPlayer getWinner() {
		System.out.println("chay vao getWinner");
		BaccaratPlayer choose = playerManager.getPlayers().get(0);
		for (BaccaratPlayer p : playerManager.getPlayers()) {
			if (p.getHand().getScore() > choose.getHand().getScore()) {
				choose = p;
			}
		}
		System.out.println("da chay xong getWinner, nguoi choi win la + " + choose.getName());
		return choose;
	}

	public String getPermission(int indexPlayer) {
		if (indexPlayer == turnManager.getCurrentActivePlayer()) {
			return "true";
		}
		return "false";
	}

}
