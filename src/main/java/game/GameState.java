package game;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.CardDao;
import game.cards.MonsterCard;
import guice.PersistenceModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Class represents one specific state of the game.
 */
@Data
@Slf4j
public class GameState
{
    private Player[] players;

    private int turn;

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

    public MonsterCard generateCard()
    {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));
        CardDao cardDao = injector.getInstance(CardDao.class);

        MonsterCard mcard = MonsterCard.builder()
                .cardName("Dark Magician")
                .level(5)
                .attack(2500)
                .defense(2000)
                .frontFace("dark.png")
                .backFace("dark-b.png")
                .build();

        cardDao.persist(mcard);

        cardDao.findAll().forEach(System.out::println);

        return mcard;
    }


    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }

    public void setPlayerData()
    {
        MonsterCard blueeyes = MonsterCard.builder()
                .cardName("Blue Eyes White Dragon")
                .level(7)
                .attack(300)
                .defense(2500)
                .frontFace("asd")
                .backFace("asd")
                .build();

        MonsterCard dark = MonsterCard.builder()
                .cardName("Dark Magician")
                .level(5)
                .attack(2500)
                .defense(2000)
                .frontFace("dark.png")
                .backFace("dark-b.png")
                .build();

        MonsterCard monster = MonsterCard.builder()
                .cardName("Monster")
                .level(5)
                .attack(5000)
                .defense(1500)
                .frontFace("asd.png")
                .backFace("asd-b.png")
                .build();



        Deck deck1 = Deck.builder()
                .monsterCards(Lists.newArrayList(
                        blueeyes,
                        dark
                ))
                .build();

        Deck deck2 = Deck.builder()
                .monsterCards(Lists.newArrayList(
                        monster
                ))
                .build();

        players[0].setDeck(deck1);
        players[1].setDeck(deck2);

    }

}
