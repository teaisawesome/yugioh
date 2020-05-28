package game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * This class contains 2x5 cards slots each in a list. Represents the player's playmat/board.
 */
@Data
@Slf4j
public class Board
{
    /**
     * This will represents the five monster card slots which is on the board and player can place cards to them.
     */
    private List<CardSlot> monsterCardSlots;

    /**
     * This will represents the five spell card slots which is on the board and player can place cards to them.
     */
    private List<CardSlot> spellCardSlots;
}
