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

        players[0] = Player.builder()
                .name("player1Name")
                .build();

        players[1] = Player.builder()
                .name("player2Name")
                .build();
    }

    public Card generateCard()
    {
        Card card = Card.builder()
                .cardName("Blue Eyes White Dragon")
                .frontFace("blue-eyes-front.png")
                .backFace("blue-eyes-back.png")
                .build();

        return card;
    }


    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

}
