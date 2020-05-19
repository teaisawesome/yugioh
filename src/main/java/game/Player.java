package game;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Class describe/represents a specific player from the game.
 */
@Data
@Slf4j
@Builder
public class Player
{
    private String name;

    private Deck deck;

    private Hand hand;
}
