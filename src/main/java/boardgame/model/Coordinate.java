package boardgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data structure, used for handling the previously selected field.
 * It has a row and a col (column) variable.
 */
@lombok.Data
public class Coordinate {
    private Integer row;
    private Integer col;

    /**
     * Basic constructor with Json compatible annotations.
     * @param row The y-axis on the board.
     * @param col The x-axis on the board.
     */
    @JsonCreator
    public Coordinate(@JsonProperty("row") Integer row,@JsonProperty("col") Integer col) {
        this.row = row;
        this.col = col;
    }

    /** It returns the class data in the following form: Coordinate{ row=[row], col=[col]}. */
    @Override
    public String toString() {
        return "Coordinate{ row=" + row + ", col=" + col + "}";
    }
}
