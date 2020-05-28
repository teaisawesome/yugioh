package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.Card;
import game.cards.MonsterCardDao;
import game.cards.SpellCardDao;
import game.deck.Deck;
import guice.PersistenceModule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest
{


    @Test
    void setTurn()
    {
        GameState gameState = new GameState();

        gameState.setTurn(1);

        assertEquals(1, gameState.getTurn());

        gameState.setTurn(2);

        assertEquals(2, gameState.getTurn());

        gameState.setTurn(3);

        assertNotEquals(2, gameState.getTurn());
    }


    @Test
    void initPlayersDeck()
    {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));

        MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);

        SpellCardDao spellCardDao = injector.getInstance(SpellCardDao.class);


        int[] player1MonsterCardIds = {1,3,2,2,5,4};
        int[] player1SpellCardIds = {1,2};

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

        Card card = monsterCardDao.find(1).get();

        Card spellCard = spellCardDao.find(2).get();

        assertSame(card, player1Cards.get(0));

        assertNotSame(spellCard, player1Cards.get(0));
    }

    @Test
    public boolean isBoardFullOfMonster(Board board)
    {
        int count = 0;

        for(CardSlot slot : board.getMonsterCardSlots())
        {
            if(slot.getCard() != null)
                count++;
        }

        if(count == 5)
            return true;

        return false;
    }



}