import boardgame.fileOperations.FileHandler;
import boardgame.fileOperations.GameData;
import boardgame.model.Coordinate;
import boardgame.model.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass_FileOperations {

    @Test
    public void test_SaveLoad(){
        var fileHandler = new FileHandler();
        var gameData = new GameData();

        gameData.setCurrentPlayer(Square.BLUE);
        gameData.setSelectedTile(null);
        gameData.setBoard(createDefaultBoard());

        fileHandler.save(gameData.getCurrentPlayer(),gameData.getSelectedTile(),gameData.getBoard());
        var newGameDataSaved = fileHandler.load("Saved");
        var newGameDataDefault = fileHandler.load("Default");

        assertEquals(gameData,newGameDataSaved);
        assertEquals(gameData,newGameDataDefault);
    }

    @Test
    public void test_GameDataAdditional(){
        var gameData1 = new GameData();
        var gameData2 = new GameData();

        gameData1.setCurrentPlayer(Square.BLUE);
        gameData1.setSelectedTile(null);
        gameData1.setBoard(createDefaultBoard());

        gameData2.setCurrentPlayer(Square.BLUE);
        gameData2.setSelectedTile(null);
        gameData2.setBoard(createDefaultBoard());

        assertTrue(gameData1.equals(gameData2));
        assertEquals(gameData1.hashCode(),gameData2.hashCode());

        gameData2.setCurrentPlayer(Square.YELLOW);
        gameData2.setSelectedTile(new Coordinate(1,1));
        assertFalse(gameData1.equals(gameData2));
        assertNotEquals(gameData1.hashCode(),gameData2.hashCode());
    }

    public Square[][] createDefaultBoard(){
        int BOARD_SIZE=5;
        Square[][] board = new Square[BOARD_SIZE][BOARD_SIZE];
        for(int i=0;i<BOARD_SIZE;i++)
            for(int j=0;j<BOARD_SIZE;j++)
                board[i][j] = Square.NONE;
        for(int i=0; i<BOARD_SIZE; i++){
            board[0][i]=Square.BLUE;
            board[BOARD_SIZE-1][i]=Square.YELLOW;
        }
        board[1][0]=Square.BLUE;
        board[1][BOARD_SIZE-1]=Square.BLUE;
        board[BOARD_SIZE-2][0]=Square.YELLOW;
        board[BOARD_SIZE-2][BOARD_SIZE-1]=Square.YELLOW;
        return board;
    }
}
