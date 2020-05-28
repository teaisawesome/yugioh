package game;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import game.cards.*;
import game.deck.Deck;
import game.player.Player;
import game.player.PlayerDao;
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

    private PlayerDao playerDao = injector.getInstance(PlayerDao.class);

    private List<MonsterCard> monsterCardList;

    private List<SpellCard> spellCardList;

    private Card clickedCard;

    public GameState()
    {
        players = new Player[2];

        this.turn = 0;

        this.phase = Phases.MAIN;

        monsterCardList = monsterCardDao.findAll();
        spellCardList = spellCardDao.findAll();

        players[0] = Player.builder()
                .name("player1Name")
                .lifePoints(8000)
                .build();

        playerDao.persist(players[0]);

        players[1] = Player.builder()
                .name("player2Name")
                .lifePoints(8000)
                .build();

        playerDao.persist(players[1]);

        getPlayer(0).setHand(new Hand());
        getPlayer(1).setHand(new Hand());

        // create player's board
        getPlayer(0).setBoard(new Board());
        getPlayer(1).setBoard(new Board());
        setSlotsToBoard(getPlayer(0).getBoard());
        setSlotsToBoard(getPlayer(1).getBoard());

        initPlayersDeck();
    }


    public void initPlayersDeck()
    {
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

        int[] player2MonsterCardIds = {1,2,3,4,1};
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

    public boolean isBoardFullOfSpells(Board board)
    {
        int count = 0;

        for(CardSlot slot : board.getSpellCardSlots())
        {
            if(slot.getCard() != null)
                count++;
        }

        if(count == 5)
            return true;

        return false;
    }

    public void setSlotsToBoard(Board playerBoard)
    {
        playerBoard.setMonsterCardSlots(Lists.newArrayList());
        playerBoard.setSpellCardSlots(Lists.newArrayList());

        for (int i = 0; i < 5; i++) {
            playerBoard.getMonsterCardSlots().add(new CardSlot());
            playerBoard.getSpellCardSlots().add(new CardSlot());
        }
    }

    public Card getCardFromPlayerBoard(String nameId, Player player)
    {
        for (CardSlot slot : player.getBoard().getMonsterCardSlots())
        {
            if(slot.getCard() != null)
            {
                if(slot.getCard().getCardName() == nameId)
                {
                    return slot.getCard();
                }
            }
        }


        for (CardSlot slot : player.getBoard().getSpellCardSlots())
        {
            if(slot.getCard() != null)
            {
                if(slot.getCard().getCardName() == nameId)
                {
                    return slot.getCard();
                }
            }
        }

        return null;
    }

    public CardSlot.Mode getCardModeFromPlayerBoard(String nameId)
    {
        for (CardSlot slot : getPlayer(getTurn()).getBoard().getMonsterCardSlots())
        {
            if(slot.getCard() != null)
            {
                if(slot.getCard().getCardName() == nameId)
                {
                    return slot.getMode();
                }
            }
        }


        for (CardSlot slot : getPlayer(getTurn()).getBoard().getSpellCardSlots())
        {
            if(slot.getCard() != null)
            {
                if(slot.getCard().getCardName() == nameId)
                {
                    return slot.getMode();
                }
            }
        }

        return null;
    }

    public CardSlot getPlayerBoardCardSlot(String cardId, Player player)
    {
        for(CardSlot slot : player.getBoard().getMonsterCardSlots())
        {
            if(slot.getCard().getCardName() == cardId)
            {
                return slot;
            }
        }

        return null;
    }

    public CardSlot.Mode getPlayerCardModeById(Player player, String cardId)
    {
        for (CardSlot slot : player.getBoard().getMonsterCardSlots())
        {
            if(slot.getCard() != null)
            {
                if(slot.getCard().getCardName() == cardId)
                {
                    return slot.getMode();
                }
            }
        }

        return null;
    }

    public String getDirectoryOfCard(String name)
    {
        for (Card card : monsterCardList)
        {
            if(card.getCardName() == name)
            {
                return "/pictures/monsters/"+card.getFrontFace();
            }
        }

        for (Card card : spellCardList)
        {
            if(card.getCardName() == name)
            {
                return "/pictures/spells/" + card.getFrontFace();
            }
        }

        return "/pictures/backface.jpg";
    }

    public Card getCardByName(String name)
    {
        for (Card card : monsterCardList)
        {
            if(card.getCardName() == name)
            {
                return card;
            }
        }

        for (Card card : spellCardList)
        {
            if(card.getCardName() == name)
            {
                return card;
            }
        }

        return null;
    }

    public boolean isEnemyBoardEmpty()
    {
        if(getTurn() == 0)
        {
            for (CardSlot slot : getPlayer(1).getBoard().getMonsterCardSlots())
            {
                if(slot.getCard() != null)
                {
                    return false;
                }
            }
        }
        else
        {
            for (CardSlot slot : getPlayer(0).getBoard().getMonsterCardSlots())
            {
                if(slot.getCard() != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void removeCardFromPlayersBoard(Card card, Player player)
    {
        for(CardSlot slot : player.getBoard().getMonsterCardSlots())
        {
            if(slot.getCard() == card)
            {
                slot.setCard(null);
                slot.setMode(null);
            }
        }

        for(CardSlot slot : player.getBoard().getSpellCardSlots())
        {
            if(slot.getCard() == card)
            {
                slot.setCard(null);
                slot.setMode(null);
            }
        }
    }
    public void directHit(MonsterCard card)
    {
        if(getTurn() == 0)
        {
            int currentLifePoint = getPlayer(1).getLifePoints();

            getPlayer(1).setLifePoints(currentLifePoint - card.getAttack());
        }
        else {
            int currentLifePoint = getPlayer(0).getLifePoints();

            getPlayer(0).setLifePoints(currentLifePoint - card.getAttack());
        }
    }

    public AttackResult monsterHit(MonsterCard ownCard, MonsterCard enemyCard)
    {
        if(getTurn() == 0)
        {
            CardSlot.Mode enemyCardMode = getPlayerCardModeById(getPlayer(1), enemyCard.getCardName());

            AttackResult result = getAttackResult(ownCard, enemyCard, enemyCardMode);

            return result;

        }
        else
        {
            CardSlot.Mode enemyCardMode = getPlayerCardModeById(getPlayer(0), enemyCard.getCardName());

            AttackResult result = getAttackResult(ownCard, enemyCard, enemyCardMode);

            return result;
        }
    }

    public AttackResult getAttackResult(MonsterCard ownCard, MonsterCard enemyCard, CardSlot.Mode enemyCardMode)
    {
        Player enemyPlayer = getTurn() == 0 ? getPlayer(1) : getPlayer(0);

        int ownLifePoints = getPlayer(getTurn()).getLifePoints();
        int enemyLifePoints = enemyPlayer.getLifePoints();

        if(enemyCardMode == CardSlot.Mode.ATTACK)
        {
            if(ownCard.getAttack() > enemyCard.getAttack())
            {
                enemyPlayer.setLifePoints(enemyLifePoints - (ownCard.getAttack() - enemyCard.getAttack()));

                removeCardFromPlayersBoard(enemyCard, enemyPlayer);

                return AttackResult.WIN;
            }
            else if(ownCard.getAttack() < enemyCard.getAttack())
            {
                getPlayer(getTurn()).setLifePoints(ownLifePoints - (enemyCard.getAttack() - ownCard.getAttack()));

                removeCardFromPlayersBoard(ownCard, getPlayer(getTurn()));

                return AttackResult.FAIL;
            }
            else
            {
                removeCardFromPlayersBoard(enemyCard, enemyPlayer);
                removeCardFromPlayersBoard(ownCard, getPlayer(getTurn()));

                return AttackResult.DRAW;
            }
        }
        else if(enemyCardMode == CardSlot.Mode.DEFENSE)
        {
            if(ownCard.getAttack() > enemyCard.getDefense())
            {
                removeCardFromPlayersBoard(enemyCard, enemyPlayer);

                return AttackResult.WIN;
            }
            else if(ownCard.getAttack() < enemyCard.getDefense())
            {
                getPlayer(getTurn()).setLifePoints(ownLifePoints - (enemyCard.getAttack() - ownCard.getAttack()));

                removeCardFromPlayersBoard(ownCard, getPlayer(getTurn()));

                return AttackResult.DRAW;
            }
            else
            {
                removeCardFromPlayersBoard(enemyCard, enemyPlayer);
                removeCardFromPlayersBoard(ownCard, getPlayer(getTurn()));

                return AttackResult.DRAW;
            }
        }
        return null;
    }

    public boolean isGameOver()
    {
        if(getPlayer(0).getLifePoints() <= 0 || getPlayer(1).getLifePoints() <= 0)
        {
            log.info("Vége a játéknak!");
            return true;
        }

        return false;
    }

    public void persistPlayerNames(String player1Name, String player2Name)
    {
        getPlayer(0).setName(player1Name);
        getPlayer(1).setName(player2Name);

        playerDao.persist(getPlayer(0));
        playerDao.persist(getPlayer(1));
    }
    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }
}
