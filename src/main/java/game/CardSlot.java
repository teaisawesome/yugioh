package game;

import game.cards.Card;
import lombok.Data;

/**
 * A CardSlot represents a cardslot from the player's board. During the game the card's will be placed to them.
 */
@Data
public class CardSlot
{
    /**
     * Enum types for describe a card modes.
     * ATTACK - Attack position
     * DEFENSE - Defense Position
     * SET - Set Position
     */
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

    /**
     * Describe the card mode which placed to the  cardslot.
     */
    private Mode mode;
}
