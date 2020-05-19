package game;

import game.cards.MonsterCard;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@Builder
public class Deck
{
    private List<MonsterCard> monsterCards;
}
