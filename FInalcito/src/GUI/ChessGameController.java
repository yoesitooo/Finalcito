package GUI;

import kernel.jugador.*;
import kernel.piezas.*;
import kernel.tablero.*;
import javax.swing.*;

import Persistencia.ChessGameData;
import Persistencia.GameDataManager;
import Persistencia.GameMetadata;
import Persistencia.PGNHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Main Chess Game Controller - Integrates perfectly with your logic system
 */
public class ChessGameController {
    
    // Integration with YOUR logic system
    private Tablero currentBoard;
    private GameMetadata currentGameData;
    
    // GUI Components
    private ChessMainWindow gameWindow;
    private Timer gameTimer;
    private Timer autoSaveTimer;
    
    // Game state
    private long gameStartTime;
    private boolean gameInProgress;
    private Stack<Tablero> undoStack;
    private Stack<Tablero> redoStack;
    private List<String> moveHistory;
    private int moveCounter = 0;
    
    // Auto-save and persistence
    private static final int AUTO_SAVE_INTERVAL = 600000; // 10 minutes
    private static final int MOVES_PER_AUTO_SAVE = 10;
    
    public ChessGameController() {
        initializeController();
    }
    
    private void initializeController() {
        // Initialize data structures
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        moveHistory = new ArrayList<>();
        
        // Create main game window
        gameWindow = new ChessMainWindow(this);
        
        // Setup timers
        setupGameTimer();
        setupAutoSaveTimer();
        
        // Test persistence on startup
        System.out.println("Testing persistence system...");
        if (GameDataManager.testPersistence()) {
            System.out.println("Persistence system working correctly!");
        } else {
            System.err.println("WARNING: Persistence system has issues!");
        }
    }
    
    /**
     * Start a new game using YOUR Tablero.crearTableroStandar()
     */
    public void startNewGame() {
        try {
            // Create new game using YOUR logic
            currentBoard = Tablero.crearTableroStandar();
            
            // Initialize game metadata
            currentGameData = new GameMetadata();
            currentGameData.setGameName(generateGameName());
            currentGameData.setCreationDate(LocalDateTime.now());
            currentGameData.setWhitePlayerName("White Player");
            currentGameData.setBlackPlayerName("Black Player");
            
            // Reset game state
            resetGameState();
            
            // Update GUI
            gameWindow.updateBoardDisplay();
            gameWindow.updateGameStatus("New game started - White to move");
            gameWindow.clearMoveHistory();
            gameWindow.updatePlayerTurn(true); // White starts
            
            gameInProgress = true;
            gameTimer.start();
            
            System.out.println("New game started successfully");
            
        } catch (Exception e) {
            System.err.println("Error starting new game: " + e.getMessage());
            e.printStackTrace();
            gameWindow.showMessage("Error starting new game: " + e.getMessage(), "Error");
        }
    }
    
    /**
     * Process move from GUI clicks - integrates with YOUR FabricarMovimiento
     */
    public boolean processMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!gameInProgress) {
            gameWindow.showMessage("Game is not in progress", "Invalid Move");
            return false;
        }
        
        try {
            // Convert GUI coordinates to YOUR coordinate system (0-63)
            int coordActual = fromRow * 8 + fromCol;
            int coordDestino = toRow * 8 + toCol;
            
            // Use YOUR FabricarMovimiento to create/validate movement
            Movimiento movimiento = FabricarMovimiento.crearMovimiento(
                currentBoard, coordActual, coordDestino
            );
            
            // Check if movement is valid using YOUR system
            if (movimiento == Movimiento.MOVIMIENTO_NULO) {
                gameWindow.showMessage("Invalid move", "Move Error");
                return false;
            }
            
            // Save current board state for undo
            undoStack.push(currentBoard);
            redoStack.clear();
            
            // Execute movement using YOUR Jugador.hacerMovimiento()
            Jugador currentPlayer = currentBoard.jugadorActual();
            Transicion transicion = currentPlayer.hacerMovimiento(movimiento);
            
            // Check if movement was successful using YOUR StatusDeMovimiento
            if (!transicion.getStatusDeMovimiento().estaHecho()) {
                undoStack.pop(); // Remove the saved state since move failed
                
                String errorMessage = getErrorMessage(transicion.getStatusDeMovimiento());
                gameWindow.showMessage(errorMessage, "Move Error");
                return false;
            }
            
            // Get new board state from YOUR Transicion
            currentBoard = transicion.getTableroTransicion();
            
            // Generate move notation
            String moveNotation = generateMoveNotation(movimiento);
            moveHistory.add(moveNotation);
            moveCounter++;
            
            // Handle special moves
            handleSpecialMoves(movimiento);
            
            // Update game metadata
            updateGameMetadata(movimiento);
            
            // Check game end conditions using YOUR logic
            checkGameEndConditions();
            
            // Update GUI
            gameWindow.updateBoardDisplay();
            gameWindow.addMoveToHistory(moveNotation, moveCounter);
            gameWindow.updatePlayerTurn(currentBoard.jugadorActual().getEquipo().esBlanca());
            
            // Auto-save every N moves
            if (moveCounter % MOVES_PER_AUTO_SAVE == 0) {
                autoSaveGame();
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error processing move: " + e.getMessage());
            e.printStackTrace();
            gameWindow.showMessage("Error processing move: " + e.getMessage(), "Move Error");
            return false;
        }
    }
    
    /**
     * Get valid moves for a piece - integrates with YOUR Pieza.movimientosLegales()
     */
    public List<int[]> getValidMoves(int row, int col) {
        List<int[]> validMoves = new ArrayList<>();
        
        try {
            int coordenada = row * 8 + col;
            Casilla casilla = currentBoard.getCasilla(coordenada);
            
            if (casilla.estaCasillaOcupada()) {
                Pieza pieza = casilla.getPieza();
                
                // Check if it's the current player's piece
                Jugador currentPlayer = currentBoard.jugadorActual();
                if (pieza.getEquipo() == currentPlayer.getEquipo()) {
                    
                    // Get legal moves using YOUR Pieza.movimientosLegales()
                    var movimientos = pieza.movimientosLegales(currentBoard);
                    
                    // Convert to GUI coordinates
                    for (Movimiento mov : movimientos) {
                        // Validate move can actually be made
                        if (currentPlayer.esMovimientoLegal(mov)) {
                            int destRow = mov.getCoordDestino() / 8;
                            int destCol = mov.getCoordDestino() % 8;
                            validMoves.add(new int[]{destRow, destCol});
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting valid moves: " + e.getMessage());
        }
        
        return validMoves;
    }
    
    /**
     * Check if square is under attack - integrates with YOUR Jugador system
     */
    public boolean isSquareUnderAttack(int row, int col, boolean byWhite) {
        try {
            int coordenada = row * 8 + col;
            
            Jugador attackingPlayer = byWhite ? 
                currentBoard.jugadorBlanco() : currentBoard.jugadorNegro();
            
            // Check if any of the attacking player's moves target this square
            for (Movimiento mov : attackingPlayer.getMovLegales()) {
                if (mov.getCoordDestino() == coordenada) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking square attack: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get piece at position - integrates with YOUR Casilla system
     */
    public Pieza getPieceAt(int row, int col) {
        try {
            int coordenada = row * 8 + col;
            Casilla casilla = currentBoard.getCasilla(coordenada);
            
            return casilla.estaCasillaOcupada() ? casilla.getPieza() : null;
        } catch (Exception e) {
            System.err.println("Error getting piece: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Undo last move
     */
    public boolean undoMove() {
        if (undoStack.isEmpty()) {
            gameWindow.showMessage("No moves to undo", "Undo");
            return false;
        }
        
        try {
            // Save current state for redo
            redoStack.push(currentBoard);
            
            // Restore previous state
            currentBoard = undoStack.pop();
            
            // Remove last move from history
            if (!moveHistory.isEmpty()) {
                moveHistory.remove(moveHistory.size() - 1);
                moveCounter--;
            }
            
            // Update GUI
            gameWindow.updateBoardDisplay();
            gameWindow.removeLastMoveFromHistory();
            gameWindow.updatePlayerTurn(currentBoard.jugadorActual().getEquipo().esBlanca());
            gameWindow.updateGameStatus("Move undone");
            
            return true;
            
        } catch (Exception e) {
            gameWindow.showMessage("Error undoing move: " + e.getMessage(), "Undo Error");
            return false;
        }
    }
    
    /**
     * Redo last undone move
     */
    public boolean redoMove() {
        if (redoStack.isEmpty()) {
            gameWindow.showMessage("No moves to redo", "Redo");
            return false;
        }
        
        try {
            // Save current state for undo
            undoStack.push(currentBoard);
            
            // Restore next state
            currentBoard = redoStack.pop();
            
            // Update GUI
            gameWindow.updateBoardDisplay();
            gameWindow.updatePlayerTurn(currentBoard.jugadorActual().getEquipo().esBlanca());
            gameWindow.updateGameStatus("Move redone");
            
            return true;
            
        } catch (Exception e) {
            gameWindow.showMessage("Error redoing move: " + e.getMessage(), "Redo Error");
            return false;
        }
    }
    
    /**
     * Save current game
     */
    public boolean saveGame(String gameName) {
        try {
            if (gameName == null || gameName.trim().isEmpty()) {
                gameName = currentGameData.getGameName();
            }
            
            // Update game metadata
            currentGameData.setGameName(gameName);
            currentGameData.setLastModified(LocalDateTime.now());
            currentGameData.setTotalMoves(moveCounter);
            currentGameData.setMoveHistory(new ArrayList<>(moveHistory));
            
            // Convert YOUR Tablero to saveable format
            ChessGameData saveData = new ChessGameData();
            saveData.setTablero(currentBoard);
            saveData.setMetadata(currentGameData);
            saveData.setMoveHistory(moveHistory);
            
            // Save using persistence manager
            boolean success = GameDataManager.saveGame(saveData, gameName);
            
            if (success) {
                gameWindow.updateGameStatus("Game saved: " + gameName);
                System.out.println("Game saved successfully: " + gameName);
            } else {
                gameWindow.showMessage("Failed to save game", "Save Error");
                System.err.println("Failed to save game: " + gameName);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            gameWindow.showMessage("Error saving game: " + e.getMessage(), "Save Error");
            return false;
        }
    }
    
    /**
     * Load a saved game
     */
    public boolean loadGame(String gameName) {
        try {
            ChessGameData loadedData = GameDataManager.loadGame(gameName);
            
            if (loadedData == null) {
                gameWindow.showMessage("Game not found: " + gameName, "Load Error");
                return false;
            }
            
            // Restore YOUR Tablero
            currentBoard = loadedData.getTablero();
            currentGameData = loadedData.getMetadata();
            moveHistory = new ArrayList<>(loadedData.getMoveHistory());
            moveCounter = moveHistory.size();
            
            // Reset stacks
            undoStack.clear();
            redoStack.clear();
            
            // Update game state
            gameInProgress = !currentGameData.isGameFinished();
            
            // Update GUI
            gameWindow.updateBoardDisplay();
            gameWindow.loadMoveHistory(moveHistory);
            gameWindow.updatePlayerTurn(currentBoard.jugadorActual().getEquipo().esBlanca());
            gameWindow.updateGameStatus("Game loaded: " + gameName);
            
            if (gameInProgress) {
                gameTimer.start();
            }
            
            System.out.println("Game loaded successfully: " + gameName);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            gameWindow.showMessage("Error loading game: " + e.getMessage(), "Load Error");
            return false;
        }
    }
    
    // Helper methods
    private void resetGameState() {
        gameStartTime = System.currentTimeMillis();
        gameInProgress = false;
        moveCounter = 0;
        undoStack.clear();
        redoStack.clear();
        moveHistory.clear();
    }
    
    private void setupGameTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInProgress) {
                    long elapsedSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
                    gameWindow.updateGameTime(elapsedSeconds);
                }
            }
        });
    }
    
    private void setupAutoSaveTimer() {
        autoSaveTimer = new Timer(AUTO_SAVE_INTERVAL, e -> autoSaveGame());
        autoSaveTimer.start();
    }
    
    private void autoSaveGame() {
        if (gameInProgress && moveCounter > 0) {
            String autoSaveName = "AutoSave_" + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
            saveGame(autoSaveName);
        }
    }
    
    private String generateGameName() {
        return "Game_" + java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }
    
    private String getErrorMessage(StatusDeMovimiento status) {
        if (status == StatusDeMovimiento.DEJA_A_JUGADOR_EN_JAQUE) {
            return "Cannot leave your king in check";
        } else if (status == StatusDeMovimiento.MOVIMIENTO_ILEGAL) {
            return "Illegal move";
        }
        return "Move not allowed";
    }
    
    private String generateMoveNotation(Movimiento movimiento) {
        try {
            Pieza pieza = movimiento.getPiezaMovida();
            String pieceSymbol = getPieceNotation(pieza.getTipoDePieza());
            
            // Convert coordinates to algebraic notation
            String fromSquare = coordinateToAlgebraic(pieza.getPosicion());
            String toSquare = coordinateToAlgebraic(movimiento.getCoordDestino());
            
            StringBuilder notation = new StringBuilder();
            
            // Handle special moves
            if (movimiento instanceof MovimientoEnroqueCorto) {
                return "O-O";
            } else if (movimiento instanceof MovimientoEnroqueLargo) {
                return "O-O-O";
            } else if (movimiento instanceof MovimientoPeonCapturaAlPaso) {
                notation.append(fromSquare.charAt(0)).append("x").append(toSquare).append(" e.p.");
            } else if (movimiento.esAtaque()) {
                // Capture notation
                if (pieza.getTipoDePieza() == TipoDePieza.PEON) {
                    notation.append(fromSquare.charAt(0)); // Pawn captures show file
                } else {
                    notation.append(pieceSymbol);
                }
                notation.append("x").append(toSquare);
            } else {
                // Normal move
                if (pieza.getTipoDePieza() != TipoDePieza.PEON) {
                    notation.append(pieceSymbol);
                }
                notation.append(toSquare);
            }
            
            // Check for pawn promotion
            if (pieza.getTipoDePieza() == TipoDePieza.PEON) {
                int destRow = movimiento.getCoordDestino() / 8;
                if (destRow == 0 || destRow == 7) {
                    notation.append("=Q"); // Default to Queen
                }
            }
            
            return notation.toString();
            
        } catch (Exception e) {
            System.err.println("Error generating move notation: " + e.getMessage());
            return "Move";
        }
    }
    
    private String coordinateToAlgebraic(int coordinate) {
        int row = coordinate / 8;
        int col = coordinate % 8;
        char file = (char)('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }
    
    private String getPieceNotation(TipoDePieza tipo) {
        switch (tipo) {
            case REY: return "K";
            case REINA: return "Q";
            case TORRE: return "R";
            case ALFIL: return "B";
            case CABALLO: return "N";
            case PEON: return "";
            default: return "";
        }
    }
    
    private void handleSpecialMoves(Movimiento movimiento) {
        try {
            // Handle pawn promotion
            if (movimiento.getPiezaMovida().getTipoDePieza() == TipoDePieza.PEON) {
                int destRow = movimiento.getCoordDestino() / 8;
                if (destRow == 0 || destRow == 7) {
                    handlePawnPromotion(movimiento.getCoordDestino());
                }
            }
            
            // Play appropriate sound effects
            if (movimiento instanceof MovimientoEnroque) {
                SoundManager.playCastleSound();
            } else if (movimiento instanceof MovimientoPeonCapturaAlPaso) {
                SoundManager.playEnPassantSound();
            } else if (movimiento.esAtaque()) {
                SoundManager.playCaptureSound();
            } else {
                SoundManager.playMoveSound();
            }
            
        } catch (Exception e) {
            System.err.println("Error handling special moves: " + e.getMessage());
        }
    }
    
    private void handlePawnPromotion(int position) {
        SwingUtilities.invokeLater(() -> {
            PromotionDialog dialog = new PromotionDialog(gameWindow);
            dialog.setVisible(true);
            
            TipoDePieza promotionPiece = dialog.getSelectedPiece();
            if (promotionPiece != null) {
                // Create new promoted piece using YOUR Builder system
                Casilla casilla = currentBoard.getCasilla(position);
                if (casilla.estaCasillaOcupada()) {
                    Pieza peon = casilla.getPieza();
                    
                    // Create new board with promoted piece
                    Tablero.Builder builder = new Tablero.Builder();
                    
                    // Copy all pieces except the promoted pawn
                    for (int i = 0; i < 64; i++) {
                        Casilla c = currentBoard.getCasilla(i);
                        if (c.estaCasillaOcupada() && i != position) {
                            builder.setPieza(c.getPieza());
                        }
                    }
                    
                    // Add promoted piece
                    Pieza promotedPiece = createPromotedPiece(promotionPiece, peon.getEquipo(), position);
                    builder.setPieza(promotedPiece);
                    
                    // Set current player
                    builder.setMovimiento(currentBoard.jugadorActual().getEquipo());
                    
                    // Update board
                    currentBoard = builder.build();
                    gameWindow.updateBoardDisplay();
                }
            }
        });
    }
    
    private Pieza createPromotedPiece(TipoDePieza tipo, Equipo equipo, int position) {
        switch (tipo) {
            case REINA: return new Reina(equipo, position);
            case TORRE: return new Torre(equipo, position);
            case ALFIL: return new Alfil(equipo, position);
            case CABALLO: return new Caballo(equipo, position);
            default: return new Reina(equipo, position); // Default to Queen
        }
    }
    
    private void checkGameEndConditions() {
        try {
            Jugador currentPlayer = currentBoard.jugadorActual();
            
            if (currentPlayer.estaEnMate()) {
                // JAQUE MATE - Juego terminado
                String loser = currentPlayer.getEquipo().esBlanca() ? "White" : "Black";
                String winner = currentPlayer.getEquipo().esBlanca() ? "Black" : "White";
                String result = currentPlayer.getEquipo().esBlanca() ? "0-1" : "1-0";
                
                currentGameData.setGameResult(result);
                currentGameData.setGameFinished(true);
                gameInProgress = false;
                gameTimer.stop();
                
                // MOSTRAR MENSAJE DE VICTORIA CLARO
                String victoryMessage = "♛ CHECKMATE! ♛\n\n" + 
                                      winner + " WINS!\n\n" +
                                      loser + " is in checkmate.";
                
                gameWindow.showGameResult(victoryMessage);
                gameWindow.updateGameStatus("GAME OVER - " + winner + " wins by checkmate!");
                
                System.out.println("🏆 GAME OVER: " + winner + " wins by checkmate!");
                
            } else if (currentPlayer.estaAhogado()) {
                // AHOGADO - Empate
                currentGameData.setGameResult("1/2-1/2");
                currentGameData.setGameFinished(true);
                gameInProgress = false;
                gameTimer.stop();
                
                String drawMessage = "♔ STALEMATE! ♔\n\n" +
                                   "Game is a DRAW!\n\n" +
                                   "No legal moves available.";
                
                gameWindow.showGameResult(drawMessage);
                gameWindow.updateGameStatus("GAME OVER - Draw by stalemate");
                
                System.out.println("🤝 GAME OVER: Draw by stalemate");
                
            } else if (currentPlayer.estaEnJaque()) {
                // JAQUE - Juego continúa
                String player = currentPlayer.getEquipo().esBlanca() ? "White" : "Black";
                gameWindow.updateGameStatus(player + " is in CHECK! ⚠️");
                System.out.println("⚠️ " + player + " is in check!");
                
                // NO reproducir sonido ya que quitamos el sistema de sonido
                
            } else {
                // JUEGO NORMAL
                String player = currentPlayer.getEquipo().esBlanca() ? "White" : "Black";
                gameWindow.updateGameStatus(player + " to move");
            }
            
            // TODO: Agregar otras condiciones de empate:
            // - Regla de 50 movimientos
            // - Repetición de posiciones 
            // - Material insuficiente
            
        } catch (Exception e) {
            System.err.println("❌ Error checking game end conditions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateGameMetadata(Movimiento movimiento) {
        currentGameData.setLastModified(LocalDateTime.now());
        currentGameData.setTotalMoves(moveCounter);
        
        // Update game duration
        long durationSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
        currentGameData.setGameDurationSeconds(durationSeconds);
    }
    
    // Public getters for GUI
    public ChessMainWindow getGameWindow() {
        return gameWindow;
    }
    
    public boolean isGameInProgress() {
        return gameInProgress;
    }
    
    public Tablero getCurrentBoard() {
        return currentBoard;
    }
    
    public GameMetadata getCurrentGameData() {
        return currentGameData;
    }
    
    public List<String> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
    
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Export current game to PGN format
     */
    public boolean exportToPGN(String filename) {
        try {
            PGNHandler pgnHandler = new PGNHandler();
            ChessGameData gameData = new ChessGameData();
            gameData.setTablero(currentBoard);
            gameData.setMetadata(currentGameData);
            gameData.setMoveHistory(moveHistory);
            
            boolean success = pgnHandler.exportGame(gameData, filename);
            
            if (success) {
                gameWindow.updateGameStatus("Game exported to PGN: " + filename);
            } else {
                gameWindow.showMessage("Failed to export game", "Export Error");
            }
            
            return success;
            
        } catch (Exception e) {
            gameWindow.showMessage("Error exporting game: " + e.getMessage(), "Export Error");
            return false;
        }
    }
    
    /**
     * Get list of available saved games
     */
    public List<String> getAvailableGames() {
        return GameDataManager.getAvailableGames();
    }
    
    /**
     * Delete a saved game
     */
    public boolean deleteGame(String gameName) {
        try {
            boolean success = GameDataManager.deleteGame(gameName);
            
            if (success) {
                gameWindow.updateGameStatus("Game deleted: " + gameName);
            } else {
                gameWindow.showMessage("Failed to delete game", "Delete Error");
            }
            
            return success;
            
        } catch (Exception e) {
            gameWindow.showMessage("Error deleting game: " + e.getMessage(), "Delete Error");
            return false;
        }
    }
    
    /**
     * Shutdown the controller cleanly
     */
    public void shutdown() {
        // Stop timers
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (autoSaveTimer != null) {
            autoSaveTimer.stop();
        }
        
        // Auto-save current game if in progress
        if (gameInProgress && moveCounter > 0) {
            String autoSaveName = "AutoSave_Exit_" + 
                java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            saveGame(autoSaveName);
        }
        
        System.out.println("Chess game controller shutdown complete");
    }
}