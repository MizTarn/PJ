package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

import command.ParseData;
import manager.GameManager;
import player.Player;

public class NewConnection implements Runnable {
	private ConnectionManager connectionManager;
	private GameManager gameManager;
	private ParseData parseAndSendData;
	private boolean initializeGame = false;
	private int numberPlayer;

	public NewConnection() {
		connectionManager = new ConnectionManager();
		gameManager = new GameManager();
		parseAndSendData = new ParseData(gameManager);
		numberPlayer = 2;
	}

	// dependency injection
	public NewConnection(ConnectionManager connectionManager, GameManager gameManager,
			ParseData parseAndSendData, int numberPlayer) {
		this.connectionManager = connectionManager;
		this.gameManager = gameManager;
		this.parseAndSendData = parseAndSendData;
		this.numberPlayer = numberPlayer;
	}

	public void addPlayer(BufferedReader in, PrintWriter out, Player h) {
		connectionManager.addConnection(in, out);
		gameManager.getPlayerManager().addPlayer(h);
		connectionManager.sendDataToAll("waiting");
		System.out.println("WAITING....(Connection)");
	}

	public void addStreams(BufferedReader in, PrintWriter out, Player player) {
		if (gameManager.getPlayerManager().getPlayers().size() < numberPlayer) {
			this.addPlayer(in, out, player);
			
			if (gameManager.getPlayerManager().getPlayers().size() == numberPlayer) {
				connectionManager.sendDataToAll("started");
				gameManager.setGameStarted(true);
			} 
		} 
		else {
			connectionManager.sendData("join_session_failed", out);
		}
	}

	@Override
	public void run() {
		System.out.println("Bắt đầu chạy vòng lặp chính của trò chơi...");

		while (true) {
			try {
				if (gameManager.isGameStarted()) {
					if (!initializeGame) {
						gameManager.startGame();
						for (int i = 0; i < gameManager.getPlayerManager().getPlayers().size(); i++) {
							connectionManager.sendData(gameManager.getGameStart(i), connectionManager.getOutputs().get(i));
						}
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