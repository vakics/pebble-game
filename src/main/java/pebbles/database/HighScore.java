package pebbles.database;

@lombok.Data
public class HighScore {
    private String player;
    private long wins;

    public HighScore(String key, long value) {
        this.player=key;
        this.wins=value;
    }
}
