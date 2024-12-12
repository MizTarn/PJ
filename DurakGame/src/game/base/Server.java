package game.base;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import command.ParseData;
import manager.ConnectionManager;
import manager.GameManager;
import manager.PlayerManager;
import manager.Table;
import manager.TurnManager;
import player.BotPlayer;
import player.Player;

public class Server {
	private static ServerSocket server;
	private static HashMap<String, NewConnection> connections = new HashMap<>();
	
	public static void main(String[] args) { 
		try {
			server = new ServerSocket(12345);
			// Khởi tạo các đối tượng phụ thuộc
			Table tableManager = new Table();
			PlayerManager playerManager = new PlayerManager();
			TurnManager turnManager = new TurnManager(playerManager);
			ConnectionManager connectionManager = new ConnectionManager();
			// Khởi tạo GameManager với Dependency Injection 
			GameManager gameManager = new GameManager(tableManager, playerManager, turnManager);

			// Khởi tạo ParseAndSendData 
			ParseData parseAndSendData = new ParseData(gameManager);
			
			BotPlayer botPlayer = new BotPlayer("bot");

			while (true) {
				Socket socket = server.accept();
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				while (true) {
					String[] data = in.readLine().split("#");
					String operation = data[0];
					if (Objects.equals(operation, "create_session")) {
						String nickname = data[1];
						String session_id = data[2];
						Player host = new Player(nickname, session_id);
						NewConnection connection = new NewConnection(connectionManager, gameManager, parseAndSendData,
								2);
						connection.addPlayer(in, out, host);
						connections.put(host.getId(), connection);
						Thread con = new Thread(connection);
						con.setDaemon(true);
						con.start();
						break;
					} else if (Objects.equals(operation, "join_session")) {
						String nickname = data[1];
						String session_id = data[2]; 
						Player member = new Player(nickname); 
						if (connections.containsKey(session_id)) {
							connections.get(session_id).addStreams(in, out, member);
						} else {
							out.println("join_session_failed");
						}
						break; 
					} 
					else if (Objects.equals(operation, "play_with_bot")) {
						Player host = new Player("a1", "01");
						BotConnection connection = new BotConnection(connectionManager, gameManager, parseAndSendData,
								1);
						connection.addPlayer(in, out, host);
						Thread con = new Thread(connection);
						con.setDaemon(true);
						con.start();
					}
					
					
					else {
						socket.close();
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("Error occurred in main in ServerHandler: " + ex.toString());
			ex.printStackTrace();
		}
	}
}