package controller;

import game.CardSlot;
import game.GameState;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.exceptions.BoardIsFullException;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Random;

@Slf4j
public class GameController
{
    @FXML
    public AnchorPane anchor;

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

    private HandEventHandler handEventHandler = new HandEventHandler();

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

            drawCard(1);

            addHandEventHandler(player2Hand.getChildren());

            removeHandEventHandler(player1Hand.getChildren());

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
            */
            drawCard(0);

            addHandEventHandler(player1Hand.getChildren());

            removeHandEventHandler(player2Hand.getChildren());

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
        removeHandEventHandler(player2Hand.getChildren());

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
                summonMonster(card, button, CardSlot.Mode.ATTACK);
            }
            else if(result.get() == defenseButton)
            {
                summonMonster(card, button, CardSlot.Mode.DEFENSE);
            }
        }
        else
        {
            alertDialog.setContentText("Choose a(n) action to " + card.getCardName() + " spell!");
            ButtonType activateButton = new ButtonType("Activate");
            ButtonType setSpellButton = new ButtonType("Set");
            ButtonType cancelButton = new ButtonType("Cancel");

            alertDialog.getButtonTypes().setAll(activateButton, setSpellButton, cancelButton);

            Optional<ButtonType> result = alertDialog.showAndWait();
            if(result.get() == setSpellButton)
            {
                setSpell(card, button);
            }
            if (result.get() == cancelButton) {
                alertDialog.close();
            }
        }
    }

    public void summonMonster(Card card, Button button, CardSlot.Mode mode)
    {
        try
        {

            if(!gameState.isBoardFullOfMonster(gameState.getPlayer(gameState.getTurn()).getBoard()))
            {
                int counter = 0;

                for(CardSlot slot : gameState.getPlayer(gameState.getTurn()).getBoard().getMonsterCardSlots())
                {
                    if (slot.getCard() == null)
                    {
                        int x = gameState.getTurn();

                        slot.setCard(card);

                        button.setPrefWidth(80);
                        button.setPrefHeight(120);

                        if(mode == CardSlot.Mode.ATTACK)
                        {
                            slot.setMode(CardSlot.Mode.ATTACK);
                        }
                        else
                        {
                            slot.setMode(CardSlot.Mode.DEFENSE);
                            button.getTransforms().add(new Rotate(90, 80/2, 120/2));
                        }

                        board.add(button,counter, x == 0 ? 2 : 1);
                        button.removeEventHandler(MouseEvent.ANY, handEventHandler);
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
        catch (Exception e)
        {
            log.error("Board is full!");
        }
    }



    public void setSpell(Card card, Button button)
    {
        try
        {
            if(!gameState.isBoardFullOfSpells(gameState.getPlayer(gameState.getTurn()).getBoard()))
            {
                int counter = 0;

                for(CardSlot slot : gameState.getPlayer(gameState.getTurn()).getBoard().getSpellCardSlots())
                {
                    if (slot.getCard() == null)
                    {
                        int x = gameState.getTurn();

                        slot.setCard(card);
                        slot.setMode(CardSlot.Mode.SET); // itt más mode kell a spellnek
                        button.setPrefWidth(80);
                        button.setPrefHeight(120);
                        board.add(button,counter, x == 0 ? 3 : 0);
                        button.removeEventHandler(MouseEvent.ANY, handEventHandler);
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
        catch (Exception e)
        {
            log.error("Board is full! Cannot take down more Spell Card!");
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

                button.addEventHandler(MouseEvent.ANY, handEventHandler);
                player1Hand.getChildren().add(button);
            }
            else
            {
                gameState.getPlayer(playerId).getHand().getCardsInHand().add(card);

                button.addEventHandler(MouseEvent.ANY, handEventHandler);
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

    public void addHandEventHandler(ObservableList<Node> childs)
    {
        for (Node node: childs)
        {
            node.addEventHandler(MouseEvent.ANY, handEventHandler);
        }
    }

    public void removeHandEventHandler(ObservableList<Node> childs)
    {
        for (Node node: childs)
        {
            node.removeEventHandler(MouseEvent.ANY, handEventHandler);
        }
    }

    class HandEventHandler implements EventHandler<MouseEvent>
    {
        @Override
        public void handle(MouseEvent mouseEvent)
        {
            if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                for (Card card : gameState.getPlayer(gameState.getTurn()).getHand().getCardsInHand())
                {
                    if(card.getCardName() == ((Button)mouseEvent.getSource()).getId())
                    {
                        if(card.getClass() == MonsterCard.class)
                        {
                            createDialogForCard(card, (Button) mouseEvent.getSource());
                        }
                        else if(card.getClass() == SpellCard.class)
                        {
                            createDialogForCard(card, (Button) mouseEvent.getSource());
                        }

                        break;
                    }
                }
            }
        }
    }
}