package game.cards;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * This class defines superclass for MonsterCard and SpellCard to use. All of fields will be persisted with the entity.
 */
@Data
@MappedSuperclass
public class Card
{
    @Id
    @GeneratedValue
    private int id;

    /**
     * The name of the card.
     */
    @Column(nullable = false)
    protected String cardName;

    /**
     * The card's front face URL path.
     */
    @Column(nullable = false)
    protected String frontFace;

    /**
     * The card's back face URL path.
     */
    @Column(nullable = false)
    protected String backFace;
}
