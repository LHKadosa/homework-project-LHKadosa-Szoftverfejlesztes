package boardgame.fileOperations;

import boardgame.model.BoardGameModel;
import boardgame.model.Coordinate;
import boardgame.model.Square;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Handles the IO operations for saving and loading.
 * The match data is stored in json files using Jackson.
 */
public class FileHandler {
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private String path = BoardGameModel.class.getClassLoader().getResource("").getPath();


    /**
     * It saves the three data that define a game state.
     * The data is stored in {@code gameData_Saved.json}.
     * @param currentPlayer Which player can move their discs.
     * @param selectedTile Which tile is being selected for moving. If the value is {@code null}, the player has not yet selected a disc.
     * @param board Stores the locations of disks on the board.
     */
    public void save(Square currentPlayer, Coordinate selectedTile, Square[][] board){
        try {
        var gameData = new GameData();

        gameData.setCurrentPlayer(currentPlayer);
        gameData.setSelectedTile(selectedTile);
        gameData.setBoard(board);

        Logger.debug(objectMapper.writeValueAsString(gameData));
        try (var writer = new FileWriter(path+ "gameData_Saved.json")) {
            objectMapper.writeValue(writer, gameData);
        }
        Logger.info(objectMapper.readValue(new FileReader(path+ "gameData_Saved.json"),GameData.class));
        }
        catch (Exception e){
            Logger.error("An error occurred while saving!");
            e.printStackTrace();
        }
    }

    /**
     * It loads the three data that define a game state.
     * @param fileType Tells the loader to load the default or the saved game state. It can only contain the text {@code Default} or {@code Saved}
     * @return An instance of the GameData, filled with the loaded information.
     */
    public GameData load(String fileType){
        try {
            File file = new File(path + "gameData_"+fileType+".json");
            return objectMapper.readValue(file, GameData.class);
        }
        catch (Exception e){
            Logger.error("An error occurred while loading!");
            e.printStackTrace();
            return new GameData();
        }
    }
}
