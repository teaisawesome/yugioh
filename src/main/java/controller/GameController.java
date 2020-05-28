package controller;

import game.CardSlot;
import game.GameState;
import game.SpecialEffects;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.exceptions.BoardIsFullException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 *
 *
 */
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

    @FXML
    private ImageView cardView;

    @FXML
    private Label player1LifePoints;

    @FXML
    private Label player2LifePoints;

    @FXML
    private Button endButton;

    @FXML
    private Label player1Name;

    @FXML
    private Label player2Name;


    //private String player1Name;

    //private String player2Name;

    private GameState gameState;

    private SpecialEffects specialEffects;

    private Button clickedButton;

    private List<Button> usedTheirAction = new ArrayList<>();

    private HandEventHandler handEventHandler = new HandEventHandler();
    private BattleEventHandler battleEventHandler = new BattleEventHandler();
    private ShowEventHandler showEventHandler = new ShowEventHandler();
    private AttackEventHandler attackEventHandler = new AttackEventHandler();

    public void initPlayerNames(String player1Name, String player2Name)
    {
        //this.player1Name = player1Name;
        //this.player2Name = player2Name;

        gameState.persistPlayerNames(player1Name, player2Name);

        this.player1Name.setText(gameState.getPlayerDao().find(1).get().getName());
        this.player2Name.setText(gameState.getPlayerDao().find(2).get().getName());
        //this.player2Name.setText(String.valueOf(gameState.getPlayerDao().find(2)));

        log.info("p1:" + player1Name + " p2:" + player2Name);
    }

    @FXML
    public void initialize()
    {
        gameState = new GameState();

        specialEffects = new SpecialEffects();

        initPlayersLifePoints();

        initHandCards();

        cardView.setImage(new Image(getClass().getResource("/pictures/backface.jpg").toExternalForm()));
    }
    public void initPlayersLifePoints()
    {
        player1LifePoints.setText(String.valueOf(gameState.getPlayer(0).getLifePoints()));
        player2LifePoints.setText(String.valueOf(gameState.getPlayer(1).getLifePoints()));
    }

    public void endTurn(MouseEvent mouseEvent)
    {
        if (gameState.getTurn() == 0)
        {
            log.info("Kör:" + gameState.getPlayer(0).getName());

            drawCard(1);

            addHandEventHandler(player2Hand.getChildren());

            removeHandEventHandler(player1Hand.getChildren());

            removeEventHandlerByRow(1,attackEventHandler);

            usedTheirAction.clear();

            gameState.setTurn(1);
        }
        else {
            log.info("Kör:" + gameState.getPlayer(1).getName());

            drawCard(0);

            addHandEventHandler(player1Hand.getChildren());

            removeHandEventHandler(player2Hand.getChildren());

            removeEventHandlerByRow(2,attackEventHandler);

            usedTheirAction.clear();

            gameState.setTurn(0);
        }

        gameState.setPhase(GameState.Phases.MAIN);
        nextPhaseButton.setDisable(false);
        removeBattleEventHandler(board.getChildren());
        log.info(gameState.getPhase().toString());
    }


    public void initHandCards()
    {
        drawCard(0);
        drawCard(1);
        removeHandEventHandler(player2Hand.getChildren());
        removeBattleEventHandler(board.getChildren());

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
            ButtonType cancelButton = new ButtonType("Cancel");

            alertDialog.getButtonTypes().setAll(activateButton, cancelButton);

            Optional<ButtonType> result = alertDialog.showAndWait();
            if(result.get() == activateButton)
            {
                log.info("Aktiváltam!");

                specialEffects.activateSpecialEffect(gameState, card.getCardName());

                if(card.getCardName().equals("Pot Of Greed"))
                {
                    if(gameState.getTurn() == 0)
                    {
                        drawCard(0);
                        drawCard(0);
                    }
                    else
                    {
                        drawCard(1);
                        drawCard(1);
                    }
                }

                log.info(String.valueOf(card.getCardName()));

                gameState.getPlayer(gameState.getTurn()).getHand().getCardsInHand().remove(card);

                if(gameState.getTurn() == 0)
                {
                    player1Hand.getChildren().remove(clickedButton);
                }
                else
                {
                    player2Hand.getChildren().remove(clickedButton);
                }

                initPlayersLifePoints();
            }
            if (result.get() == cancelButton) {
                alertDialog.close();
            }
        }
    }

    public void battlePhaseDialog(Card card, CardSlot.Mode mode)
    {
        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        alertDialog.setTitle("Yu-Gi-OH! Action!");
        alertDialog.setContentText("Choose a(n) action!");

        if(card.getClass() == MonsterCard.class)
        {
            if(mode == CardSlot.Mode.ATTACK)
            {
                ButtonType attackButton = new ButtonType("Attack!");
                ButtonType flipToDefenseButton = new ButtonType("Flip to Defense Position");
                ButtonType cancelButton = new ButtonType("Cancel");

                alertDialog.getButtonTypes().setAll(attackButton, flipToDefenseButton, cancelButton);

                Optional<ButtonType> result = alertDialog.showAndWait();

                if (result.get() == attackButton)
                {
                    gameState.setClickedCard(card);

                    if(gameState.getTurn() == 0)
                    {
                        if(gameState.isEnemyBoardEmpty())
                        {
                            log.info("Direct HIT!");
                            gameState.directHit((MonsterCard)card);
                            initPlayersLifePoints();

                            if(gameState.isGameOver())
                            {
                                setGameOverDialog();
                            }
                            clickedButton.removeEventHandler(MouseEvent.ANY,battleEventHandler); // OK
                        }
                        else {
                            declareAnAttackDialog();
                            addEventHandlerByRow(1, attackEventHandler);
                            removeEventHandlerByRow(2, battleEventHandler);
                            removeEventHandlerByRow(3, battleEventHandler);
                        }

                    }
                    else
                    {
                        if(gameState.isEnemyBoardEmpty())
                        {
                            log.info("Direct HIT!");
                            gameState.directHit((MonsterCard)card);
                            initPlayersLifePoints();

                            if(gameState.isGameOver())
                            {
                                setGameOverDialog();
                            }
                            clickedButton.removeEventHandler(MouseEvent.ANY,battleEventHandler); // OK
                        }
                        else
                        {
                            declareAnAttackDialog();
                            addEventHandlerByRow(2,attackEventHandler);
                            removeEventHandlerByRow(0, battleEventHandler);
                            removeEventHandlerByRow(1, battleEventHandler);
                        }
                    }
                }
                else if(result.get() == flipToDefenseButton)
                {
                    clickedButton.getTransforms().add(new Rotate(90, 80/2, 120/2));
                    gameState.getPlayerBoardCardSlot(clickedButton.getId(), gameState.getPlayer(gameState.getTurn())).setMode(CardSlot.Mode.DEFENSE);
                    clickedButton.removeEventHandler(MouseEvent.ANY,battleEventHandler);

                    usedTheirAction.add(clickedButton);

                    usedTheirAction.forEach(e -> e.removeEventHandler(MouseEvent.ANY, battleEventHandler));
                    log.info("Megfordítottam DEF-be!");
                }
                else
                {
                    alertDialog.close();
                }
            }
            else
            {
                ButtonType flipButton = new ButtonType("Flip to Attack!");
                ButtonType cancelButton = new ButtonType("Cancel");

                alertDialog.getButtonTypes().setAll(flipButton, cancelButton);

                Optional<ButtonType> result = alertDialog.showAndWait();

                if (result.get() == flipButton)
                {
                    clickedButton.getTransforms().add(new Rotate(-90, 80/2, 120/2));
                    gameState.getPlayerBoardCardSlot(clickedButton.getId(), gameState.getPlayer(gameState.getTurn())).setMode(CardSlot.Mode.ATTACK);

                    clickedButton.removeEventHandler(MouseEvent.ANY,battleEventHandler);
                    log.info("Flip to Attack!");

                    usedTheirAction.add(clickedButton);

                    usedTheirAction.forEach(e -> e.removeEventHandler(MouseEvent.ANY, battleEventHandler));
                }
                else if(result.get() == cancelButton)
                {
                    alertDialog.close();
                }
            }
        }
    }

    public void setGameOverDialog()
    {

        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        if(gameState.getPlayer(0).getLifePoints() <= 0)
        {
            player1LifePoints.setText("0");
            alertDialog.setContentText("Gratulálok " + player2Name.getText() + " megnyerted a játékot!");
        }
        else if(gameState.getPlayer(1).getLifePoints() <= 0)
        {
            player2LifePoints.setText("0");
            alertDialog.setContentText("Gratulálok " + player1Name.getText() + " megnyerted a játékot!");
        }
        ButtonType backtoMainMenu = new ButtonType("Vissza a főmenübe");

        alertDialog.getButtonTypes().setAll(backtoMainMenu);

        Optional<ButtonType> result = alertDialog.showAndWait();

        if(result.get() == backtoMainMenu)
        {
            endButton.fire();
        }
    }

    public void boardIsFullDialog()
    {
        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        alertDialog.setTitle("Yu-Gi-OH!");
        alertDialog.setContentText("Your board is FULL!");
        ButtonType cancelButton = new ButtonType("Cancel");

        alertDialog.getButtonTypes().setAll(cancelButton);

        Optional<ButtonType> result = alertDialog.showAndWait();

        if(result.get() == cancelButton)
        {
            alertDialog.close();
        }
    }
    public void declareAnAttackDialog()
    {
        Alert alertDialog = new Alert(Alert.AlertType.NONE);
        alertDialog.setTitle("Yu-Gi-OH!");
        alertDialog.setContentText("Choose a target monster!");
        ButtonType okButton = new ButtonType("OK");

        alertDialog.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alertDialog.showAndWait();

        if(result.get() == okButton)
        {
            alertDialog.close();
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
                boardIsFullDialog();
                throw new BoardIsFullException();
            }
        }
        catch (Exception e)
        {
            log.error("Board is full!");
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
            button.setPrefWidth(95);
            button.setPrefHeight(140);
            button.setId(card.getCardName());

            setStyleForButton(button, card);

            if(playerId == 0)
            {
                gameState.getPlayer(playerId).getHand().getCardsInHand().add(card);

                button.addEventHandler(MouseEvent.ANY, handEventHandler);
                addShowEventHandler(button);
                player1Hand.getChildren().add(button);
            }
            else
            {
                gameState.getPlayer(playerId).getHand().getCardsInHand().add(card);

                button.addEventHandler(MouseEvent.ANY, handEventHandler);
                addShowEventHandler(button);
                player2Hand.getChildren().add(button);
            }

            log.info("button created");
        }
        else
        {
            log.info("Nincs több kártyám: " + gameState.getPlayer(playerId).getName());
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

    public void addBattleEventHandler(ObservableList<Node> childs)
    {
        for (Node node: childs)
        {
            node.addEventHandler(MouseEvent.ANY, battleEventHandler);
        }
    }

    public void removeBattleEventHandler(ObservableList<Node> childs)
    {
        for (Node node: childs)
        {
            node.removeEventHandler(MouseEvent.ANY, battleEventHandler);
        }
    }

    public void addShowEventHandler(Button button)
    {
            button.addEventHandler(MouseEvent.ANY, showEventHandler);
    }

    public void nextPhase(MouseEvent mouseEvent)
    {
        nextPhaseButton.setDisable(true);
        if(gameState.getTurn() == 0)
        {
            addEventHandlerByRow(2, battleEventHandler);
            addEventHandlerByRow(3, battleEventHandler);
            removeHandEventHandler(player1Hand.getChildren());
        }
        else
        {
            addEventHandlerByRow(0, battleEventHandler);
            addEventHandlerByRow(1, battleEventHandler);
            removeHandEventHandler(player2Hand.getChildren());
        }
    }

    public void end(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
                            clickedButton = (Button)mouseEvent.getSource();
                            createDialogForCard(card, (Button) mouseEvent.getSource());
                        }
                        else if(card.getClass() == SpellCard.class)
                        {
                            clickedButton = (Button)mouseEvent.getSource();
                            createDialogForCard(card, (Button) mouseEvent.getSource());
                        }

                        break;
                    }
                }
            }
        }
    }


    void addEventHandlerByRow(int row, EventHandler eventHandler)
    {
        for (Node node : board.getChildren())
        {
            if(node.getClass() == Group.class)
                continue;

            if(board.getRowIndex(node) == row)
            {
                node.addEventHandler(MouseEvent.ANY, eventHandler);
                //node.addEventHandler(MouseEvent.ANY, battleEventHandler);
            }
        }
    }

    void removeEventHandlerByRow(int row, EventHandler eventHandler)
    {
        for (Node node : board.getChildren())
        {
            if(node.getClass() == Group.class)
                continue;

            if(board.getRowIndex(node) == row)
            {
                node.removeEventHandler(MouseEvent.ANY, eventHandler);
            }
        }
    }

    class BattleEventHandler implements EventHandler<MouseEvent>
    {
        @Override
        public void handle(MouseEvent mouseEvent)
        {
            if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                String cardName = ((Button)mouseEvent.getSource()).getId();
                Card card = gameState.getCardFromPlayerBoard(cardName, gameState.getPlayer(gameState.getTurn()));

                if(gameState.getCardModeFromPlayerBoard(cardName) == CardSlot.Mode.DEFENSE)
                {
                    clickedButton = (Button)mouseEvent.getSource();
                    battlePhaseDialog(card, gameState.getCardModeFromPlayerBoard(cardName));
                }
                else
                {
                    clickedButton = (Button)mouseEvent.getSource();
                    battlePhaseDialog(card, gameState.getCardModeFromPlayerBoard(cardName));
                }
            }
        }
    }
    class ShowEventHandler implements EventHandler<MouseEvent>
    {

        @Override
        public void handle(MouseEvent mouseEvent) {
            if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_ENTERED))
            {
                cardView.setImage(new Image(getClass().getResource(gameState.getDirectoryOfCard(((Button)mouseEvent.getSource()).getId())).toExternalForm()));
            }
            else if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_EXITED))
            {
                cardView.setImage(new Image(getClass().getResource("/pictures/backface.jpg").toExternalForm()));
            }

        }
    }

EndActionEventHandler endActionEventHandler = new EndActionEventHandler();

    class EndActionEventHandler implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent event)
        {
            if(event.getEventType().equals(ActionEvent.ANY))
            {
                log.info("endEVENT");
            }
        }
    }

    class AttackEventHandler implements EventHandler<MouseEvent>
    {
        @Override
        public void handle(MouseEvent mouseEvent)
        {
            if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                log.info("HITTED");

                MonsterCard card1 = (MonsterCard)gameState.getCardByName(clickedButton.getId());
                MonsterCard card2 = (MonsterCard)gameState.getCardByName(((Button)mouseEvent.getSource()).getId());

                switch (gameState.monsterHit(card1, card2))
                {
                    case WIN: board.getChildren().remove(mouseEvent.getSource()); usedTheirAction.add(clickedButton);break;
                    case FAIL: board.getChildren().remove(clickedButton);break;
                    case DRAW: board.getChildren().remove(clickedButton);  board.getChildren().remove(mouseEvent.getSource());break;
                }

                if(gameState.getTurn() == 0)
                {
                    addEventHandlerByRow(2, battleEventHandler);
                    addEventHandlerByRow(3, battleEventHandler);
                    removeEventHandlerByRow(1, this);
                    usedTheirAction.forEach(e -> e.removeEventHandler(MouseEvent.ANY, battleEventHandler));
                }
                else
                {
                    addEventHandlerByRow(0, battleEventHandler);
                    addEventHandlerByRow(1, battleEventHandler);
                    removeEventHandlerByRow(2, this);
                    usedTheirAction.forEach(e -> e.removeEventHandler(MouseEvent.ANY, battleEventHandler));
                }

                if(gameState.isGameOver())
                {
                    setGameOverDialog();
                }

                initPlayersLifePoints();

            }
        }
    }
}