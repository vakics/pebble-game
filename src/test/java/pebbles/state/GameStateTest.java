package pebbles.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    GameState game1=new GameState();
    GameState game2=new GameState();
    GameState game3=new GameState();
    GameState game4=new GameState();
    GameState game5=new GameState();
    GameState game6=new GameState();
    GameState game7 =new GameState();

    @Test
    void isOver() {
        assertFalse(game1.isOver());
    }

    @Test
    void take() {
        game1.setPlayersName("X","Y");
        game1.take(0,0);
        assertTrue(game1.whatIsInside(0,0)==Square.PLAYER1);
        game1.turnIsOver();
        game1.take(0,0);
        assertFalse(game1.whatIsInside(0,1)==Square.PLAYER2);
        game1.take(0,1);
        assertTrue(game1.whatIsInside(0,1)==Square.PLAYER2);
        game1.turnIsOver();
        game1.take(1,0);
        game1.take(1,2);
        assertFalse(game1.whatIsInside(1,2)==Square.PLAYER1);
        game1.take(2,2);
        assertFalse(game1.whatIsInside(2,2)==Square.PLAYER1);
    }

    @Test
    void turnIsOver() {
        game2.setPlayersName("X","Y");
        assertTrue(game2.now_plays==game2.player1);
        game2.take(0,0);
        game2.turnIsOver();
        assertTrue(game2.now_plays==game2.player2);
    }

    @Test
    void pickFromRow() {
        game3.setPlayersName("x","y");
        game3.take(1,1);
        game3.take(1,2);
        assertTrue(game3.whatIsInside(1,2)==Square.PLAYER1);
        game3.take(2,1);
        assertFalse(game3.whatIsInside(2,1)==Square.PLAYER1);
    }

    @Test
    void pickFromColumn() {
        game4.setPlayersName("x","y");
        game4.take(1,1);
        game4.take(2,1);
        assertTrue(game4.whatIsInside(2,1)==Square.PLAYER1);
        game4.take(1,2);
        assertFalse(game4.whatIsInside(1,2)==Square.PLAYER1);
    }

    @Test
    void isEmptyBetweenRow() {
        game5.setPlayersName("x","y");
        game5.take(0,0);
        game5.take(0,2);
        assertFalse(game5.whatIsInside(0,2)==Square.PLAYER1);
    }

    @Test
    void isEmptyBetweenCol() {
        game6.setPlayersName("x","y");
        game6.take(0,0);
        game6.take(2,0);
        assertFalse(game6.whatIsInside(2,0)==Square.PLAYER1);
    }

    @Test
    void whatIsInside() {
        game7.setPlayersName("X","Y");
        assertTrue(game7.whatIsInside(0,0)==Square.PEBBLE);
        game7.take(1,1);
        assertTrue(game7.whatIsInside(1,1)==Square.PLAYER1);
        game7.turnIsOver();
        game7.take(2,2);
        assertTrue(game7.whatIsInside(2,2)==Square.PLAYER2);
    }
}