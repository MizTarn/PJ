package command.durak;

import java.util.HashMap;
import java.util.Map;

import command.Command;
import manager.durak.DurakManager;

public class ParseData {
	private DurakManager gameManager;
	private Map<String, Command> commandMap;

	public ParseData(DurakManager gameManager) {
		this.gameManager = gameManager;
		commandMap = new HashMap<>();
		initCommands();
	}

	private void initCommands() { 
		commandMap.put("take_card", new TakeCardCommand(gameManager.getPlayerManager(), gameManager.getTableManager()));
		commandMap.put("discard", new DiscardCommand(gameManager.getPlayerManager(), gameManager.getTableManager()));
		commandMap.put("drop_card", new DropCardCommand(gameManager.getPlayerManager(), gameManager.getTableManager()));
		commandMap.put("pass", new PassTurnCommand(gameManager.getPlayerManager(), gameManager.getTableManager()));
	}
 
	public void parseData(String data) { 
		try {
			String[] params = data.split("#"); 
			String operation = params[0];

			Command command = commandMap.get(operation);
			if (command != null) {
				command.execute(params);
			} else {
				System.out.println("Lệnh không hợp lệ: " + operation);
			}

		} catch (Exception e) {
			System.err.println("Lỗi khi xử lý dữ liệu: " + e.getMessage());
		}
	}

}