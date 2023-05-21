package boardgame.fileOperations;

import boardgame.model.Coordinate;
import boardgame.model.Square;

@lombok.Data
public class GameData {
        private Square currentPlayer;
        private Coordinate selectedTile;
        private Square[][] board;

        public Square getBoardElement(int i, int j){
                return board[i][j];
        }
}
