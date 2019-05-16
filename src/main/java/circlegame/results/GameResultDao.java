package circlegame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import javax.management.Query;
import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    /**
     * Constructor
     */
    public GameResultDao() {
        super(GameResult.class);
    }

    /**
     * Returns the list of {@code n} best results with the number
     * how many games he won
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with how many games he won
     */
    @Transactional
    public List<Object[]> findBest(int n) {

        return entityManager.createQuery("SELECT r.player, COUNT(r.player) AS wins FROM GameResult r WHERE r.solved = true GROUP BY r.player ORDER BY COUNT(r.player) DESC")
                .setMaxResults(n)
                .getResultList();


    }

}
