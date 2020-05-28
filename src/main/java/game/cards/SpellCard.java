package game.cards;

import lombok.Data;

import javax.persistence.Entity;

/**
 * This class defines a spellcard in the game.
 * This is subclass which extends Card class.
 * All of fields will be persisted with the entity.
 */
@Data
@Entity
public class SpellCard extends Card
{
    /**
     * Contains the spell card special effect.
     */
    private String effect;
}
