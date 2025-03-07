package player;

import card.HandForBaccarat;

public class BaccaratPlayer extends BasePlayer {

	
	private HandForBaccarat hand;
	private boolean isPlay;
	
	public BaccaratPlayer(String nickname, String session_id) {
		super(nickname, session_id);
		hand = new HandForBaccarat();
		this.isPlay = false;
	}

	public BaccaratPlayer(String nickname) {
		super(nickname);
		hand = new HandForBaccarat();
		this.isPlay = false;
	}
	
	public HandForBaccarat getHand() {
		return hand;
	}

	public void setHand(HandForBaccarat hand) {
		this.hand = hand;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
	
}
