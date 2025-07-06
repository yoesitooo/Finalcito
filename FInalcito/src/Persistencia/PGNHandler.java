package Persistencia;

import java.io.*;
import java.time.format.DateTimeFormatter;

public class PGNHandler {
    
    public boolean exportGame(ChessGameData gameData, String filename) {
        if (!filename.endsWith(".pgn")) {
            filename += ".pgn";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            GameMetadata metadata = gameData.getMetadata();
            
            // Write PGN headers
            writer.println("[Event \"" + metadata.getGameName() + "\"]");
            writer.println("[Date \"" + metadata.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "\"]");
            writer.println("[White \"" + metadata.getWhitePlayerName() + "\"]");
            writer.println("[Black \"" + metadata.getBlackPlayerName() + "\"]");
            writer.println("[Result \"" + metadata.getGameResult() + "\"]");
            writer.println();
            
            // Write moves
            int moveNum = 1;
            for (int i = 0; i < gameData.getMoveHistory().size(); i++) {
                if (i % 2 == 0) {
                    writer.print(moveNum + ". ");
                    moveNum++;
                }
                writer.print(gameData.getMoveHistory().get(i) + " ");
                
                if (i % 2 == 1) {
                    writer.println();
                }
            }
            
            // Add result at the end
            writer.println(" " + metadata.getGameResult());
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting PGN: " + e.getMessage());
            return false;
        }
    }
}
