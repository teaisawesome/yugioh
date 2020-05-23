package game;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.*;
import game.deck.Deck;
import guice.PersistenceModule;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Class represents one specific state of the game.
 */
@Data
@Slf4j
public class GameState
{
    private Player[] players;

    private int turn;

    private Injector injector = Guice.createInjector(new PersistenceModule("test"));

    private MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);

    private SpellCardDao spellCardDao = injector.getInstance(SpellCardDao.class);

    private Board board;



    public GameState()
    {
        players = new Player[2];

        this.turn = 0;

        board = new Board();

        for (int i = 0; i < 10; i++)
        {
            board.setSlots(Lists.newArrayList(
               new CardSlot()
            ));
        }

        players[0] = Player.builder()
                .name("player1Name")
                .build();

        players[1] = Player.builder()
                .name("player2Name")
                .build();

        initPlayersDeck();
    }


    public void initPlayersDeck()
    {
        int[] player1MonsterCardIds = {1,3,5};
        int[] player1SpellCardIds = {1};

        List<Card> player1Cards = new ArrayList<>();

        for(int i : player1MonsterCardIds)
        {
            player1Cards.add(monsterCardDao.find(i).get());
        }

        for(int i : player1SpellCardIds)
        {
            player1Cards.add(spellCardDao.find(i).get());
        }

        Deck player1Deck = Deck.builder()
                .cards(player1Cards)
                .build();

        int[] player2CardIds = {2,4};

        List<Card> player2Monsters = new ArrayList<>();

        for(int i : player2CardIds)
        {
            player2Monsters.add(monsterCardDao.find(i).get());
        }

        Deck player2Deck = Deck.builder()
                .cards(player2Monsters)
                .build();

        getPlayer(0).setDeck(player1Deck);
        getPlayer(1).setDeck(player2Deck);

        log.info("Player's deck are initialized!");
    }

    /**
     * Initialize player hand cards.
     */
    public void initHandCards(HBox player1Hand, HBox player2Hand)
    {
        for (Card card: getPlayer(0).getDeck().getCards())
        {
            Button button = new Button();
            button.setPrefWidth(100);
            button.setPrefHeight(140);
            button.setId(card.getCardName());

            setStyleForButton(button, card);

            // ezt berakni egy külön metódusba
            button.setOnMouseClicked(e-> {
                Alert alertDialog = new Alert(Alert.AlertType.NONE);
                alertDialog.setTitle("Yu-Gi-OH! Action!");

                if(card.getClass() == MonsterCard.class)
                {
                    alertDialog.setContentText("Choose a(n) action!");
                    ButtonType buttonTypeOne = new ButtonType("Summon Monster");
                    ButtonType buttonTypeTwo = new ButtonType("Set to Defense Mode");
                    ButtonType cancelButton = new ButtonType("Cancel");

                    alertDialog.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, cancelButton);

                    Optional<ButtonType> result = alertDialog.showAndWait();
                    if(result.get() == cancelButton)
                    {
                        alertDialog.close();
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
                    if(result.get() == cancelButton)
                    {
                        alertDialog.close();
                    }
                }
            });

            player1Hand.getChildren().add(button);

            log.info("button created");
        }
    }

    /**
     * This method bind the card datas their own button.
     *
     * @param button the button reference which we set
     * @param card the card reference which contain the card infos for bind them to button
     */
    public void setStyleForButton(Button button, Card card)
    {
        if(card.getClass() == MonsterCard.class)
        {
            button.setStyle(
                    "-fx-background-image: url('"+ getClass().getResource("/pictures/monsters/" + card.getFrontFace()).toExternalForm()+"');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );
        }
        else if(card.getClass() == SpellCard.class)
        {
            button.setStyle(
                    "-fx-background-image: url('"+ getClass().getResource("/pictures/spells/" + card.getFrontFace()).toExternalForm()+"');\n" +
                            "-fx-background-position: center;\n" +
                            "-fx-background-size: cover;"
            );
        }
    }

    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

}
