package circlegame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceTest {

    @Test
    public void of() {
        assertEquals(Place.EMPTY,Place.of(0));
        assertEquals(Place.BLUE,Place.of(1));
        assertEquals(Place.RED,Place.of(2));
        assertEquals(Place.BLACK,Place.of(3));
        assertThrows(IllegalArgumentException.class, () -> Place.of(-1));
        assertThrows(IllegalArgumentException.class, () -> Place.of(4));
    }

    @Test
    public void pushTo() {
        assertThrows(UnsupportedOperationException.class, () -> Place.EMPTY.pushTo(Place.BLACK));
        assertThrows(UnsupportedOperationException.class, () -> Place.BLACK.pushTo(Place.EMPTY));
        assertEquals(Place.BLUE, Place.BLUE.pushTo(Place.EMPTY).pushTo(Place.BLUE));
        assertEquals(Place.RED, Place.RED.pushTo(Place.EMPTY).pushTo(Place.RED));
        assertEquals(Place.EMPTY, Place.RED.pushTo(Place.EMPTY));
        assertEquals(Place.EMPTY, Place.BLUE.pushTo(Place.EMPTY));
        assertEquals(Place.BLUE, Place.EMPTY.pushTo(Place.BLUE));
        assertEquals(Place.RED, Place.EMPTY.pushTo(Place.RED));
    }

}