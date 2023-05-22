package boardgame.fileOperations;

import boardgame.model.Coordinate;
import boardgame.model.Square;

/**
 * Used to group match data for saving and loading.
 */
@lombok.Data
public class GameData {
        private Square currentPlayer;
        private Coordinate selectedTile;
        private Square[][] board;

        /**
         * Refer to elements individually
         * @param i The y-axis on the board.
         * @param j The x-axis on the board.
         * @return A single Square element
         */
        public Square getBoardElement(int i, int j){
                return board[i][j];
        }
}
