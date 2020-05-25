package game;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.Card;
import game.cards.MonsterCardDao;
import game.cards.SpellCardDao;
import game.deck.Deck;
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
    public enum Phases
    {
        MAIN,
        BATTLE
    }

    private Player[] players;

    private int turn;

    private Phases phase;

    private Injector injector = Guice.createInjector(new PersistenceModule("test"));

    private MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);

    private SpellCardDao spellCardDao = injector.getInstance(SpellCardDao.class);

    private Board board;

    public GameState()
    {
        players = new Player[2];

        this.turn = 0;

        this.phase = Phases.MAIN;

        board = new Board();

        board.setMonsterCardSlots(Lists.newArrayList());
        board.setSpellCardSlots(Lists.newArrayList());

        for (int i = 0; i < 5; i++) {
            board.getMonsterCardSlots().add(new CardSlot());
            board.getSpellCardSlots().add(new CardSlot());
        }

        players[0] = Player.builder()
                .name("player1Name")
                .build();

        players[1] = Player.builder()
                .name("player2Name")
                .build();

        getPlayer(0).setHand(new Hand());
        getPlayer(1).setHand(new Hand());

        initPlayersDeck();
    }


    public void initPlayersDeck() {
        int[] player1MonsterCardIds = {1};
        int[] player1SpellCardIds = {1};

        List<Card> player1Cards = new ArrayList<>();

        for (int i : player1MonsterCardIds) {
            player1Cards.add(monsterCardDao.find(i).get());
        }

        for (int i : player1SpellCardIds) {
            player1Cards.add(spellCardDao.find(i).get());
        }

        Deck player1Deck = Deck.builder()
                .cards(player1Cards)
                .build();

        int[] player2MonsterCardIds = {1,2,3,4};
        int[] player2SpellCardIds = {1};

        List<Card> player2Cards = new ArrayList<>();

        for (int i : player2MonsterCardIds) {
            player2Cards.add(monsterCardDao.find(i).get());
        }
        for (int i : player2SpellCardIds) {
            player2Cards.add(spellCardDao.find(i).get());
        }

        Deck player2Deck = Deck.builder()
                .cards(player2Cards)
                .build();

        getPlayer(0).setDeck(player1Deck);
        getPlayer(1).setDeck(player2Deck);

        log.info("Player's deck are initialized!");
    }

    public boolean isBoardFullOfMonster()
    {
        int count = 0;
        if(getTurn() == 0)
        {
            for(CardSlot slot : board.getMonsterCardSlots())
            {
                if(slot.getCard() != null)
                    count++;
            }
        }
        else
        {
            for(CardSlot slot : board.getMonsterCardSlots())
            {
                if(slot.getCard() != null)
                    count++;
            }
        }

        if(count == 5)
            return true;

        return false;
    }

    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

}
