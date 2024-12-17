package command.baccarat;

import card.Card;
import card.HandForBaccarat;
import command.Command;
import manager.PlayerManager;
import manager.baccarat.BaccaratTable;
import player.BaccaratPlayer;

public class PlayCommand implements Command {
	private BaccaratTable tableManager;
	private PlayerManager<BaccaratPlayer> playerManager;
	
	public PlayCommand(PlayerManager<BaccaratPlayer> playerManager, BaccaratTable tableManager) {
		this.tableManager = tableManager;
		this.playerManager = playerManager;
	}
	@Override
	public boolean execute(String[] params) throws Exception {
		System.out.println("vao phan play command nay");
		int idPlayer = Integer.parseInt(params[2]);
		BaccaratPlayer p = playerManager.getPlayers().get(idPlayer);
		HandForBaccarat hand = p.getHand();
		for (Card c : hand.getCardsInHand()) {
			tableManager.getTable().add(c);
		}
		p.setPlay(true);
		tableManager.setChangeMove(true);
		
		return true;
	}

}
