package game;

import game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represent's the players hand which will contains monster- and spellcards.
 */
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
