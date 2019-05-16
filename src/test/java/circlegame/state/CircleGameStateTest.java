package circlegame.state;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircleGameStateTest {

    @Test
    public void isSolvedTest() {
        CircleGameState game = new CircleGameState();
        assertEquals(false, game.isSolved());
        int[][] a = {
                {2, 2, 2, 2, 2},
                {0, 3, 0, 3, 0},
                {0, 0, 0, 0, 0},
                {0, 3, 1, 3, 0},
                {1, 1, 0, 1, 1}
        };
        CircleGameState game2 = new CircleGameState(a);
        assertEquals(true, game2.isSolved());

        int[][] b = {
                {2, 2, 0, 2, 2},
                {0, 3, 2, 3, 0},
                {0, 0, 0, 0, 0},
                {0, 3, 0, 3, 0},
                {1, 1, 1, 1, 1}
        };
        CircleGameState game3 = new CircleGameState(b);
        assertEquals(true, game2.isSolved());
    }

    @Test
    public void pushCircleTest(){
        CircleGameState game = new CircleGameState();

        game.pushCircles(4,4, 4, 0, Direction.UP);
        assertEquals(Place.EMPTY, game.getTray()[4][4]);
        assertEquals(Place.EMPTY, game.getTray()[4][0]);
        assertEquals(Place.RED, game.getTray()[3][4]);
        assertEquals(Place.RED, game.getTray()[3][0]);

        assertEquals(Place.BLUE, game.getTray()[0][0]);
        assertEquals(Place.BLUE, game.getTray()[0][2]);
        assertEquals(Place.EMPTY, game.getTray()[1][0]);
        assertEquals(Place.EMPTY, game.getTray()[1][2]);
        game.pushCircles(0,0, 0, 2, Direction.DOWN);
        assertEquals(Place.EMPTY, game.getTray()[0][0]);
        assertEquals(Place.EMPTY, game.getTray()[0][2]);
        assertEquals(Place.BLUE, game.getTray()[1][0]);
        assertEquals(Place.BLUE, game.getTray()[1][2]);

        game.pushCircles(4,4, 4, 0, Direction.UP);
        assertEquals(Place.EMPTY, game.getTray()[4][4]);
        assertEquals(Place.EMPTY, game.getTray()[4][0]);
    }

    @Test
    public void isValidStep(){
        CircleGameState game = new CircleGameState();
        assertTrue(game.isValidStep(4,4,4,0,Direction.UP));
        assertFalse(game.isValidStep(4,4,4,0,Direction.DOWN));
        assertFalse(game.isValidStep(4,4,4,0,Direction.RIGHT));
        assertFalse(game.isValidStep(4,4,4,0,Direction.LEFT));
        game.changePlayer();
        assertFalse(game.isValidStep(0,0,0,0,Direction.UP));
        assertFalse(game.isValidStep(0,0,0,2,Direction.RIGHT));
        assertFalse(game.isValidStep(0,0,0,2,Direction.LEFT));
        assertTrue(game.isValidStep(0,0,0,2,Direction.DOWN));

        assertFalse(game.isValidStep(-1,0,0,0,Direction.UP));
        assertFalse(game.isValidStep(0,-1,0,2,Direction.RIGHT));
        assertFalse(game.isValidStep(0,0,-1,2,Direction.LEFT));
        assertFalse(game.isValidStep(0,0,0,-2,Direction.LEFT));

        assertFalse(game.isValidStep(10,0,0,0,Direction.UP));
        assertFalse(game.isValidStep(0,10,2,0,Direction.RIGHT));
        assertFalse(game.isValidStep(0,0,10,2,Direction.LEFT));
        assertFalse(game.isValidStep(0,0,0,10,Direction.LEFT));

        game.changePlayer();
        assertFalse(game.isValidStep(1,1,0,0,Direction.UP));
        assertFalse(game.isValidStep(1,1,0,0,Direction.DOWN));
        assertFalse(game.isValidStep(1,1,0,0,Direction.LEFT));
        assertFalse(game.isValidStep(1,1,0,0,Direction.RIGHT));

        assertFalse(game.isValidStep(0,1,0,0,Direction.UP));
        assertFalse(game.isValidStep(0,1,0,0,Direction.DOWN));
        assertFalse(game.isValidStep(0,1,0,0,Direction.LEFT));
        assertFalse(game.isValidStep(0,1,0,0,Direction.RIGHT));
    }

    @Test
    public void isWinnerTest(){
        CircleGameState game = new CircleGameState();

        assertFalse(game.isWinner(Place.BLUE));
        assertFalse(game.isWinner(Place.RED));
        assertThrows(IllegalArgumentException.class, () -> game.isWinner(Place.BLACK));
        assertThrows(IllegalArgumentException.class, () -> game.isWinner(Place.EMPTY));

        int[][] a = {
                {2, 2, 2, 2, 2},
                {0, 3, 0, 3, 0},
                {0, 0, 0, 0, 0},
                {0, 3, 1, 3, 0},
                {1, 1, 0, 1, 1}
        };
        CircleGameState game2 = new CircleGameState(a);
        assertFalse(game2.isWinner(Place.BLUE));
        assertTrue(game2.isWinner(Place.RED));

        int[][] b = {
                {2, 2, 0, 2, 2},
                {0, 3, 2, 3, 0},
                {0, 0, 0, 0, 0},
                {0, 3, 0, 3, 0},
                {1, 1, 1, 1, 1}
        };

        CircleGameState game3 = new CircleGameState(b);

        assertTrue(game3.isWinner(Place.BLUE));
        assertFalse(game3.isWinner(Place.RED));
    }



}