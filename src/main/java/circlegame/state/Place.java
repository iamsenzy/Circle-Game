package circlegame.state;

/**
 * Class representing the game places.
 */
public enum  Place {
    EMPTY,
    BLUE,
    RED,
    BLACK;

    /**
     * The array defining the transitions between orientations when a Circle
     * pushed.
     */
    private static final int[] T = {
            0,
            1,
            2
    };

    /**
     * Returns the instance represented by the value specified.
     *
     * @param value the value representing an instance
     * @return the instance represented by the value specified
     * @throws IllegalArgumentException if the value specified does not
     * represent an instance
     */
    public static Place of(int value) {
        if (value < 0 || value >= values().length) {
            throw new IllegalArgumentException();
        }
        return values()[value];
    }

    /**
     * Returns the integer value that represents this instance.
     *
     * @return the integer value that represents this instance
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Push a circle to another place.
     * @param to the place where circle goes
     * @return the place pushed to the new place specified
     * @throws UnsupportedOperationException if the method is invoked on the
     * {@link #BLACK} instance or to {@link #BLACK}, or {@link #BLUE} to {@link #RED}
     * or {@link #RED} to {@link #BLUE}
     */
    public Place pushTo(Place to) {
        if (this == BLACK || to == BLACK || (this.equals(to)) ||
                ((this == BLUE && to == RED) || (this == RED && to == BLUE)) ) {
            throw new UnsupportedOperationException();
        }
        return values()[T[to.ordinal()]];
    }

    public String toString() {
        return Integer.toString(ordinal());
    }

}
