package game;

import com.google.common.collect.Lists;
import game.cards.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest2 {

    @Test
    void isBoardFullOfMonster()
    {
        Board board = new Board();

        board.setMonsterCardSlots(Lists.newArrayList());


        Card card = new Card();

        for (int i = 0; i < 5; i++) {
            board.getMonsterCardSlots().add(new CardSlot());
            board.getMonsterCardSlots().get(i).setCard(card);
        }

        GameState gameState = new GameState();

        assertEquals(true, gameState.isBoardFullOfMonster(board));

        assertNotEquals(false, gameState.isBoardFullOfMonster(board));

        assertTrue(gameState.isBoardFullOfMonster(board));

    }
}