package circlegame.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameResult {

    /**
     * Constructor for output
     * @param player The name who wined the game
     * @param wins The number how many games he won.
     */
    public GameResult(String player, Long wins){
        this.player = player;
        this.wins = wins;
    }

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the player.
     */
    @Column(nullable = false)
    private String player;

    /**
     * Indicates whether a player win the game.
     */
    private boolean solved;

    /**
     * The number of steps made by the player.
     */
    private int steps;

    /**
     * The number how many games he won.
     */
    private long wins;

    /**
     * The duration of the game.
     */
    @Column(nullable = false)
    private Duration duration;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }

}
