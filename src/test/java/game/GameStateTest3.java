package game;

import game.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class GameStateTest3 {

    @Test
    void isGameOver()
    {
        Player player1 = new Player();
        Player player2 = new Player();

        player1.setLifePoints(0);
        player2.setLifePoints(375);

        GameState gameState =new GameState();

        Player[] playerList = new  Player[2];
        playerList[0] = player1;
        playerList[1] = player2;

        gameState.setPlayers(playerList);

        assertTrue(gameState.isGameOver());
    }
}