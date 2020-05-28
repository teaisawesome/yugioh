package game.deck;

import game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * This class represent the player's deck which will contains monster- and spellscards.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Deck
{
    /**
     *  List of the cards which can be eighter monster- or spellcards.
     */
    private List<Card> cards;
}
