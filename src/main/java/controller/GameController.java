package controller;

import com.google.common.collect.Lists;
import game.Board;
import game.CardSlot;
import game.GameState;
import game.cards.Card;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GameController
{
    @FXML
    private HBox player1Hand;

    @FXML
    private HBox player2Hand;

    @FXML
    private GridPane board;

    @FXML
    private Button teszt;

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
    /*
        for (Card mc: gameState.getPlayer(0).getDeck().getCards())
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

            button.setOnMouseClicked(e-> {
                board.add(button, 0, 0);
            });

            player1Hand.getChildren().add(button);

            log.info(String.valueOf(mc.getClass()));
        }
*/
        gameState.initHandCards(player1Hand, player2Hand);

        for (Card mc: gameState.getPlayer(1).getDeck().getCards())
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
        Board b = new Board();

        CardSlot slot = new CardSlot();

        slot.setCard(new Button("teszt"));

        List<CardSlot> list = Lists.newArrayList(
            slot
        );

        b.setSlots(list);


        board.add(b.getSlots().get(0).getCard(), 0, 0);

        b.getSlots().get(0).getCard().setPrefWidth(80);
        b.getSlots().get(0).getCard().setPrefHeight(120);
        b.getSlots().get(0).getCard().setStyle("-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/blue-eyes-white-dragon-faceup.jpg").toExternalForm()+"');" +
                "-fx-background-position: center;\n" +
                "-fx-background-size: cover;"
        );

        Rotate r = new Rotate();
        r.setAngle(90);
        r.setPivotX(100);
        r.setPivotY(300);
        b.getSlots().get(0).getCard().getTransforms().add(r);
    }

    public void endTurn(MouseEvent mouseEvent)
    {
        if (gameState.getTurn() == 0)
        {
            log.info("Kör:" + gameState.getPlayer(0).getName());

            /*
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
            }*/

            gameState.setTurn(1);
        }
        else {
            log.info("Kör:" + gameState.getPlayer(1).getName());

            /*
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
             */

            gameState.setTurn(0);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void tesztClick(MouseEvent mouseEvent)
    {
        gameState.getBoard().getSlots().get(0).setCard(teszt);

        board.add(teszt,0,0);
    }
}