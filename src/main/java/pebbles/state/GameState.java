package pebbles.state;

import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;


/**
 * Represents the state of the game.
 */
public class GameState implements Cloneable{

    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE=4;

    /**
     * The maximum number of pebbles that you can take.
     */
    public static final int MAX_TO_TAKE=4;

    /**
     * The name of the first player.
     */
    public String player1= "";
    /**
     * The name of the second player.
     */
    public String player2= "";

    /**
     * Contains that which player is playing right now.
     */
    public String now_plays;
    /**
     * Counts how many pebbles were taken this turn.
     */
    public int taken=0;

    private ReadOnlyObjectWrapper<Square> pebbles[][]=new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private int takenPositionRow[]=new int[BOARD_SIZE];
    private int takenPositionCol[]=new int[BOARD_SIZE];

    /**
     * Creates a {@code GameState} object that corresponds to the original
     * initial state of the game.
     */
    public GameState(){
        for(int i=0;i<BOARD_SIZE;i++) {
            this.takenPositionCol[i]=-1;
            this.takenPositionRow[i]=-1;
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.pebbles[i][j] = new ReadOnlyObjectWrapper<>(Square.PEBBLE);
            }
        }
    }

    /**
     * Sets the players' name and the starter player.
     *
     * @param player1 the name of the first player
     * @param player2 the name of the second player
     */
    public void setPlayersName(String player1,String player2){
        this.player1=player1;
        this.player2=player2;
        this.now_plays=this.player1;
    }

    /**
     * @return if the game has ended.
     */
    public boolean isOver(){
        for(int i=0;i<BOARD_SIZE;i++){
            for (int j=0;j<BOARD_SIZE;j++){
                if (!isEmpty(i,j)) return false;
            }
        }
        return true;
    }

    /**
     * Decides which player won the game.
     *
     * @return an empty string if the game hasn't ended yet, otherwise the winner player
     */
    public String whoWins(){
        if (isOver()){
            if (taken!=0){
                if (now_plays==player1) return player2;
                else return player1;
            }
            else {
                if (now_plays==player1) return player1;
                else return player2;
            }
        }
        return "";
    }

    /**
     * Checks if everything is okay on the board, then decides whether the player can
     * take a pebble, takes it and returns with true or they made an invalid move and returns with false.
     *
     * @param row the number of the row
     * @param col the number of column
     * @return a boolean value
     */
    public boolean take(int row,int col){
        checking();
        if (whatIsInside(row, col)!=Square.PEBBLE){
            Logger.error("There is no pebble at {},{}!",row,col);
            return false;
        }
        if (taken==0){
            taking(row, col);
            return true;
        }
        if (pickFromRow(row) && !pickFromColumn(col)){
            if (!isNextToEachOtherRow(col)){
                Logger.error("You can't take pebbles with space between them!");
                return false;
            }
            taking(row, col);
            return true;
        }
        if (pickFromColumn(col) && !pickFromRow(row)){
            if (!isNextToEachOtherCol(row)){
                Logger.error("You can't take pebbles with space between them!");
                return false;
            }
            taking(row, col);
            return true;
        }
        Logger.error("Invalid move!");
        return false;
    }

    /**
     * Gives the control to the other player, clears the arrays and sets {@code taken} to 0.
     */
    public void turnIsOver(){
        if (taken<1){
            Logger.error("You have to take at least one pebble!");
            return;
        }
        Logger.debug("{} is done with their turn",now_plays);
        if (now_plays.equals(player1)){
            this.now_plays=player2;
        }
        else {
            this.now_plays=player1;
        }

        for (int i=0;i<taken;i++){
            takenPositionCol[i]=-1;
            takenPositionRow[i]=-1;
        }
        taken=0;
    }

    /**
     * Decides if the next pebble is at the same row, as the earlier taken ones.
     *
     * @param row which row the pebble is in
     * @return a boolean value
     */
    public boolean pickFromRow(int row){
        if (taken>0){
            for (int i=0;i<taken;i++){
                if (takenPositionRow[i]!=row) return false;
            }
        }
        return true;
    }

    /**
     * Decides if the next pebble is at the same column, as the earlier taken ones.
     *
     * @param col which column the pebble is in
     * @return a boolean value
     */
    public boolean pickFromColumn(int col){
        if (taken>0){
            for (int i=0;i<taken;i++){
                if (takenPositionCol[i]!=col) return false;
            }
        }
        return true;
    }

    /**
     * Decides if there is empty space between the horizontally taken pebbles.
     *
     * @param col which column has the next pebble
     * @return a boolean value
     */
    public boolean isNextToEachOtherRow(int col){
        if (taken>0){
            if (Math.abs(takenPositionCol[taken-1]-col)>1) return false;
        }
        return true;
    }

    /**
     * Decides if there is empty space between the vertically taken pebbles.
     *
     * @param row which row has the next pebble
     * @return a boolean value
     */
    public boolean isNextToEachOtherCol(int row){
        if (taken>0){
            if (Math.abs(takenPositionRow[taken-1]-row)>1) return false;
        }
        return true;
    }

    /**
     * Returns which Square enum is on that index.
     *
     * @param row the number of the row
     * @param col the number of column
     * @return a Square enum
     */
    public Square whatIsInside(int row, int col) {
        return pebbles[row][col].get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(pebbles[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Does the taking process.
     *
     * @param row which row the pebble is in
     * @param col which column the pebble is in
     */
    private void taking(int row,int col){
        takenPositionRow[taken]=row;
        takenPositionCol[taken]=col;
        taken++;
        Logger.debug("Pebble taken from row {}, column {}, by {}, they have {} pebble(s)",row,col,now_plays,taken);
        if (now_plays.equals(player1)){
            pebbles[row][col].set(Square.PLAYER1);
        }
        if (now_plays.equals(player2)){
            pebbles[row][col].set(Square.PLAYER2);
        }
        if (taken==MAX_TO_TAKE){
            Logger.trace("{} took {} pebbles, that's the maximum!",now_plays,MAX_TO_TAKE);
            turnIsOver();
            taken=0;
        }
    }

    private boolean isEmpty(int row,int col){
        return whatIsInside(row, col)!=Square.PEBBLE;
    }

    private void checking(){
        if (pebbles.length!=BOARD_SIZE){
            throw new IllegalArgumentException();
        }
        for(int i=0;i<BOARD_SIZE;i++){
            for (int j=0;j<BOARD_SIZE;j++){
                if (pebbles[i][j].get()==Square.PEBBLE||
                        pebbles[i][j].get()==Square.PLAYER1||
                        pebbles[i][j].get()==Square.PLAYER2){
                    continue;
                }
                else throw new IllegalArgumentException();
            }
        }
    }
}
