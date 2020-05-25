package game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class Board
{
    /**
     * Card slots.
     */
    private List<CardSlot> monsterCardSlots;

    private List<CardSlot> spellCardSlots;
}
