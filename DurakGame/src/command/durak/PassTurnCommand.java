package command.durak;

import command.Command;
import manager.PlayerManager;
import manager.durak.DurakTable;

public class PassTurnCommand implements Command {

	private DurakTable tableManager;

	public PassTurnCommand(PlayerManager<?> playerManager, DurakTable tableManager) {
		this.tableManager = tableManager;
	}

	@Override
	public boolean execute(String[] params) throws Exception {
		try {
			System.out.println("qua luot (pass turn connection)");
//			tableManager.getTable().clear();
//			tableManager.drawCardsForPlayers(playerManager.getPlayers());
//			for (Player2 player : playerManager.getPlayers()) {
//				player.setAttacker(false);
//			}
			tableManager.setChangeMove(true);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} 

}