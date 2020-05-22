package game.deck;

import game.cards.MonsterCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Entity
public class Deck
{
    @Id
    @GeneratedValue
    private int id;

    @ManyToMany
    @JoinColumn()
    private List<MonsterCard> monsterCards;
}
