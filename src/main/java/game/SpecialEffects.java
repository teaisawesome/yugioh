package game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * This class handle the spell card's special effects in backed side.
 */
@Data
@Slf4j
public class SpecialEffects
{
    /**
     * Set effects for the two spell cards.
     * If the card is The Creators Blessing give 2000 lifePoints each player.
     * If the card is The Pot Of Greed: gives 2 cards to his owner's hand.
     *
     * @param gameState contains the current gameState instance which will be help to set the right values.
     * @param cardId the spell card's name in characters.
     */
    public void activateSpecialEffect(GameState gameState, String cardId)
    {
        if(cardId.equals("The Creators Blessing"))
        {
            int player1LifePoints = gameState.getPlayer(0).getLifePoints();
            int player2LifePoints = gameState.getPlayer(1).getLifePoints();

            gameState.getPlayer(0).setLifePoints(player1LifePoints + 2000);
            gameState.getPlayer(1).setLifePoints(player2LifePoints + 2000);

            gameState.getPlayer(gameState.getTurn()).getHand().getCardsInHand().remove(gameState.getCardByName(cardId));

            log.info("The Creators Blessing Spell Card Effect is Activated");
        }
        else if(cardId.equals("Pot Of Greed"))
        {
            gameState.getPlayer(gameState.getTurn()).getHand().getCardsInHand().remove(gameState.getCardByName(cardId));

            log.info("Pot Of Greed Spell Card Effect is Activated");
        }
    }
}
