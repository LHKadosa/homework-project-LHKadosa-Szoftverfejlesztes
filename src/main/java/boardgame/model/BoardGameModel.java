package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardGameModel {

    public static final int BOARD_SIZE = 5;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

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

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public void move(int i, int j) {
        board[i][j].set(
                switch (board[i][j].get()) {
                    case NONE -> Square.BLUE;
                    case BLUE -> Square.YELLOW;
                    case YELLOW -> Square.NONE;
                }
        );
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

    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
