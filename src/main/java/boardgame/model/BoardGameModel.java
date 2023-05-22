package boardgame.model;

import boardgame.fileOperations.FileHandler;
import boardgame.fileOperations.GameData;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import org.tinylog.Logger;

import java.util.SimpleTimeZone;

/**
 * Defines the rules of the game and manages them
 */
public class BoardGameModel {

    private static FileHandler fileHandler = new FileHandler();
    public static final int BOARD_SIZE = 5;
    public Square currentPlayer;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private ReadOnlyObjectWrapper<Coordinate> selectedTile = new ReadOnlyObjectWrapper<>();

    /** Initializes the board structure and loads the starting game state. */
    public BoardGameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
            }
        }
        load("Default");
    }

    /** Used by the Controller to be able to subscribe to the changes of the board state. */
    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }
    /** Used by the Controller to be able to subscribe to the changes of the selected discs. */
    public ReadOnlyObjectProperty<Coordinate> selectedProperty() {
        return selectedTile.getReadOnlyProperty();
    }

    /**
     * Interprets disk selection and executes the appropriate instructions according to the game rules.
     * It will ignore the input and returns with {@code false} if:
     *  -the selected disc belongs to the other player.
     *  -the player selects a destination before selecting the disc to be moved.
     *  -the destination is NOT a diagonal neighbor of the disc the player wants to move.
     * It will execute the appropriate instructions and returns with {@code true} if:
     *  -the player selects a disc of its own.
     *  -the destination is a diagonal neighbor of the disc the player wants to move.
     * @param i The y coordinate of the selected disc.
     * @param j The x coordinate of the selected disc.
     * @return The success of the process. If a square is saved in the selectedTile or a disc was moved it return {@code true}, otherwise {@code false}.
     */
    public boolean inputManager(int i, int j){
        if(isOpponentSquare(i,j)){
            Logger.warn("Invalid input! You cannot select the other player's square");
            return false;
        }

        if(board[i][j].get() == Square.NONE){
            if(selectedTile.get() == null){
                Logger.warn("Invalid input! You need to select a square first");
                return false;
            }
            else if(!isValidMove(i,j,selectedTile.get().getRow(), selectedTile.get().getCol())){
                Logger.warn("Invalid input! The destination must be a diagonal square");
                return false;
            }
        }

        if(board[i][j].get() == Square.NONE){
            Logger.debug("The square has been moved");
            move(i,j,selectedTile.get().getRow(), selectedTile.get().getCol());
            nextTurn();
        }
        else{
            Logger.debug("The square has been selected");
            selectedTile.set(new Coordinate(i,j));
        }
        return true;
    }

    /**
     * Moves a selected disc to a new position.
     * It assumes that the feasibility of the move has been checked in advance.
     * @param toRow Destination row.
     * @param toCol Destination column.
     * @param fromRow Row of disk to be moved.
     * @param fromCol Column of disk to be moved.
     */
    public void move(int toRow, int toCol, int fromRow, int fromCol){
        board[toRow][toCol].set(currentPlayer);
        board[fromRow][fromCol].set(Square.NONE);
    }

    /** Returns {@code true} if the player selects the opponent's disc. */
    public boolean isOpponentSquare(int i, int j){
        if(board[i][j].get() != Square.NONE && board[i][j].get() != currentPlayer) return true;
        return false;
    }

    /** Returns {@code true} if the destination is a diagonal neighbor of the disc the player wants to move. */
    public boolean isValidMove(int toRow, int toCol, int fromRow, int fromCol){
        return Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1;
    }

    /**
     * Handles round switching.
     * It calls the {@code checkForWin()} methode and declares the winner using {@code winnerAlert(winner)} if there is one.
     * If there is no winner, it passes the round to the other player using the {@code nextPlayer()} methode.
     */
    public void nextTurn(){
        Square winner = checkForWin();
        if(winner == Square.BLUE){
            winnerAlert("Blue");
        }
        else if(winner == Square.YELLOW){
            winnerAlert("Yellow");
        }
        else nextPlayer();
    }

    /** Resets the {@code selectedTile} variable and switches to the other player. */
    public void nextPlayer(){
        selectedTile.set(null);
        currentPlayer = switch(currentPlayer){
            case BLUE -> Square.YELLOW;
            default -> Square.BLUE;
        };
    }

    /** Returns with the winning color. If there is no winner, then it returns with {@code NONE}*/
    public Square checkForWin(){
        Boolean winChecker = true;
        for(int i=0; i<BOARD_SIZE; i++)
            if (board[0][i].get() != Square.YELLOW) winChecker = false;
        if(board[1][0].get() != Square.YELLOW) winChecker = false;
        if(board[1][BOARD_SIZE-1].get() != Square.YELLOW) winChecker = false;

        if(winChecker) return Square.YELLOW;

        winChecker = true;
        for(int i=0; i<BOARD_SIZE; i++)
            if (board[BOARD_SIZE-1][i].get() != Square.BLUE) winChecker = false;
        if(board[BOARD_SIZE-2][0].get() != Square.BLUE) winChecker = false;
        if(board[BOARD_SIZE-2][BOARD_SIZE-1].get() != Square.BLUE) winChecker = false;

        if(winChecker) return Square.BLUE;

        return Square.NONE;
    }

    /**
     * Creates a pop-up window where it congratulates the winner.
     * After the player closes the pop-up, it resets the board.
     * @param winner The name of the winner.
     */
    public void winnerAlert(String winner){
        Logger.info("{} wins!",winner);
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Congratulations "+winner+"! You won!");
        alert.showAndWait();
        load("Default");
    }

    /** Passes the game state to the {@code FileHandler} for saving. */
    public void save(){
        Square[][] tempBoard = new Square[BOARD_SIZE][BOARD_SIZE];
        for(int i=0;i<BOARD_SIZE;i++)
            for(int j=0;j<BOARD_SIZE;j++)
                tempBoard[i][j] = board[i][j].get();

        fileHandler.save(currentPlayer, selectedTile.get(), tempBoard);
        Logger.debug("Data was saved successfully");
    }

    /**
     * Receives the game state from the {@code FileHandler} by loading.
     * @param fileType Tells the loader to load the default or the saved game state. It can only contain the text {@code Default} or {@code Saved}
     */
    public void load(String fileType){
        GameData gameData = fileHandler.load(fileType);

        currentPlayer = gameData.getCurrentPlayer();
        selectedTile.set(gameData.getSelectedTile());
        for(int i=0;i<BOARD_SIZE;i++)
            for(int j=0;j<BOARD_SIZE;j++)
                board[i][j].set(gameData.getBoardElement(i,j));
        Logger.trace(gameData);
        Logger.debug("Data was loaded successfully");
    }

    /** Returns an int matrix from the board. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new BoardGameModel();
        Logger.trace(model);
    }

}
