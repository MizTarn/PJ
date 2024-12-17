package player;

import java.util.UUID;

public class BasePlayer {
	private String name;
	private String id;
	private String gui;
//	private HandForDurak hand;

	public BasePlayer(String name, String id) {
		this.name = name;
		this.id = id;
//		hand = new HandForDurak();
	}

	public BasePlayer() {

	}

	public BasePlayer(String name) {
		this.name = name;
		this.id = UUID.randomUUID().toString();
//		hand = new HandForDurak();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getGui() {
		return gui;
	}

	public void setGui(String gui) {
		this.gui = gui;
	}
}
