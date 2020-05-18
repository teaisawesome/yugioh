package game;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Card
{
    @Id
    @GeneratedValue
    private int id;

    /**
     * The name of the card.
     */
    @Column(nullable = false)
    private String cardName;

    /**
     * The card's front face URL path.
     */
    @Column(nullable = false)
    private String frontFace;

    /**
     * The card's back face URL path.
     */
    @Column(nullable = false)
    private String backFace;
}
