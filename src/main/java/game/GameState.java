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

    /**
     * Contains Playe objects from the game.
     */
    private Player[] players;

    /**
     * Current game turn which can be 0 or 1.
     *
     */
    private int turn;

    /**
     * Represent phases.
     */
    private Phases phase;

    /**
     * Injector instance.
     */
    private Injector injector = Guice.createInjector(new PersistenceModule("test"));

    /**
     * MonsterCardDao instance for handle persistence.
     */
    private MonsterCardDao monsterCardDao = injector.getInstance(MonsterCardDao.class);

    /**
     * SpellCardDao instance for handle persistence.
     */
    private SpellCardDao spellCardDao = injector.getInstance(SpellCardDao.class);

    /**
     * PlayerDao instance for handle persistence.
     */
    private PlayerDao playerDao = injector.getInstance(PlayerDao.class);

    /**
     * This list contains all type of monstercard type objects.
     */
    private List<MonsterCard> monsterCardList;

    /**
     * This list contains all type of spellcard type objects.
     */
    private List<SpellCard> spellCardList;

    /**
     *  During duel will be stored and enemy Card object reference in clickedCard.
     */
    private Card clickedCard;


    /**
     * Creates a {@code GameState} object and initialize members with the default init datas.
     */
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

    /**
     * This method initialize each player's deck with by the given id-s.
     * The card infos binded from database infos.
     */
    public void initPlayersDeck()
    {
        int[] player1MonsterCardIds = {1,2,3,4,5,6,7,8};
        int[] player1SpellCardIds = {1,2,1,2};

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

        int[] player2MonsterCardIds = {1,2,3,4,5,6,7,8};
        int[] player2SpellCardIds = {1,2,1,2};

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


    /**
     *  This method check if the player's monster cardslots are full.
     *
     * @param board player's board which we wanna validate
     * @return {@code true}, if there is 5 monster placed into the board, {@code false} if still have enough space for one monster on the board.
     */
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

    /**
     * This method check if the player's monster cardslots are full.
     *
     * @param board player's board which we wanna validate
     * @return {@code true}, if there is 5 spell placed into the board, {@code false} if still have enough space for one spell on the board.
     */
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

    /**
     *  This method add 5x monster and 5x cardslots to player's board. Each cardslot will the a new instance.
     *
     * @param playerBoard player's board to which we add the card slots.
     */
    public void setSlotsToBoard(Board playerBoard)
    {
        playerBoard.setMonsterCardSlots(Lists.newArrayList());
        playerBoard.setSpellCardSlots(Lists.newArrayList());

        for (int i = 0; i < 5; i++) {
            playerBoard.getMonsterCardSlots().add(new CardSlot());
            playerBoard.getSpellCardSlots().add(new CardSlot());
        }
    }

    /**
     * The method check if the searched for nameId equals from monsterCardSlots or spellCardSlots.
     * If its exists return the card object.
     *
     * @param nameId the cardname what we are looking for
     * @param player which player's board is where we search
     * @return Card object otherwise return null
     */

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

    /**
     * This method search by card nameId and find out its mode from the player's board.
     *
     * @param nameId the card's nameId what we are looking for
     * @return get back the card's mode otherwise return null
     */
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

    /**
     * Returns the slot where the card is placed.
     *
     * @param cardId the card what we are looking for
     * @param player which player is where we search
     * @return CardSlot otherwise null
     */
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

    /**
     * Returns the mode of the card specially from monsterCardSlots.
     *
     * @param player which player is where we search
     * @param cardId the card what we are looking for.
     * @return Mode otherwise return null
     */
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

    /**
     * Return the card's path.
     *
     *
     * @param name the  card what we are looking for
     * @return the card's path
     */
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


    /**
     * Return an Card object by card name.
     *
     * @param name the  card what we are looking for
     * @return Card object
     */
    public Card getCardByName(String name)
    {
        for (Card card : monsterCardList)
        {
            if(card.getCardName().equals(name))
            {
                return card;
            }
        }

        for (Card card : spellCardList)
        {
            if(card.getCardName().equals(name))
            {
                return card;
            }
        }

        return null;
    }

    /**
     * Check to see if your opponent's board is empty.
     * This method used for calculate the direct hit.
     *
     * @return true if the board is empty, false otherwise
     */
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


    /**
     * This method loop through the player's board, if the searched card is exits remove it from slot.
     *
     * @param card the  card what we are looking for
     * @param player which player is where we search
     */
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

    /**
     * This method calculate damage and set player's life point if a monster attack directly to player.
     * Calculation: playerLifePoint = playerLifePoint - monster attack points.
     *
     * @param card monstercard which is attack the player directly.
     */
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

    /**
     * This method calls the getAttackResult function and return with an AttackResult.
     *
     * @param ownCard owncard in battle
     * @param enemyCard enemycard in battle
     * @return AttackResult which can be: WIN, FAIL, DRAW
     */
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

    /**
     * Calculate the result of the battle and set life points.
     *
     * There is two ways:
     * ATTACK mode vs ATTACK mode
     * ATTACK mode vs. DEFENSE mode
     *
     * @param ownCard owncard in the battle
     * @param enemyCard enemycard in the battle
     * @param enemyCardMode enemycard mode
     * @return AttackResult object
     */
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

    /**
     * If either player's life points goes to zero or belove the game is ended.
     *
     * @return true if the game is ended, false otherwise
     */
    public boolean isGameOver()
    {
        if(getPlayer(0).getLifePoints() <= 0 || getPlayer(1).getLifePoints() <= 0)
        {
            log.info("Vége a játéknak!");
            return true;
        }

        return false;
    }

    /**
     * This function persist/store in database the player's name. The names comes from welcome page.
     *
     * @param player1Name player1 name
     * @param player2Name player2 name
     */
    public void persistPlayerNames(String player1Name, String player2Name)
    {
        getPlayer(0).setName(player1Name);
        getPlayer(1).setName(player2Name);

        playerDao.persist(getPlayer(0));
        playerDao.persist(getPlayer(1));
    }

    /**
     * Get back Player object by index.
     *
     * @param playerIndex playerindex
     * @return Player object
     */
    public Player getPlayer(int playerIndex)
    {
        return players[playerIndex];
    }
}
