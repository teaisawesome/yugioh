package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.MonsterCardDao;
import guice.PersistenceModule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertSame;

class GameStateTest5 {

    @Test
    void getCardByName()
    {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));

        MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);

        List<MonsterCard> monsterCardList;

        monsterCardList = monsterCardDao.findAll();

        GameState gameState =new GameState();

        gameState.setMonsterCardList(monsterCardList);

        gameState.getMonsterCardList().forEach(System.out::println);

        Card card = monsterCardDao.find(1).get();

        assertSame(card, gameState.getCardByName("Blue Eyes White Dragon"));
    }
}