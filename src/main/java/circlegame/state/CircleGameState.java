package circlegame.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class CircleGameState implements Cloneable {

    /**
     * The array representing the initial configuration of the tray.
     */
    public static final int[][] INITIAL = {
            {1, 1, 1, 1, 1},
            {0, 3, 0, 3, 0},
            {0, 0, 0, 0, 0},
            {0, 3, 0, 3, 0},
            {2, 2, 2, 2, 2},
    };

    /**
     * The array storing the current configuration of the tray.
     */
    @Setter(AccessLevel.NONE)
    private Place[][] tray;

    /**
     * The boolean storing who is the actual player {@code true} presenting Blue
     * {@code false} presenting Red player.
     */
    private boolean player = false;

    /**
     * Creates a {@code CircleGameState} object representing the (original)
     * initial state of the puzzle.
     */
    public CircleGameState() {
        this(INITIAL);
    }

    /**
     * Creates a {@code CircleGameState} object that is initialized it with
     * the specified array.
     *
     * @param a an array of size 3&#xd7;3 representing the initial configuration
     *          of the tray
     * @throws IllegalArgumentException if the array does not represent a valid
     *                                  configuration of the tray
     */
    public CircleGameState(int[][] a) {
        if (!isValidBoard(a)) {
            throw new IllegalArgumentException();
        }
        initBoard(a);
    }

    private boolean isValidBoard(int[][] a) {
        if (a == null || a.length != 5) {
            return false;
        }
        int blue_db = 0, red_db = 0;
        for (int[] row: a){
            if (row == null || row.length != 5) {
                return false;
            }
            for (int space : row) {
                if (space < 0 || space >= Place.values().length) {
                    return false;
                }
                if(space == 1){
                    blue_db++;
                }else if(space == 2){
                    red_db++;
                }
            }
        }
        if(blue_db != 5 || red_db != 5){
            return false;
        }
        return true;
    }

    private void initBoard(int[][] a) {
        this.tray = new Place[5][5];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
               this.tray[i][j] = Place.of(a[i][j]);
            }
        }
    }

    /**
     * Checks if a player win a game.
     * @param p is the player Red / Blue
     * @return {@code true} if p player is the winner {@code false} orherwise.
     * @throws IllegalArgumentException when p isnt RED / BLUE
     */
    public boolean isWinner(Place p){
        if(p == Place.BLACK || p == Place.EMPTY){
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < 5; i++){
            if(p.equals(Place.BLUE)){
                if(tray[4][i] != p ){
                    return false;
                }
            }else if(p.equals(Place.RED)){
                if(tray[0][i] != p ){
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Checks the game has a winner.
     *
     * @return {@code true} if red or blue player won, {@code false} otherwise
     */
    public boolean isSolved() {
        return isWinner(Place.BLUE) || isWinner(Place.RED);
    }

    /**
     * Checks the step.
     * @param circle1x First circle x coordinate
     * @param circle1y First circle y coordinate
     * @param circle2x Second circle x coordinate
     * @param circle2y Second circle y coordinate
     * @param direction Directon of the way want push
     * @return {@code true} when the step is valid {@code false} otherwise.
     */
   public boolean isValidStep(int circle1x, int circle1y, int circle2x, int circle2y, Direction direction){
        if((circle1x == circle2x && circle1y == circle2y)
                || (circle1x+direction.getDx() < 0 || circle1x+direction.getDx() > 4 )
                || (circle2x+direction.getDx() < 0 || circle2x+direction.getDx() > 4 )
                || (circle1y+direction.getDy() < 0 || circle1y+direction.getDy() > 4 )
                || (circle2y+direction.getDy() < 0 || circle2y+direction.getDy() > 4 )){

            return false;
        }else if((tray[circle1x+direction.getDx()][circle1y+direction.getDy()] != Place.EMPTY
               || tray[circle2x+direction.getDx()][circle2y+direction.getDy()] != Place.EMPTY )
               || tray[circle1x][circle1y]== Place.EMPTY || tray[circle2x][circle2y]== Place.EMPTY
               || tray[circle1x][circle1y]== Place.BLACK || tray[circle2x][circle2y]== Place.BLACK) {
            return false;
        }else if((tray[circle1x][circle1y] != Place.BLUE && player )
               ||(tray[circle1x][circle1y] != Place.RED && !player )
               ||(tray[circle1x][circle1y] != tray[circle2x][circle2y]) ){
                return false;
        }

        return true;
   }


    /**
     * Changing player
     */
    public void changePlayer(){
        if(player){
            player = false;
        }else{
            player = true;
        }
    }

    /**
     * Circle one and Circle two push to the Direction and changePlayer.
     * @param circle1x the x position of circle1
     * @param circle1y the y position of circle1
     * @param circle2x the x position of circle2
     * @param circle2y the y position of circle2
     * @param direction the direction where will goes
     */
    public void pushCircles(int circle1x, int circle1y, int circle2x, int circle2y, Direction direction) {
        if(isValidStep(circle1x,circle1y,circle2x,circle2y,direction)) {
            Place from1 = tray[circle1x][circle1y];
            Place from2 = tray[circle2x][circle2y];
            Place to1 = tray[circle1x + direction.getDx()][circle1y + direction.getDy()];
            Place to2 = tray[circle2x + direction.getDx()][circle2y + direction.getDy()];

            tray[circle1x][circle1y] = from1.pushTo(to1);
            tray[circle2x][circle2y] = from2.pushTo(to2);

            tray[circle1x + direction.getDx()][circle1y + direction.getDy()] = to1.pushTo(from1);
            tray[circle2x + direction.getDx()][circle2y + direction.getDy()] = to2.pushTo(from2);
            log.info("Circle one at ({},{}) Circle two at ({},{}) is pushed {}", circle1x, circle1y, circle2x, circle2y, direction);

            changePlayer();
        }
    }


    public CircleGameState clone() {
        CircleGameState copy = null;
        try {
            copy = (CircleGameState) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        copy.tray = new Place[tray.length][];
        for (int i = 0; i < tray.length; ++i) {
            copy.tray[i] = tray[i].clone();
        }
        return copy;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Place[] row : tray) {
            for (Place place : row) {
                sb.append(place).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        CircleGameState state = new CircleGameState();
        System.out.println(state);
        state.pushCircles(4,4, 4,2,Direction.UP);
        System.out.println(state);
        state.pushCircles(0,0, 0,2,Direction.DOWN);
        System.out.println(state);
        state.isSolved();
    }
}
