package boardgame.model;

public class Coordinate {
    private Integer row;
    private Integer col;

    public Integer getRow(){
        return row;
    }

    public Integer getCol(){
        return col;
    }

    public Coordinate(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
