package game;

import game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Hand
{
    /**
     * Contains all of monster type cards in player hand.
     */
    private List<Card> cardsInHand;

    public Hand()
    {
        cardsInHand = new ArrayList<>();
    }
}
