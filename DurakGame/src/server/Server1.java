package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import command.baccarat.BaccaratParseData;
import command.durak.DurakParseData;
import manager.PlayerManager;
import manager.TurnManager;
import manager.baccarat.BaccaratManager;
import manager.baccarat.BaccaratTable;
import manager.durak.DurakManager;
import manager.durak.DurakTable;
import player.BaccaratPlayer;
import player.DurakPlayer;
import server.bot.BotConnection;

public class Server1 {
	private static ServerSocket server;
	private static HashMap<String, ServerConnection> connections = new HashMap<>();

	public static void main(String[] args) {
		try {
			server = new ServerSocket(12345);
			// Khởi tạo các đối tượng phụ thuộc

			int numberPlayer = 0;

			while (true) {
				Socket socket = server.accept();
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				while (true) {
					String[] data = in.readLine().split("#");
					String operation = data[0];
					if (operation != null) {
						if (Objects.equals(operation, "create_session")) {
							DurakTable tableManager = new DurakTable();
							PlayerManager<DurakPlayer> playerManager = new PlayerManager<DurakPlayer>();
							TurnManager<DurakPlayer> turnManager = new TurnManager<DurakPlayer>(playerManager);
							ConnectionManager connectionManager = new ConnectionManager();
							DurakManager gameManager = new DurakManager(tableManager, playerManager, turnManager);
							DurakParseData parseAndSendData = new DurakParseData(gameManager);
							String nickname = data[1];
							String session_id = data[2];
							String gui = data[3];
							System.out.println("data3 la :" + data[3]);
							DurakPlayer host = new DurakPlayer(nickname, session_id);
							host.setGui(gui);
							ServerConnection connection = new ServerConnection(connectionManager, gameManager,
									parseAndSendData, numberPlayer);
							connection.addPlayer(in, out, host, gameManager);
							connections.put(host.getId(), connection);
							Thread con = new Thread(connection);
							con.setDaemon(true);
							con.start();
							break;
						} else if (Objects.equals(operation, "join_session")) {
							String nickname = data[1];
							String session_id = data[2];
							String gui = data[3];
							System.out.println("data3 la :" + data[3]);
							DurakPlayer member = new DurakPlayer(nickname);
							member.setGui(gui);
							if (connections.containsKey(session_id)) {
								connections.get(session_id).addStreams(in, out, member);
							} else {
								out.println("join_session_failed");
							}
							break;
						} else if (Objects.equals(operation, "play_with_bot")) {
							DurakTable tableManager = new DurakTable();
							PlayerManager<DurakPlayer> playerManager = new PlayerManager<DurakPlayer>();
							TurnManager<DurakPlayer> turnManager = new TurnManager<DurakPlayer>(playerManager);
							ConnectionManager connectionManager = new ConnectionManager();
							DurakManager gameManager = new DurakManager(tableManager, playerManager, turnManager);
							DurakParseData parseAndSendData = new DurakParseData(gameManager);
							DurakPlayer host = new DurakPlayer(data[1]);
							host.setGui("gui");
							BotConnection connection = new BotConnection(connectionManager, gameManager,
									parseAndSendData, 1);
							connection.add(in, out, host);
							Thread con = new Thread(connection);
							con.setDaemon(true);
							con.start();
							break;
						} else if (Objects.equals(operation, "create_session_baccarat")) {
							BaccaratTable tableManager = new BaccaratTable();
							PlayerManager<BaccaratPlayer> playerManager = new PlayerManager<BaccaratPlayer>();
							TurnManager<BaccaratPlayer> turnManager = new TurnManager<BaccaratPlayer>(playerManager);
							ConnectionManager connectionManager = new ConnectionManager();
							BaccaratManager gameManager = new BaccaratManager(tableManager, playerManager, turnManager);
							BaccaratParseData parseAndSendData = new BaccaratParseData(gameManager);
							String nickname = data[1];
							String session_id = data[2];
							String gui = data[3];
							BaccaratPlayer host = new BaccaratPlayer(nickname, session_id);
							host.setGui(gui);
							ServerConnection connection = new ServerConnection(connectionManager, gameManager,
									parseAndSendData, numberPlayer);
							connection.addPlayer(in, out, host, gameManager);
							connections.put(host.getId(), connection);
							Thread con = new Thread(connection);
							con.setDaemon(true);
							con.start();
							break;
						} else if (Objects.equals(operation, "join_session_baccarat")) {
//							joinSession(data, nickname -> new BaccaratPlayer(nickname), in, out);
							String nickname = data[1];
							String session_id = data[2];
							String gui = data[3];
							BaccaratPlayer member = new BaccaratPlayer(nickname);
							member.setGui(gui);
							if (connections.containsKey(session_id)) {
								connections.get(session_id).addStreams(in, out, member);
							} else {
								out.println("join_session_failed");
							}
							break;

						} else {
							numberPlayer = Integer.parseInt(operation);
						}
					} else {
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