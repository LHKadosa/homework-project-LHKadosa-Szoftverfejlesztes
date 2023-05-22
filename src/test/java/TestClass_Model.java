import boardgame.model.BoardGameModel;
import boardgame.model.Coordinate;
import boardgame.model.Square;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass_Model {

    @Test
    public void test_inputManager(){
        var model = new BoardGameModel();

        //the player selects a destination before selecting the disc to be moved.
        assertFalse(model.inputManager(2,2));

        //the selected disc belongs or NOT to the other player
        model.currentPlayer = Square.BLUE;
        assertTrue(model.inputManager(0,0));
        assertFalse(model.inputManager(4,4));
        model.currentPlayer = Square.YELLOW;
        assertTrue(model.inputManager(4,4));
        assertFalse(model.inputManager(0,0));

        //the destination is or NOT a diagonal neighbor of the disc the player wants to move.
        model.currentPlayer = Square.BLUE;
        model.inputManager(1,0);
        assertFalse(model.inputManager(2,0));
        assertFalse(model.inputManager(2,2));
        assertTrue(model.inputManager(2,1));
        model.currentPlayer = Square.YELLOW;
        model.inputManager(3,4);
        assertFalse(model.inputManager(2,4));
        assertFalse(model.inputManager(1,1));
        assertTrue(model.inputManager(2,3));
    }

    @Test
    public void test_move(){
        var model = new BoardGameModel();

        model.move(1,1,1,0);
        model.move(2,2,model.BOARD_SIZE - 1,model.BOARD_SIZE - 1);

        assertEquals(Square.NONE,model.board[1][0].get());
        assertEquals(Square.BLUE,model.board[1][1].get());

        assertEquals(Square.NONE,model.board[model.BOARD_SIZE - 1][model.BOARD_SIZE - 1].get());
        assertEquals(Square.YELLOW,model.board[2][2].get());
    }

    @Test
    public void test_isOpponentSquare(){
        var model = new BoardGameModel();

        model.currentPlayer = Square.BLUE;

        assertTrue(model.isOpponentSquare(model.BOARD_SIZE - 1,model.BOARD_SIZE - 1));
        assertFalse(model.isOpponentSquare(0,0));
        assertFalse(model.isOpponentSquare(model.BOARD_SIZE / 2 , model.BOARD_SIZE /2));

        model.currentPlayer = Square.YELLOW;

        assertTrue(model.isOpponentSquare(0,0));
        assertFalse(model.isOpponentSquare(model.BOARD_SIZE - 1,model.BOARD_SIZE - 1));
        assertFalse(model.isOpponentSquare(model.BOARD_SIZE / 2 , model.BOARD_SIZE /2));
    }

    @Test
    public void test_isValidMove(){
        assertTrue(BoardGameModel.isValidMove(1,1,2,2));
        assertFalse(BoardGameModel.isValidMove(1,1,5,2));
    }

    @Test
    public void test_nextPlayer(){
        var model = new BoardGameModel();

        model.selectedTile.set(new Coordinate(1,1));

        assertEquals(1,model.selectedTile.get().getRow());
        assertEquals(1,model.selectedTile.get().getCol());
        assertEquals(Square.BLUE, model.currentPlayer);

        model.nextTurn();

        assertEquals(null,model.selectedTile.get());
        assertEquals(Square.YELLOW, model.currentPlayer);
    }

    @Test
    public void test_checkForWin(){
        var model = new BoardGameModel();

        assertEquals(Square.NONE,model.checkForWin());

        int BOARD_SIZE = model.BOARD_SIZE;
        for(int i=0; i<BOARD_SIZE; i++){
            model.board[0][i].set(Square.YELLOW);
        }
        model.board[1][0].set(Square.YELLOW);
        model.board[1][BOARD_SIZE-1].set(Square.YELLOW);

        assertEquals(Square.YELLOW,model.checkForWin());

        model = new BoardGameModel();
        for(int i=0; i<BOARD_SIZE; i++){
            model.board[BOARD_SIZE-1][i].set(Square.BLUE);
        }
        model.board[BOARD_SIZE-2][0].set(Square.BLUE);
        model.board[BOARD_SIZE-2][BOARD_SIZE-1].set(Square.BLUE);

        assertEquals(Square.BLUE,model.checkForWin());
    }

    @Test
    public void test_save(){
        var model = new BoardGameModel();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        String logOutput;

        PrintStream originalSystemOut = System.out;
        System.setOut(printStream);

        model.save();
        logOutput = outputStream.toString();
        assertTrue(logOutput.contains("Data was saved successfully"));

        System.setOut(originalSystemOut);
    }

    @Test
    public void test_toString(){
        var model = new BoardGameModel();

        String test =  "1 1 1 1 1 \n1 0 0 0 1 \n0 0 0 0 0 \n2 0 0 0 2 \n2 2 2 2 2 \n";

        assertEquals(test,model.toString());
    }

    @Test
    public void test_Coordinate(){
        var coordinate1 = new Coordinate(1,1);
        var coordinate2 = new Coordinate(1,1);

        assertTrue(coordinate1.equals(coordinate2));
        assertEquals(coordinate1.hashCode(),coordinate2.hashCode());

        coordinate2.setCol(2);
        coordinate2.setRow(2);

        assertFalse(coordinate1.equals(coordinate2));
        assertNotEquals(coordinate1.hashCode(),coordinate2.hashCode());

        String test = "Coordinate{ row=" + coordinate1.getRow() + ", col=" + coordinate1.getCol() + "}";
        assertEquals(test,coordinate1.toString());
    }
}
