package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.MonsterCardDao;
import game.player.Player;
import guice.PersistenceModule;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class GameStateTest4 {

    @Test
    void directHit()
    {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));

        MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);


        Player player1 = new Player();
        Player player2 = new Player();

        player1.setLifePoints(8000);
        player2.setLifePoints(5000);

        GameState gameState =new GameState();

        gameState.setTurn(0);



        Player[] playerList = new  Player[2];
        playerList[0] = player1;
        playerList[1] = player2;

        gameState.setPlayers(playerList);

        Card card = monsterCardDao.find(2).get();

        gameState.directHit((MonsterCard)card);

        assertEquals(2500, gameState.getPlayer(1).getLifePoints());

        assertNotEquals(3000, gameState.getPlayer(1).getLifePoints());

    }
}