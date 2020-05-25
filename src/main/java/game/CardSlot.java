package game;

import game.cards.Card;
import lombok.Data;

@Data
public class CardSlot
{
    public enum Mode
    {
        ATTACK,
        DEFENSE,
        SET
    }
    /**
     * Contains a card reference.
     */
    private Card card;

    private Mode mode;
}
