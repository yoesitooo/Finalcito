package Persistencia;

import kernel.tablero.Tablero;
import java.util.List;

public class ChessGameData {
    private Tablero tablero;
    private GameMetadata metadata;
    private List<String> moveHistory;
    
    public Tablero getTablero() { return tablero; }
    public void setTablero(Tablero tablero) { this.tablero = tablero; }
    
    public GameMetadata getMetadata() { return metadata; }
    public void setMetadata(GameMetadata metadata) { this.metadata = metadata; }
    
    public List<String> getMoveHistory() { return moveHistory; }
    public void setMoveHistory(List<String> moveHistory) { this.moveHistory = moveHistory; }
}
