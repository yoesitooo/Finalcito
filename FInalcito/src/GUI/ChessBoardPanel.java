package GUI;

import kernel.piezas.*;
import java.io.File;
import kernel.jugador.*;
import kernel.tablero.*;
import javax.swing.*;

import Persistencia.UserPreferences;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;

/**
 * Chess Board Panel - Visual representation integrated with YOUR coordinate system
 */
public class ChessBoardPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private static final long serialVersionUID = 1L;
    
    // Board constants - AUMENTADO EL TAMAÑO
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 90; // Aumentado de 70 a 90
    private static final int BOARD_MARGIN = 50; // Aumentado de 40 a 50
    
    // Colors - can be customized via preferences
    private Color lightSquareColor = new Color(240, 217, 181);
    private Color darkSquareColor = new Color(181, 136, 99);
    private Color highlightColor = new Color(255, 255, 0, 120);
    private Color validMoveColor = new Color(50, 205, 50, 100);
    private Color checkColor = new Color(255, 0, 0, 150);
    private Color lastMoveColor = new Color(255, 165, 0, 80);
    
    // Controller reference
    private ChessGameController controller;
    
    // Board state
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<int[]> validMoves = new ArrayList<>();
    private boolean boardFlipped = false;
    private boolean showCoordinates = true;
    private boolean highlightMoves = true;
    
    // Animation
    private Timer animationTimer;
    private Point animationStart;
    private Point animationEnd;
    private Pieza animatingPiece;
    private float animationProgress = 0f;
    
    // Drag and drop
    private boolean isDragging = false;
    private Point dragStart;
    private Point mousePosition;
    
    // NUEVO: Campo para las imágenes de las piezas
    private Map<String, Image> pieceImages;
    
    public ChessBoardPanel(ChessGameController controller) {
        this.controller = controller;
        initializePanel();
        setupEventHandlers();
        loadPieceImages(); // NUEVO: Cargar las imágenes
    }
    
    private void initializePanel() {
        setBackground(new Color(40, 44, 52));
        setPreferredSize(new Dimension(
            BOARD_SIZE * SQUARE_SIZE + 2 * BOARD_MARGIN,
            BOARD_SIZE * SQUARE_SIZE + 2 * BOARD_MARGIN
        ));
        
        // Setup animation timer
        animationTimer = new Timer(16, e -> {
            if (animationProgress < 1.0f) {
                animationProgress += 0.08f;
                repaint();
            } else {
                animationTimer.stop();
                animationProgress = 0f;
                animatingPiece = null;
            }
        });
    }
    
    // Método auxiliar para cargar una imagen
    private Image loadImage(String filename) throws IOException {
        try {
            // OPCIÓN 1: Intentar desde resources en classpath
            String resourcePath = "/Resources/pieces/" + filename;
            InputStream is = getClass().getResourceAsStream(resourcePath);
            
            if (is != null) {
                BufferedImage originalImage = ImageIO.read(is);
                is.close();
                return originalImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            }
            
            // OPCIÓN 2: Intentar desde carpeta del proyecto
            String projectPath = "Resources/pieces/" + filename;
            File imageFile = new File(projectPath);
            
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                return originalImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            }
            
            // OPCIÓN 3: Intentar desde ruta absoluta del directorio de trabajo
            String workingDir = System.getProperty("user.dir");
            String absolutePath = workingDir + File.separator + "Resources" + File.separator + "pieces" + File.separator + filename;
            File absoluteFile = new File(absolutePath);
            
            if (absoluteFile.exists()) {
                BufferedImage originalImage = ImageIO.read(absoluteFile);
                return originalImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            }
            
            // Si nada funciona, lanzar excepción
            throw new IOException("No se encontró la imagen: " + filename + 
                                "\nIntentó buscar en:\n" +
                                "1. " + resourcePath + "\n" +
                                "2. " + projectPath + "\n" +
                                "3. " + absolutePath);
            
        } catch (Exception e) {
            throw new IOException("Error cargando imagen " + filename + ": " + e.getMessage());
        }
    }
    
    // NUEVO: Método para cargar las imágenes
    private void loadPieceImages() {
    	pieceImages = new HashMap<>();
        
        try {
            // Cargar piezas blancas
            pieceImages.put("K_WHITE", loadImage("Rey-Blanco.png"));
            pieceImages.put("Q_WHITE", loadImage("Reina-Blanca.png"));
            pieceImages.put("R_WHITE", loadImage("Torre-Blanca.png"));
            pieceImages.put("B_WHITE", loadImage("Alfil-Blanco.png"));
            pieceImages.put("N_WHITE", loadImage("Caballo-Blanco.png"));
            pieceImages.put("P_WHITE", loadImage("Peon-Blanco.png"));
            
            // Cargar piezas negras
            pieceImages.put("K_BLACK", loadImage("Rey-Negro.png"));
            pieceImages.put("Q_BLACK", loadImage("Reina-Negra.png"));
            pieceImages.put("R_BLACK", loadImage("Torre-Negra.png"));
            pieceImages.put("B_BLACK", loadImage("Alfil-Negro.png"));
            pieceImages.put("N_BLACK", loadImage("Caballo-Negro.png"));
            pieceImages.put("P_BLACK", loadImage("Peon-Negro.png"));
            
            System.out.println("¡Imágenes cargadas correctamente!");
            
        } catch (Exception e) {
            System.err.println("Error al cargar imágenes: " + e.getMessage());
            e.printStackTrace();
            pieceImages = null;
        }
    }
    
    // NUEVO: Método para obtener la clave de la imagen
    private String getImageKey(TipoDePieza tipo, Equipo equipo) {
        String key = "";
        switch (tipo) {
            case REY: key = "K"; break;
            case REINA: key = "Q"; break;
            case TORRE: key = "R"; break;
            case ALFIL: key = "B"; break;
            case CABALLO: key = "N"; break;
            case PEON: key = "P"; break;
        }
        return key + "_" + (equipo.esBlanca() ? "WHITE" : "BLACK");
    }
    
    private void setupEventHandlers() {
        addMouseListener(this);
        addMouseMotionListener(this);
        
        // Keyboard shortcuts for board control
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    flipBoard();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    resetView();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw board background
        drawBoardBackground(g2d);
        
        // Draw coordinates
        if (showCoordinates) {
            drawCoordinates(g2d);
        }
        
        // Draw board squares
        drawBoard(g2d);
        
        // Draw highlights
        drawHighlights(g2d);
        
        // Draw pieces
        drawPieces(g2d);
        
        // Draw animation
        if (animatingPiece != null) {
            drawAnimation(g2d);
        }
        
        // Draw drag piece
        if (isDragging && selectedRow != -1 && selectedCol != -1) {
            drawDragPiece(g2d);
        }
    }
    
    private void drawBoardBackground(Graphics2D g2d) {
        // Draw border
        g2d.setColor(new Color(60, 64, 72));
        g2d.fillRoundRect(10, 10, 
            BOARD_SIZE * SQUARE_SIZE + 2 * (BOARD_MARGIN - 10),
            BOARD_SIZE * SQUARE_SIZE + 2 * (BOARD_MARGIN - 10), 20, 20);
        
        // Draw border highlight
        g2d.setColor(new Color(97, 175, 239));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(10, 10,
            BOARD_SIZE * SQUARE_SIZE + 2 * (BOARD_MARGIN - 10),
            BOARD_SIZE * SQUARE_SIZE + 2 * (BOARD_MARGIN - 10), 20, 20);
    }
    
    private void drawCoordinates(Graphics2D g2d) {
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            // Files (a-h)
            char file = boardFlipped ? (char)('h' - i) : (char)('a' + i);
            int x = BOARD_MARGIN + i * SQUARE_SIZE + SQUARE_SIZE / 2 - 5;
            g2d.drawString(String.valueOf(file), x, BOARD_MARGIN - 10);
            g2d.drawString(String.valueOf(file), x, BOARD_MARGIN + BOARD_SIZE * SQUARE_SIZE + 20);
            
            // Ranks (1-8)
            int rank = boardFlipped ? i + 1 : 8 - i;
            int y = BOARD_MARGIN + i * SQUARE_SIZE + SQUARE_SIZE / 2 + 5;
            g2d.drawString(String.valueOf(rank), BOARD_MARGIN - 20, y);
            g2d.drawString(String.valueOf(rank), BOARD_MARGIN + BOARD_SIZE * SQUARE_SIZE + 10, y);
        }
    }
    
    private void drawBoard(Graphics2D g2d) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawSquare(g2d, row, col);
            }
        }
    }
    
    private void drawSquare(Graphics2D g2d, int row, int col) {
        int x = BOARD_MARGIN + col * SQUARE_SIZE;
        int y = BOARD_MARGIN + row * SQUARE_SIZE;
        
        // Determine square color
        boolean isLight = (row + col) % 2 == 0;
        Color squareColor = isLight ? lightSquareColor : darkSquareColor;
        
        // Apply hover effect
        if (isMouseOverSquare(row, col)) {
            squareColor = brightenColor(squareColor, 0.2f);
        }
        
        g2d.setColor(squareColor);
        g2d.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
        
        // Draw square border
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
    }
    
    private void drawHighlights(Graphics2D g2d) {
        // Highlight selected square
        if (selectedRow != -1 && selectedCol != -1) {
            drawSquareHighlight(g2d, selectedRow, selectedCol, highlightColor);
        }
        
        // Highlight valid moves
        if (highlightMoves && !validMoves.isEmpty()) {
            for (int[] move : validMoves) {
                drawMoveIndicator(g2d, move[0], move[1]);
            }
        }
        
        // Highlight check
        if (controller != null && controller.getCurrentBoard() != null) {
            highlightCheck(g2d);
        }
        
        // TODO: Highlight last move
    }
    
    private void drawSquareHighlight(Graphics2D g2d, int row, int col, Color color) {
        int x = BOARD_MARGIN + col * SQUARE_SIZE;
        int y = BOARD_MARGIN + row * SQUARE_SIZE;
        
        g2d.setColor(color);
        g2d.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
        
        // Draw border
        g2d.setColor(brightenColor(color, 0.3f));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x + 2, y + 2, SQUARE_SIZE - 4, SQUARE_SIZE - 4);
    }
    
    private void drawMoveIndicator(Graphics2D g2d, int row, int col) {
        int x = BOARD_MARGIN + col * SQUARE_SIZE;
        int y = BOARD_MARGIN + row * SQUARE_SIZE;
        
        // Check if target square has a piece (capture)
        Pieza targetPiece = controller.getPieceAt(row, col);
        
        if (targetPiece != null) {
            // Capture indicator - ring around square
            g2d.setColor(new Color(255, 100, 100, 150));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRoundRect(x + 3, y + 3, SQUARE_SIZE - 6, SQUARE_SIZE - 6, 8, 8);
        } else {
            // Normal move indicator - circle in center
            g2d.setColor(validMoveColor);
            int size = SQUARE_SIZE / 3;
            int offset = (SQUARE_SIZE - size) / 2;
            g2d.fillOval(x + offset, y + offset, size, size);
        }
    }
    
    private void highlightCheck(Graphics2D g2d) {
        try {
            Tablero board = controller.getCurrentBoard();
            if (board != null) {
                Jugador currentPlayer = board.jugadorActual();
                if (currentPlayer.estaEnJaque()) {
                    Rey king = currentPlayer.getReyDelJugador();
                    int kingPos = king.getPosicion();
                    int row = kingPos / 8;
                    int col = kingPos % 8;
                    
                    drawSquareHighlight(g2d, row, col, checkColor);
                }
            }
        } catch (Exception e) {
            System.err.println("Error highlighting check: " + e.getMessage());
        }
    }
    
    private void drawPieces(Graphics2D g2d) {
        if (controller == null || controller.getCurrentBoard() == null) {
            return;
        }
        
        try {
            Tablero board = controller.getCurrentBoard();
            
            for (int i = 0; i < 64; i++) {
                Casilla casilla = board.getCasilla(i);
                if (casilla.estaCasillaOcupada()) {
                    Pieza pieza = casilla.getPieza();
                    
                    // Convert YOUR coordinate system (0-63) to GUI coordinates
                    int row = i / 8;
                    int col = i % 8;
                    
                    // Skip piece being dragged
                    if (isDragging && row == selectedRow && col == selectedCol) {
                        continue;
                    }
                    
                    // Skip piece being animated
                    if (animatingPiece == pieza) {
                        continue;
                    }
                    
                    drawPiece(g2d, pieza, row, col, 1.0f);
                }
            }
        } catch (Exception e) {
            System.err.println("Error drawing pieces: " + e.getMessage());
        }
    }
    
    // MODIFICADO: Método drawPiece actualizado para usar imágenes
    private void drawPiece(Graphics2D g2d, Pieza pieza, int row, int col, float alpha) {
        if (pieza == null) return;
        
        int x = BOARD_MARGIN + col * SQUARE_SIZE;
        int y = BOARD_MARGIN + row * SQUARE_SIZE;
        
        // Set alpha for animations
        if (alpha < 1.0f) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        
        // Intentar usar imagen primero
        if (pieceImages != null) {
            String key = getImageKey(pieza.getTipoDePieza(), pieza.getEquipo());
            Image img = pieceImages.get(key);
            
            if (img != null) {
                // Dibujar la imagen centrada
                int imgX = x + (SQUARE_SIZE - 75) / 2; // Ajustado para el nuevo tamaño
                int imgY = y + (SQUARE_SIZE - 75) / 2;
                g2d.drawImage(img, imgX, imgY, null);
                
                // Reset alpha
                if (alpha < 1.0f) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
                return;
            }
        }
        
        // Si no hay imagen, usar el código de letras existente
        String symbol = getPieceSymbol(pieza);
        
        // Dibujar círculo de fondo
        int circleSize = SQUARE_SIZE - 10;
        int circleX = x + (SQUARE_SIZE - circleSize) / 2;
        int circleY = y + (SQUARE_SIZE - circleSize) / 2;
        
        if (pieza.getEquipo().esBlanca()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        } else {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        }
        
        // Dibujar letra - AUMENTAMOS EL TAMAÑO
        g2d.setFont(new Font("Arial", Font.BOLD, 46)); // Aumentado de 36 a 46
        g2d.setColor(pieza.getEquipo().esBlanca() ? Color.BLACK : Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (SQUARE_SIZE - fm.stringWidth(symbol)) / 2;
        int textY = y + (SQUARE_SIZE + fm.getAscent()) / 2 - 4;
        g2d.drawString(symbol, textX, textY);
        
        // Reset alpha
        if (alpha < 1.0f) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    // MODIFICADO: drawAnimation actualizado para usar imágenes
    private void drawAnimation(Graphics2D g2d) {
        if (animatingPiece == null || animationStart == null || animationEnd == null) {
            return;
        }
        
        // Calculate current position
        float t = easeInOutCubic(animationProgress);
        int currentX = (int)(animationStart.x + (animationEnd.x - animationStart.x) * t);
        int currentY = (int)(animationStart.y + (animationEnd.y - animationStart.y) * t);
        
        // Intentar usar imagen
        if (pieceImages != null) {
            String key = getImageKey(animatingPiece.getTipoDePieza(), animatingPiece.getEquipo());
            Image img = pieceImages.get(key);
            
            if (img != null) {
                int imgX = currentX + (SQUARE_SIZE - 75) / 2;
                int imgY = currentY + (SQUARE_SIZE - 75) / 2;
                g2d.drawImage(img, imgX, imgY, null);
                return;
            }
        }
        
        // Si no hay imagen, usar letras...
        String symbol = getPieceSymbol(animatingPiece);
        
        int circleSize = SQUARE_SIZE - 10;
        int circleX = currentX + (SQUARE_SIZE - circleSize) / 2;
        int circleY = currentY + (SQUARE_SIZE - circleSize) / 2;
        
        if (animatingPiece.getEquipo().esBlanca()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        } else {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        }
        
        g2d.setFont(new Font("Arial", Font.BOLD, 46));
        g2d.setColor(animatingPiece.getEquipo().esBlanca() ? Color.BLACK : Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = currentX + (SQUARE_SIZE - fm.stringWidth(symbol)) / 2;
        int textY = currentY + (SQUARE_SIZE + fm.getAscent()) / 2 - 4;
        g2d.drawString(symbol, textX, textY);
    }
    
    // MODIFICADO: drawDragPiece actualizado para usar imágenes
    private void drawDragPiece(Graphics2D g2d) {
        if (mousePosition == null) return;
        
        Pieza piece = controller.getPieceAt(selectedRow, selectedCol);
        if (piece == null) return;
        
        // Add transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        
        // Intentar usar imagen
        if (pieceImages != null) {
            String key = getImageKey(piece.getTipoDePieza(), piece.getEquipo());
            Image img = pieceImages.get(key);
            
            if (img != null) {
                int imgX = mousePosition.x - 37; // Centrar en el mouse (75/2)
                int imgY = mousePosition.y - 37;
                g2d.drawImage(img, imgX, imgY, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                return;
            }
        }
        
        // Si no hay imagen, usar el código de letras existente
        String symbol = getPieceSymbol(piece);
        
        int circleSize = SQUARE_SIZE - 10;
        int circleX = mousePosition.x - circleSize / 2;
        int circleY = mousePosition.y - circleSize / 2;
        
        if (piece.getEquipo().esBlanca()) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        } else {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(circleX, circleY, circleSize, circleSize);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(circleX, circleY, circleSize, circleSize);
        }
        
        g2d.setFont(new Font("Arial", Font.BOLD, 46));
        g2d.setColor(piece.getEquipo().esBlanca() ? Color.BLACK : Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = mousePosition.x - fm.stringWidth(symbol) / 2;
        int textY = mousePosition.y + fm.getAscent() / 2 - 4;
        g2d.drawString(symbol, textX, textY);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    private String getPieceSymbol(Pieza pieza) {
        boolean isWhite = pieza.getEquipo().esBlanca();
        
        switch (pieza.getTipoDePieza()) {
            case REY:     return "K";
            case REINA:   return "Q";
            case TORRE:   return "R";
            case ALFIL:   return "B";
            case CABALLO: return "N";
            case PEON:    return "P";
            default:      return "";
        }
    }
    
    // Mouse event handlers
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow(); // For keyboard shortcuts
        
        Point boardCoord = screenToBoardCoordinates(e.getPoint());
        if (boardCoord == null) return;
        
        int row = boardCoord.y;
        int col = boardCoord.x;
        
        handleSquareClick(row, col);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        Point boardCoord = screenToBoardCoordinates(e.getPoint());
        if (boardCoord == null) return;
        
        int row = boardCoord.y;
        int col = boardCoord.x;
        
        Pieza piece = controller.getPieceAt(row, col);
        if (piece != null) {
            // Start potential drag
            dragStart = e.getPoint();
            
            // Select piece and show valid moves
            selectSquare(row, col);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            // Handle drag and drop
            Point boardCoord = screenToBoardCoordinates(e.getPoint());
            if (boardCoord != null && selectedRow != -1 && selectedCol != -1) {
                int targetRow = boardCoord.y;
                int targetCol = boardCoord.x;
                
                // Attempt move
                if (targetRow != selectedRow || targetCol != selectedCol) {
                    boolean moveSuccessful = controller.processMove(selectedRow, selectedCol, targetRow, targetCol);
                    if (moveSuccessful) {
                        // Start animation
                        startMoveAnimation(selectedRow, selectedCol, targetRow, targetCol);
                    }
                }
            }
            
            isDragging = false;
            clearSelection();
        }
        
        repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart != null && selectedRow != -1 && selectedCol != -1) {
            // Calculate drag distance
            double distance = dragStart.distance(e.getPoint());
            
            if (distance > 10 && !isDragging) {
                // Start dragging
                isDragging = true;
            }
            
            if (isDragging) {
                mousePosition = e.getPoint();
                repaint();
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        // Update hover state
        repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    // Helper methods
    private void handleSquareClick(int row, int col) {
        if (selectedRow == -1 && selectedCol == -1) {
            // No piece selected - try to select
            selectSquare(row, col);
        } else if (selectedRow == row && selectedCol == col) {
            // Same square clicked - deselect
            clearSelection();
        } else {
            // Different square - try to move
            boolean moveSuccessful = controller.processMove(selectedRow, selectedCol, row, col);
            if (moveSuccessful) {
                // Start animation
                startMoveAnimation(selectedRow, selectedCol, row, col);
                clearSelection();
            } else {
                // Try to select new piece
                selectSquare(row, col);
            }
        }
    }
    
    private void selectSquare(int row, int col) {
        Pieza piece = controller.getPieceAt(row, col);
        
        if (piece != null) {
            // Check if it's the current player's piece
            if (controller.getCurrentBoard() != null) {
                boolean isCurrentPlayerPiece = piece.getEquipo() == controller.getCurrentBoard().jugadorActual().getEquipo();
                
                if (isCurrentPlayerPiece) {
                    selectedRow = row;
                    selectedCol = col;
                    validMoves = controller.getValidMoves(row, col);
                    repaint();
                    return;
                }
            }
        }
        
        // Could not select piece
        clearSelection();
    }
    
    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        validMoves.clear();
        repaint();
    }
    
    private Point screenToBoardCoordinates(Point screenPoint) {
        int x = screenPoint.x - BOARD_MARGIN;
        int y = screenPoint.y - BOARD_MARGIN;
        
        if (x < 0 || y < 0 || x >= BOARD_SIZE * SQUARE_SIZE || y >= BOARD_SIZE * SQUARE_SIZE) {
            return null;
        }
        
        int col = x / SQUARE_SIZE;
        int row = y / SQUARE_SIZE;
        
        // Apply board flip
        if (boardFlipped) {
            col = BOARD_SIZE - 1 - col;
            row = BOARD_SIZE - 1 - row;
        }
        
        return new Point(col, row);
    }
    
    private boolean isMouseOverSquare(int row, int col) {
        Point mousePos = getMousePosition();
        if (mousePos == null) return false;
        
        Point boardCoord = screenToBoardCoordinates(mousePos);
        return boardCoord != null && boardCoord.y == row && boardCoord.x == col;
    }
    
    private void startMoveAnimation(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isAnimationEnabled()) return;
        
        Pieza piece = controller.getPieceAt(toRow, toCol); // Piece is now at destination
        if (piece == null) return;
        
        // Calculate animation positions
        int fromX = BOARD_MARGIN + fromCol * SQUARE_SIZE;
        int fromY = BOARD_MARGIN + fromRow * SQUARE_SIZE;
        int toX = BOARD_MARGIN + toCol * SQUARE_SIZE;
        int toY = BOARD_MARGIN + toRow * SQUARE_SIZE;
        
        animationStart = new Point(fromX, fromY);
        animationEnd = new Point(toX, toY);
        animatingPiece = piece;
        animationProgress = 0f;
        
        animationTimer.start();
    }
    
    private boolean isAnimationEnabled() {
        // TODO: Get from preferences
        return true;
    }
    
    private Color brightenColor(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
        return new Color(r, g, b, color.getAlpha());
    }
    
    private float easeInOutCubic(float t) {
        return t < 0.5 ? 4 * t * t * t : 1 - (float)Math.pow(-2 * t + 2, 3) / 2;
    }
    
    // Public methods for external control
    public void flipBoard() {
        boardFlipped = !boardFlipped;
        clearSelection();
        repaint();
    }
    
    public void resetView() {
        boardFlipped = false;
        clearSelection();
        repaint();
    }
    
    public void setShowCoordinates(boolean show) {
        this.showCoordinates = show;
        repaint();
    }
    
    public void setHighlightMoves(boolean highlight) {
        this.highlightMoves = highlight;
        repaint();
    }
    
    public void applyPreferences(UserPreferences preferences) {
        // Apply color theme
        String theme = preferences.getTheme();
        Color[] themeColors = getThemeColors(theme);
        lightSquareColor = themeColors[0];
        darkSquareColor = themeColors[1];
        
        // Apply other visual preferences
        showCoordinates = preferences.isShowCoordinates();
        highlightMoves = preferences.isHighlightMoves();
        
        repaint();
    }
    
    private Color[] getThemeColors(String theme) {
        switch (theme) {
            case "Modern":
                return new Color[]{
                    new Color(255, 248, 220),
                    new Color(139, 69, 19)
                };
            case "Wood":
                return new Color[]{
                    new Color(222, 184, 135),
                    new Color(160, 82, 45)
                };
            case "Marble":
                return new Color[]{
                    new Color(248, 248, 255),
                    new Color(128, 128, 128)
                };
            case "Blue":
                return new Color[]{
                    new Color(240, 248, 255),
                    new Color(70, 130, 180)
                };
            case "Green":
                return new Color[]{
                    new Color(245, 255, 250),
                    new Color(34, 139, 34)
                };
            default: // Classic
                return new Color[]{
                    new Color(240, 217, 181),
                    new Color(181, 136, 99)
                };
        }
    }
    
    // Getters for current state
    public boolean isBoardFlipped() {
        return boardFlipped;
    }
    
    public boolean isShowCoordinates() {
        return showCoordinates;
    }
    
    public boolean isHighlightMoves() {
        return highlightMoves;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public int getSelectedCol() {
        return selectedCol;
    }
    
    public List<int[]> getValidMoves() {
        return new ArrayList<>(validMoves);
    }

	public Color getLastMoveColor() {
		return lastMoveColor;
	}

	public void setLastMoveColor(Color lastMoveColor) {
		this.lastMoveColor = lastMoveColor;
	}
}