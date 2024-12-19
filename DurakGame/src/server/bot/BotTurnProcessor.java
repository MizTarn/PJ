package server.bot;

import manager.durak.DurakManager;
import player.BotStrategy;

public class BotTurnProcessor {
	private BotStrategy botStrategy;
	private DurakManager gameManager;
	public BotTurnProcessor(BotStrategy bot, DurakManager gameManager) {
		this.setBot(bot);
		this.gameManager = gameManager;
	}
	public BotStrategy getBot() {
		return botStrategy;
	}
	public void setBot(BotStrategy bot) {
		this.botStrategy = bot;
	}
	
	public String defendAction() {
		try {
			return "drop_card#" + botStrategy.chooseCardToDefend(gameManager.getTableManager().getDefendingCards().peek(), 
					gameManager.getTableManager().getTrumpCard().getSuit()).toString() + "#1";
		}catch(Exception e) {
			System.out.println("Stack empty: " + e.getMessage());
		}
		return null;
		
	}
	
	public String passAction() {
		return "pass#null" +  "#1";
	}
	
	public String attackAction() {
		try {
			return "drop_card#" +  botStrategy.chooseCardToAttack() + "#1";
		} catch(Exception e) {
			System.out.println("Don't have card to attack " + e.getMessage());
		}
		return null;
		
	}
	
	public String discardAction() {
		return "discard#null" + "#1";
	}
	
	public String takeAction() {
		return "take_card#null" + "#1";
	}
	
}
