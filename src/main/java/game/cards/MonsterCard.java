package game.cards;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class MonsterCard
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
     * The level of the monster card.
     */
    @Column(nullable = false)
    private int level;

    /**
     * The attack of the monster card.
     */
    @Column(nullable = false)
    private int attack;

    /**
     * The defense of the monster card.
     */
    @Column(nullable = false)
    private int defense;

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
