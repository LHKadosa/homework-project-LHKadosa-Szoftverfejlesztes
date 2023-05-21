package boardgame;

import boardgame.model.BoardGameModel;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class BoardGameController {

    @FXML
    private GridPane board;

    @FXML
    private Button save;

    @FXML
    private Button load;
    @FXML
    private Button reset;

    private BoardGameModel model = new BoardGameModel();

    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }

        save.setOnMouseClicked(this::handleMouseClickSave);
        load.setOnMouseClicked(this::handleMouseClickLoad);
        reset.setOnMouseClicked(this::handleMouseClickReset);
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(50);
        piece.setStrokeWidth(5);


        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.squareProperty(i, j));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(i, j).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case BLUE -> Color.BLUE;
                            case YELLOW -> Color.YELLOW;
                        };
                    }
                }
        );

        piece.strokeProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.selectedProperty());
                    }
                    @Override
                    protected Paint computeValue() {
                        if(model.selectedProperty().get() == null) return Color.TRANSPARENT;
                        if(model.selectedProperty().get().getRow() == i && model.selectedProperty().get().getCol() == j)
                            return Color.BLACK;
                        else
                            return Color.TRANSPARENT;
                    }
                }
        );

        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClickSquare);
        return square;
    }

    @FXML
    private void handleMouseClickSquare(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        System.out.printf("Click on square (%d,%d)%n", row, col);
        model.inputManager(row, col);
    }

    @FXML
    private void handleMouseClickSave(MouseEvent event){
        System.out.println("Saving...");
        try {
            model.save();
        } catch (Exception e){
            System.out.println("An error occurred while saving!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMouseClickLoad(MouseEvent event){
        System.out.println("Loading...");
        try {
            model.load();
        } catch (Exception e){
            System.out.println("An error occurred while loading!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMouseClickReset(MouseEvent event){
        System.out.println("RESET");
        //model.reset();
    }

}
