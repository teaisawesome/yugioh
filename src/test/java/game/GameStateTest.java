package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {


    @Test
    void setTurn()
    {
        GameState gameState = new GameState();

        gameState.setTurn(1);


        assertEquals(1, gameState.getTurn());

    }

}