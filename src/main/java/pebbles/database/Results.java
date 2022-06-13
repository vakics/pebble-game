package pebbles.database;

/**
 * The class responsible for the storing of the data for the json file.
 */
@lombok.Data
public class Results {
    /**
     * The name of the first player.
     */
    public String player1;
    /**
     * The name of the second player.
     */
    public String player2;
    /**
     * The name of the player, who won the game.
     */
    public String winner;

    /**
     * A constructor with all arguments.
     *
     * @param player1 the name of the first player
     * @param player2 he name of the second player
     * @param whoWins the name of the player, who won the game
     */
    public Results(String player1, String player2, String whoWins) {
        this.player1=player1;
        this.player2=player2;
        this.winner=whoWins;
    }
}
