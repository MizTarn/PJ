package command;

import manager.PlayerManager;
import manager.Table;
import player.Player;

public class TakeCardCommand implements Command {

	private PlayerManager playerManager;
	private Table tableManager;

	public TakeCardCommand(PlayerManager playerManager, Table tableManager) {
		this.playerManager = playerManager;
		this.tableManager = tableManager;
	}

	@Override
	public boolean execute(String[] params) throws Exception { // take_card#card#idPlayer
		int idPlayer = Integer.parseInt(params[2]);
		System.out.println("Rút bài...");
		Player currentPlayer = playerManager.getPlayers().get(idPlayer);
		currentPlayer.getHand().addCards(tableManager.getTable());
		tableManager.clearTable();
		tableManager.getDefendingCards().clear();
		tableManager.drawCardsForPlayers(playerManager.getPlayers());
		tableManager.setChangeMove(true);
		
		return true;
 
	}

}