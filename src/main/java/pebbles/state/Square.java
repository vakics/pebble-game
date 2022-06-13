package pebbles.state;

/**
 * An enum class, that contains the value of the squares.
 */
public enum Square {
    /**
     * There's a pebble on the square.
     */
    PEBBLE,
    /**
     * The square is empty and the first player took the pebble.
     */
    PLAYER1,
    /**
     * The square is empty and the second player took the pebble.
     */
    PLAYER2;
}
