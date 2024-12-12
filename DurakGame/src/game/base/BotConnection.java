package game.base;

import java.io.BufferedReader;
import java.io.PrintWriter;

import command.ParseData;
import manager.ConnectionManager;
import manager.GameManager;
import player.BotPlayer;
import player.Player;

public class BotConnection implements Runnable {
	private GameManager gameManager;
	private Player botPlayer;
	ParseData parseAndSendData;
	ConnectionManager connectionManager;
	Client client;
	private boolean initializeGame = false;
	private int numPlayer;

	public BotConnection(ConnectionManager connectionManager, GameManager gameManager,
			ParseData parseAndSendData, int numPlayer) {
		this.connectionManager = connectionManager;
		this.gameManager = gameManager;
		this.parseAndSendData = parseAndSendData;
		this.setNumPlayer(numPlayer);
		gameManager.getPlayerManager().addPlayer(botPlayer);
		
	}

	public void addPlayer(BufferedReader in, PrintWriter out, Player player) {
//		connectionManager.addConnection(in, out);
//		gameManager.getPlayerManager().addPlayer(player);
//		gameManager.getPlayerManager().addPlayer(botPlayer);
//		connectionManager.sendDataToAll("started");
//		gameManager.setGameStarted(true);

//			this.addPlayer(in, out, player);
			connectionManager.addConnection(in, out);
			gameManager.getPlayerManager().addPlayer(player);
			if (gameManager.getPlayerManager().getPlayers().size() == numPlayer) {
				connectionManager.sendDataToAll("started");
				gameManager.setGameStarted(true);
				System.out.println("start");
			} 
			
		else {
			connectionManager.sendData("join_session_failed", out);
		}

	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public Player getBotPlayer() {
		return botPlayer;
	}

	public void setBotPlayer(BotPlayer botPlayer) {
		this.botPlayer = botPlayer;
	}

	public int getNumPlayer() {
		return numPlayer;
	}

	public void setNumPlayer(int numPlayer) {
		this.numPlayer = numPlayer;
	}
	
	@Override
	public void run() {
		System.out.println("Bắt đầu chạy vòng lặp chính của trò chơi với bot ....");

		while (true) {
			try {
				if (gameManager.isGameStarted()) {
					
					if (!initializeGame) {
						gameManager.startGame();
						System.out.println("hello");
						System.out.println("number player: " + gameManager.getPlayerManager().getPlayers().size());
						System.out.println("player1: " + gameManager.getPlayerCard(0));
						System.out.println("player2: " + gameManager.getPlayerCard(1));
						connectionManager.sendData(gameManager.getGameStart(0), connectionManager.getOutputs().get(0));
						initializeGame = true;
					}
					
					int currentPlayer = gameManager.getTurnManager().getCurrentActivePlayer();
					System.out.println("Đang chờ dữ liệu từ người chơi: " + currentPlayer);

					String data = connectionManager.readData(currentPlayer);
					if (data != null && !data.isEmpty()) {
						System.out.println("Dữ liệu nhận được: " + data);
						parseAndSendData.parseData(data);
						gameManager.changeMove();
						for (int i = 0; i < gameManager.getPlayerManager().getPlayers().size(); i++) {
							connectionManager.sendData(gameManager.getGameStateAndPermission(i),
									connectionManager.getOutputs().get(i));
						}

					} else {
						System.out.println("Không nhận được dữ liệu từ người chơi: " + currentPlayer);
					}

					if (gameManager.checkWin()) {
						System.out.println("Trò chơi kết thúc!");
						connectionManager.sendDataToAll("end_game#");
						break;
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println("Lỗi trong vòng lặp chính: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}


}
