package Persistencia;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class GameMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String gameName;
    private LocalDateTime creationDate;
    private LocalDateTime lastModified;
    private String whitePlayerName;
    private String blackPlayerName;
    private String gameResult;
    private boolean gameFinished;
    private int totalMoves;
    private long gameDurationSeconds;
    private List<String> moveHistory;
    
    public GameMetadata() {
        this.creationDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.moveHistory = new ArrayList<>();
        this.gameResult = "In Progress";
        this.gameFinished = false;
        this.whitePlayerName = "Player 1";
        this.blackPlayerName = "Player 2";
        this.gameName = "New Game";
    }
    
    // Getters and setters
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { 
        this.gameName = gameName != null ? gameName : "Unnamed Game"; 
    }
    
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { 
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now(); 
    }
    
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { 
        this.lastModified = lastModified != null ? lastModified : LocalDateTime.now(); 
    }
    
    public String getWhitePlayerName() { return whitePlayerName; }
    public void setWhitePlayerName(String whitePlayerName) { 
        this.whitePlayerName = whitePlayerName != null ? whitePlayerName : "Player 1"; 
    }
    
    public String getBlackPlayerName() { return blackPlayerName; }
    public void setBlackPlayerName(String blackPlayerName) { 
        this.blackPlayerName = blackPlayerName != null ? blackPlayerName : "Player 2"; 
    }
    
    public String getGameResult() { return gameResult; }
    public void setGameResult(String gameResult) { 
        this.gameResult = gameResult != null ? gameResult : "In Progress";
        this.gameFinished = !this.gameResult.equals("In Progress");
    }
    
    public boolean isGameFinished() { return gameFinished; }
    public void setGameFinished(boolean gameFinished) { this.gameFinished = gameFinished; }
    
    public int getTotalMoves() { return totalMoves; }
    public void setTotalMoves(int totalMoves) { this.totalMoves = Math.max(0, totalMoves); }
    
    public long getGameDurationSeconds() { return gameDurationSeconds; }
    public void setGameDurationSeconds(long gameDurationSeconds) { 
        this.gameDurationSeconds = Math.max(0, gameDurationSeconds); 
    }
    
    public List<String> getMoveHistory() { 
        return moveHistory != null ? moveHistory : new ArrayList<>(); 
    }
    public void setMoveHistory(List<String> moveHistory) { 
        this.moveHistory = moveHistory != null ? moveHistory : new ArrayList<>(); 
    }
    
    // Utility methods
    public String getFormattedDuration() {
        try {
            Duration duration = Duration.ofSeconds(gameDurationSeconds);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            
            if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
            return String.format("%02d:%02d", minutes, seconds);
        } catch (Exception e) {
            return "00:00";
        }
    }
    
    public String getGameResultDescription() {
        if (gameResult == null) return "In Progress";
        
        switch (gameResult) {
            case "1-0": return "White wins";
            case "0-1": return "Black wins"; 
            case "1/2-1/2": return "Draw";
            case "In Progress": return "In Progress";
            default: return gameResult;
        }
    }
    
    public String getFormattedFileSize() {
        // Placeholder - actual implementation would calculate real file size
        return "< 1 KB";
    }
    
    @Override
    public String toString() {
        return String.format("GameMetadata{name='%s', moves=%d, result='%s'}", 
            gameName, totalMoves, gameResult);
    }
}