package boardgame.fileOperations;

import boardgame.model.BoardGameModel;
import boardgame.model.Coordinate;
import boardgame.model.Square;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileHandler {
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private String path = BoardGameModel.class.getClassLoader().getResource("").getPath();


    public void save(int BOARD_SIZE, Square currentPlayer, Coordinate selectedTile, Square[][] board) throws Exception{
        var gameData = new GameData();

        gameData.setCurrentPlayer(currentPlayer);
        gameData.setSelectedTile(selectedTile);
        gameData.setBoard(board);

        System.out.println(objectMapper.writeValueAsString(gameData));
        try (var writer = new FileWriter(path+ "gameData_Saved.json")) {
            objectMapper.writeValue(writer, gameData);
        }
        System.out.println(objectMapper.readValue(new FileReader(path+ "gameData_Saved.json"),GameData.class));
    }

    public GameData load(String fileType) throws Exception{
        File file = new File(path + "gameData_"+fileType+".json");
        return objectMapper.readValue(file, GameData.class);
    }
}
