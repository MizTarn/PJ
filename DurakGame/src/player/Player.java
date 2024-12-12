package player;

import java.io.Serializable;
import java.util.UUID;

import card.HandForDurak;

public class Player extends BasePlayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean attacker;
	private boolean isDefender;

	public Player(String nickname, String session_id) {
		super(nickname, session_id);
	}

	public Player(String nickname) {
		super(nickname);
	} 

	public void setAttacker(boolean b) {
		attacker = b;
	}

	public boolean isAttacker() {
		return attacker;
	}

	public boolean isDefender() {
		return isDefender;
	}

	public void setDefender(boolean isDefender) {
		this.isDefender = isDefender;
	}

}