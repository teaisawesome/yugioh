package controller;

import game.GameState;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameController
{
    private String player1Name;
    private String player2Name;
    private GameState gameState;

    public void initPlayerNames(String player1Name, String player2Name)
    {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        log.info("p1:" + player1Name + " p2:" + player2Name);

        // itt kell majd beállítani a labeleket a game view-hoz.
    }

    @FXML
    public void initialize()
    {
        gameState = new GameState();
    }

    public void endTurn(MouseEvent mouseEvent)
    {
        if (gameState.getTurn() == 0) {
            gameState.setTurn(1);
        } else {
            gameState.setTurn(0);
        }
        
        log.info("Kör:" + String.valueOf(gameState.getTurn()));
    }
}