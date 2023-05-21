package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardGameModel {

    public static final int BOARD_SIZE = 5;
    private Square currentPlayer = Square.BLUE;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private ReadOnlyObjectWrapper<Coordinate> selectedTile = new ReadOnlyObjectWrapper<>();

    public BoardGameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
            }
        }
        setupBoard();
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }
    public ReadOnlyObjectProperty<Coordinate> selectedProperty() {
        return selectedTile.getReadOnlyProperty();
    }


    public boolean inputManager(int i, int j){
        if(isOpponentSquare(i,j)){
            System.out.println("Invalid input! You cannot select the other player's square");
            return false;
        }

        if(board[i][j].get() == Square.NONE){
            if(selectedTile.get() == null){
                System.out.println("Invalid input! You need to select a square first");
                return false;
            }
            else if(!isNeighbour(i,j,selectedTile.get().getRow(), selectedTile.get().getCol())){
                System.out.println("Invalid input! The destination must be a neighbour square");
                return false;
            }
        }

        if(board[i][j].get() == Square.NONE){
            System.out.println("The square has been moved");
            move(i,j,selectedTile.get().getRow(), selectedTile.get().getCol());
            nextTurn();
        }
        else{
            System.out.println("The square has been selected");
            selectedTile.set(new Coordinate(i,j));
        }
        return true;
    }

    public void move(int toRow, int toCol, int fromRow, int fromCol){
        board[toRow][toCol].set(currentPlayer);
        board[fromRow][fromCol].set(Square.NONE);
    }

    public boolean isOpponentSquare(int i, int j){
        if(board[i][j].get() != Square.NONE && board[i][j].get() != currentPlayer) return true;
        return false;
    }

    public boolean isNeighbour(int toRow, int toCol, int fromRow, int fromCol){
        return Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1;
    }

    public void nextTurn(){
        Square winner = checkForWin();
        if(winner == Square.BLUE){
            System.out.println("BLUE wins!");
        }
        else if(winner == Square.YELLOW){
            System.out.println("YELLOW wins!");
        }
        nextPlayer();
    }

    public void nextPlayer(){
        selectedTile.set(null);
        currentPlayer = switch(currentPlayer){
            case BLUE -> Square.YELLOW;
            default -> Square.BLUE;
        };
    }

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

    public void setupBoard(){
        for(int i=0; i<BOARD_SIZE; i++){
            board[0][i].set(Square.BLUE);
            board[BOARD_SIZE-1][i].set(Square.YELLOW);
        }
        board[1][0].set(Square.BLUE);
        board[1][BOARD_SIZE-1].set(Square.BLUE);
        board[BOARD_SIZE-2][0].set(Square.YELLOW);
        board[BOARD_SIZE-2][BOARD_SIZE-1].set(Square.YELLOW);
    }

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
        System.out.println(model);
    }

}
