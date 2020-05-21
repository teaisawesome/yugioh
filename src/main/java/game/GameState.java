package game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.CardDao;
import game.cards.MonsterCard;
import guice.PersistenceModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
    }


    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

    public void setPlayerData()
    {
        List<MonsterCard> monsters = cardDao.findAll();

        Deck deck1 = Deck.builder()
                .monsterCards(monsters)
                .build();

        Deck deck2 = Deck.builder()
                .monsterCards(monsters)
                .build();

        players[0].setDeck(deck1);
        players[1].setDeck(deck2);

    }

    // test
    public void initMonsterCards()
    {

        MonsterCard card1 = MonsterCard.builder()
                .cardName("Blue Eyes White Dragon")
                .level(8)
                .attack(3000)
                .defense(2500)
                .frontFace("blue-eyes-white-dragon-faceup.jpg")
                .backFace("*")
                .build();
        MonsterCard card2 = MonsterCard.builder()
                .cardName("Dark Magician")
                .level(7)
                .attack(2500)
                .defense(2100)
                .frontFace("dark-magician-faceup.jpg")
                .backFace("*")
                .build();
        MonsterCard card3 = MonsterCard.builder()
                .cardName("Dark Magician Girl")
                .level(6)
                .attack(2000)
                .defense(1700)
                .frontFace("dark-magician-girl-faceup.jpg")
                .backFace("*")
                .build();
        MonsterCard card4 = MonsterCard.builder()
                .cardName("Red Eyes Black Dragon")
                .level(7)
                .attack(2400)
                .defense(2000)
                .frontFace("red-eyes-black-dragon-faceup.png")
                .backFace("*")
                .build();
        MonsterCard card5 = MonsterCard.builder()
                .cardName("Sagi The Dark Clown")
                .level(3)
                .attack(600)
                .defense(1500)
                .frontFace("sagi-the-dark-clown-faceup.png")
                .backFace("*")
                .build();

        cardDao.persist(card1);
        cardDao.persist(card2);
        cardDao.persist(card3);
        cardDao.persist(card4);
        cardDao.persist(card5);


        cardDao.findAll().forEach(System.out::println);
    }
}
