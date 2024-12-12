package card;

import java.util.ArrayList;

public class HandForBaccarat extends BaseHand{
	public HandForBaccarat(ArrayList<Card> cardsInHand) {
		super(cardsInHand);
	}
	
	public HandForBaccarat() {
		super();
	}
	
	 public Card strongestCard() {
	        Card cd = getCardsInHand().get(0);
	        for (Card card : getCardsInHand()) {
	            // Sử dụng compare để so sánh giữa card và strongestCard
	            if (new CardComparator().compare(card, cd) > 0) {
	                cd = card;
	            }
	        }
	        return cd;
	    }
	
    // Hàm tính điểm ba cây
    public int score() {
        int score = 0; // điểm tính tổng 3 lá 
        for (Card card : getCardsInHand()) {
            if (card.getValue().ordinal() >10 ){
                if (card.getValue().ordinal() == 14){
                    score += 1;
                } else {
                    score += 10;
                }
            } else {
                score += card.getValue().ordinal();
            }
        }
        score = score - 10*(score/10);
        if (score == 0) score = 10;
        Card cd = strongestCard();
        int tie_break = 5*cd.getValue().ordinal() + cd.getSuit().ordinal(); // Dùng trong trường hợp có 2 người có tổng điểm bằng nhau

        return score * 100 + tie_break;

    }
}