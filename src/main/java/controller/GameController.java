package controller;

import game.CardSlot;
import game.GameState;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.exceptions.BoardIsFullException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Random;

@Slf4j
public class GameController {
    @FXML
    private HBox player1Hand;

    @FXML
    private HBox player2Hand;

    @FXML
    private GridPane board;

    @FXML
    private Button nextPhaseButton;

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
    }

    @FXML
    public void initialize()
    {
        gameState = new GameState();

        initHandCards();
    }

    public void endTurn(MouseEvent mouseEvent)
    {
        if (gameState.getTurn() == 0)
        {
            log.info("Kör:" + gameState.getPlayer(0).getName());

            drawCard(0);

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
            }*/
            drawCard(1);

            gameState.setTurn(0);
        }

        gameState.setPhase(GameState.Phases.MAIN);
        nextPhaseButton.setDisable(false);
        log.info(gameState.getPhase().toString());
    }


    public void initHandCards()
    {
        drawCard(0);
        drawCard(1);

        log.info("Player started hand created!");
    }

    public void setStyleForButton(Button button, Card card)
    {
        if (card.getClass() == MonsterCard.class)
        {
            button.setStyle(
                    "-fx-background-image: url('" + getClass().getResource("/pictures/monsters/" + card.getFrontFace()).toExternalForm() + "');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );
        }
        else if (card.getClass() == SpellCard.class)
        {
            button.setStyle(
                    "-fx-background-image: url('" + getClass().getResource("/pictures/spells/" + card.getFrontFace()).toExternalForm() + "');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );
        }
    }




    public void createDialogForCard(Card card, Button button)
    {
        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        alertDialog.setTitle("Yu-Gi-OH! Action!");

        if (card.getClass() == MonsterCard.class)
        {
            alertDialog.setContentText("Choose a(n) action!");
            ButtonType summonButton = new ButtonType("Summon Monster");
            ButtonType defenseButton = new ButtonType("Set to Defense Mode");
            ButtonType cancelButton = new ButtonType("Cancel");

            alertDialog.getButtonTypes().setAll(summonButton, defenseButton, cancelButton);

            Optional<ButtonType> result = alertDialog.showAndWait();

            if (result.get() == summonButton)
            {
                try
                {
                    summonMonster(card, button);
                } catch (BoardIsFullException e)
                {
                    log.error("Board is FULL!");
                }
            }
        }
        else
        {
            alertDialog.setContentText("Choose a(n) action to " + card.getCardName() + " spell!");
            ButtonType buttonTypeOne = new ButtonType("Activate");
            ButtonType buttonTypeTwo = new ButtonType("Set");
            ButtonType cancelButton = new ButtonType("Cancel");

            alertDialog.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, cancelButton);

            Optional<ButtonType> result = alertDialog.showAndWait();
            if (result.get() == cancelButton) {
                alertDialog.close();
            }
        }
    }

    public void summonMonster(Card card, Button button) throws BoardIsFullException
    {

        if(!gameState.isBoardFullOfMonster())
        {
            int counter = 0;

            for(CardSlot slot : gameState.getBoard().getMonsterCardSlots())
            {
                if (slot.getCard() == null)
                {
                    int x = gameState.getTurn();

                    slot.setCard(card);
                    slot.setMode(CardSlot.Mode.ATTACK);
                    button.setPrefWidth(80);
                    button.setPrefHeight(120);
                    button.setOnMouseClicked(e->{
                        log.info("NOMVA");
                    });
                    board.add(button,counter, x == 0 ? 2 : 1);
                    break;
                }
                counter++;
            }
        }
        else
        {
            throw new BoardIsFullException();
        }
    }

    public void drawCard(int playerId)
    {

        int playerDeckLength = gameState.getPlayer(playerId).getDeck().getCards().size();

        if(playerDeckLength != 0)
        {
            Card card = gameState.getPlayer(playerId).getDeck().getCards().get(new Random().nextInt(playerDeckLength) + 0);

            gameState.getPlayer(playerId).getDeck().getCards().remove(card);

            Button button = new Button();
            button.setPrefWidth(100);
            button.setPrefHeight(140);
            button.setId(card.getCardName());

            setStyleForButton(button, card);

            if(playerId == 0)
            {

                gameState.getPlayer(playerId).getHand().getCardsInHand().add(card);

                player1Hand.getChildren().add(button);
            }
            else
            {
                gameState.getPlayer(playerId).getHand().getCardsInHand().add(card);

                player2Hand.getChildren().add(button);
            }

            log.info("button created");
        }
        else
        {
            log.info("Nincs több kártyám: " + gameState.getPlayer(playerId).getName());
        }
    }

    public void nextPhase(MouseEvent mouseEvent)
    {
        if(gameState.getPhase() == GameState.Phases.MAIN)
        {
            gameState.setPhase(GameState.Phases.BATTLE);
            nextPhaseButton.setDisable(true);

            log.info(gameState.getPhase().toString());
        }
        else
        {
            gameState.setPhase(GameState.Phases.MAIN);
            nextPhaseButton.setDisable(false);

            log.info(gameState.getPhase().toString());
        }
    }
}