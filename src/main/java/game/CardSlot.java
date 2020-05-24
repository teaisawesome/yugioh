package game;

import game.cards.Card;
import lombok.Data;

@Data
public class CardSlot
{
    /**
     * Contains a card reference.
     */
    private Card card;
}
