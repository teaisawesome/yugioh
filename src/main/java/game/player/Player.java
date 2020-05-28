package game.player;

import game.Board;
import game.Hand;
import game.deck.Deck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * Class describe/represents a specific player from the game.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Entity
public class Player
{
    @Id
    @GeneratedValue
    private int id;

    /**
     * Player's name.
     */
    private String name;

    /**
     * Player's life points.
     */
    @Transient
    private int lifePoints;

    /**
     * Player's deck which is a Deck object and contains card list.
     */
    @Transient
    private Deck deck;

    /**
     * Contain Hand object which will be handle the player's handcards.
     */
    @Transient
    private Hand hand;

    /**
     * Player's board.
     */
    @Transient
    private Board board;
}
