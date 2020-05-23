package game.cards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class MonsterCard extends Card
{
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
}
