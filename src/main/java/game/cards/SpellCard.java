package game.cards;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class SpellCard extends Card
{
    private String effect;
}
