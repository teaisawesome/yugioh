package game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Class represents one specific state of the game.
 */
@Data
@Slf4j
public class GameState
{
    private Player[] players;

    private int turn;

    public GameState()
    {
        players = new Player[2];

        this.turn = 0;
    }

}
