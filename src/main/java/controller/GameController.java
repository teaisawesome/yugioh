package controller;

import game.GameState;
import game.cards.MonsterCard;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameController
{
    @FXML
    private HBox player1Hand;

    @FXML
    private HBox player2Hand;

    private String player1Name;
    private String player2Name;
    private GameState gameState;

    public void initPlayerNames(String player1Name, String player2Name)
    {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        gameState.getPlayer(0).setName(player1Name);
        gameState.getPlayer(1).setName(player2Name);


        log.info("p1:" + player1Name + " p2:" + player2Name);

        // itt kell majd beállítani a labeleket a game view-hoz.
    }

    @FXML
    public void initialize()
    {
        gameState = new GameState();

        for (MonsterCard mc: gameState.getPlayer(0).getDeck().getMonsterCards())
        {
            Button button = new Button();
            button.setPrefWidth(100);
            button.setPrefHeight(140);
            button.setId(mc.getCardName());
            button.setStyle(
                    "-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/" + mc.getFrontFace()).toExternalForm()+"');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );

            player1Hand.getChildren().add(button);

            log.info("button created");

        }

        for (MonsterCard mc: gameState.getPlayer(1).getDeck().getMonsterCards())
        {
            Button button = new Button();
            button.setPrefWidth(100);
            button.setPrefHeight(140);
            button.setId(mc.getCardName());
            button.setStyle(
                    "-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/" + mc.getFrontFace()).toExternalForm()+"');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );

            player2Hand.getChildren().add(button);

            log.info("button created");

        }
    }

    public void endTurn(MouseEvent mouseEvent)
    {
        if (gameState.getTurn() == 0)
        {
            log.info("Kör:" + gameState.getPlayer(0).getName());

            for(Node component : player1Hand.getChildren())
            {
                if(component instanceof Button)
                {
                    component.setStyle("-fx-background-image: url('"+ getClass().getResource("/pictures/backface.jpg").toExternalForm()+"');" +
                    "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
                    );
                }
            }
            int i = 0;
            for(Node component : player2Hand.getChildren())
            {
                if(component instanceof Button)
                {
                    component.setStyle(
                            "-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/" + gameState.getPlayer(1).getDeck().getMonsterCards().get(i).getFrontFace()).toExternalForm()+"');\n" +
                                    "-fx-background-position: center;\n" +
                                    "-fx-background-size: cover;"
                    );
                    i++;
                }
            }

            gameState.setTurn(1);
        }
        else {
            log.info("Kör:" + gameState.getPlayer(1).getName());

            for(Node component : player2Hand.getChildren())
            {
                if(component instanceof Button)
                {
                    component.setStyle("-fx-background-image: url('"+ getClass().getResource("/pictures/backface.jpg").toExternalForm()+"');" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
                    );
                }
            }
            int i = 0;
            for(Node component : player1Hand.getChildren())
            {
                if(component instanceof Button)
                {
                    component.setStyle(
                            "-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/" + gameState.getPlayer(0).getDeck().getMonsterCards().get(i).getFrontFace()).toExternalForm()+"');\n" +
                                    "-fx-background-position: center;\n" +
                                    "-fx-background-size: cover;"
                    );
                    i++;
                }
            }


            gameState.setTurn(0);
        }
    }
}