package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.CardDao;
import game.cards.MonsterCard;
import game.deck.Deck;
import game.deck.DeckDao;
import guice.PersistenceModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


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

    private CardDao cardDao = injector.getInstance(CardDao.class);

    private DeckDao deckDao = injector.getInstance(DeckDao.class);

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

        initPlayersDeck();
    }


    public void initPlayersDeck()
    {
        int[] player1CardIds = {1,3,5};

        List<MonsterCard> player1Monsters = new ArrayList<>();

        for(int i : player1CardIds)
        {
            player1Monsters.add(cardDao.find(i).get());
        }

        Deck player1Deck = Deck.builder()
                .monsterCards(player1Monsters)
                .build();

        int[] player2CardIds = {2,4};

        List<MonsterCard> player2Monsters = new ArrayList<>();

        for(int i : player2CardIds)
        {
            player2Monsters.add(cardDao.find(i).get());
        }

        Deck player2Deck = Deck.builder()
                .monsterCards(player2Monsters)
                .build();

        deckDao.persist(player1Deck);
        deckDao.persist(player2Deck);

        getPlayer(0).setDeck(deckDao.find(1).get());
        getPlayer(1).setDeck(deckDao.find(2).get());

        log.info("Player's deck are initialized!");
    }

    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

}
