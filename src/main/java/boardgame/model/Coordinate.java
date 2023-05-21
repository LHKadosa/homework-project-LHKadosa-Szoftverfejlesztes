package boardgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {
    private Integer row;
    private Integer col;

    public Integer getRow(){
        return row;
    }

    public Integer getCol(){
        return col;
    }

    @JsonCreator
    public Coordinate(@JsonProperty("row") Integer row,@JsonProperty("col") Integer col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Coordinate{ row=" + row + ", col=" + col + "}";
    }
}
