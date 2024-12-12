package card;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import ulti.Util;

public class Card implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Suit { 
		C, D, H, S;

		private static final Suit[] suits = Suit.values();

		public static Suit getSuit(int i) {
			return Suit.suits[i];
		}
	}

	public enum Value {
		Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(11), Queen(12), King(13),
		Ace(14);

		final int value;

		private Value(int value) {
			this.value = value;
		}

		private static final Value[] values = Value.values();

		public int getRank() {
			return value;
		}
	}

	private final Suit suit;
	private final Value value;

	public Card(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}

	public Card(String val) {
		String[] data = val.split("-");
		this.suit = Util.getSuit(data[0]);
		this.value = Util.getValue(data[1]);
	}

	public Suit getSuit() {
		return suit;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public String toString() {
		return suit + "-" + value;
	}

	public Image getImage() {
		String filename = Util.getImageString(this);
		System.out.println("name la : " + filename);
		return new Image(Card.class.getResourceAsStream(filename)); 
	}
	
	public Text getText() {
		return new Text(toString());
	}
}