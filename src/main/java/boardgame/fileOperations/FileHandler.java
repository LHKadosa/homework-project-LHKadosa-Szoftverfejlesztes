package boardgame.fileOperations;

import boardgame.model.BoardGameModel;
import boardgame.model.Coordinate;
import boardgame.model.Square;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileReader;
import java.io.FileWriter;

public class FileHandler {

    public void save(int BOARD_SIZE, Square currentPlayer, Coordinate selectedTile, Square[][] board) throws Exception{
        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String path = BoardGameModel.class.getClassLoader().getResource("").getPath();
        var gameData = new GameData();

        gameData.setCurrentPlayer(currentPlayer);
        gameData.setSelectedTile(selectedTile);
        gameData.setBoard(board);

        System.out.println(objectMapper.writeValueAsString(gameData));
        try (var writer = new FileWriter(path+"gameData_Saved.json")) {
            objectMapper.writeValue(writer, gameData);
        }
        System.out.println(objectMapper.readValue(new FileReader(path+"gameData_Saved.json"),GameData.class));
        System.out.println("Data was saved successfully");
    }
}
