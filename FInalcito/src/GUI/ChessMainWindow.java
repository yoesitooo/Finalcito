package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Persistencia.UserPreferences;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main Chess Game Window - Professional Interface
 */
public class ChessMainWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static final Color DARK_THEME = new Color(40, 44, 52);
    private static final Color PANEL_COLOR = new Color(50, 54, 62);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    
    // Controller reference
    private ChessGameController controller;
    
    // Main components
    private ChessBoardPanel boardPanel;
    private GameHistoryPanel historyPanel;
    private JPanel statusPanel;
    private JPanel controlPanel;
    
    // Status components
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JLabel gameTimeLabel;
    private JLabel moveCountLabel;
    
    // Control buttons
    private JButton newGameButton;
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JButton undoButton;
    private JButton redoButton;
    
    // Menu components
    private JMenuBar menuBar;
    
    public ChessMainWindow(ChessGameController controller) {
        this.controller = controller;
        initializeWindow();
        createMenuBar();
        createComponents();
        setupLayout();
        setupEventHandlers();
        applyDarkTheme();
    }
    
    private void initializeWindow() {
        setTitle("Chess Master - Professional Edition");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1400, 900);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(PANEL_COLOR);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ACCENT_COLOR));
        setJMenuBar(menuBar);
        
        // File Menu
        JMenu fileMenu = createStyledMenu("File");
        addMenuItem(fileMenu, "New Game", "Ctrl+N", e -> controller.startNewGame());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Save Game", "Ctrl+S", e -> saveGame());
        addMenuItem(fileMenu, "Save As...", "Ctrl+Shift+S", e -> saveGameAs());
        addMenuItem(fileMenu, "Load Game", "Ctrl+O", e -> loadGame());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Export PGN", "Ctrl+E", e -> exportToPGN());
        addMenuItem(fileMenu, "Import PGN", "Ctrl+I", e -> importFromPGN());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", "Ctrl+Q", e -> handleWindowClosing());
        menuBar.add(fileMenu);
        
        // Game Menu
        JMenu gameMenu = createStyledMenu("Game");
        addMenuItem(gameMenu, "Undo Move", "Ctrl+Z", e -> controller.undoMove());
        addMenuItem(gameMenu, "Redo Move", "Ctrl+Y", e -> controller.redoMove());
        gameMenu.addSeparator();
        addMenuItem(gameMenu, "Flip Board", "Ctrl+F", e -> boardPanel.flipBoard());
        addMenuItem(gameMenu, "Reset View", "Ctrl+R", e -> boardPanel.resetView());
        gameMenu.addSeparator();
        addMenuItem(gameMenu, "Game Browser", "Ctrl+B", e -> showGameBrowser());
        menuBar.add(gameMenu);
        
        // View Menu
        JMenu viewMenu = createStyledMenu("View");
        JCheckBoxMenuItem showCoordinatesItem = new JCheckBoxMenuItem("Show Coordinates");
        showCoordinatesItem.setSelected(true);
        showCoordinatesItem.addActionListener(e -> 
            boardPanel.setShowCoordinates(showCoordinatesItem.isSelected()));
        viewMenu.add(showCoordinatesItem);
        
        JCheckBoxMenuItem highlightMovesItem = new JCheckBoxMenuItem("Highlight Valid Moves");
        highlightMovesItem.setSelected(true);
        highlightMovesItem.addActionListener(e -> 
            boardPanel.setHighlightMoves(highlightMovesItem.isSelected()));
        viewMenu.add(highlightMovesItem);
        menuBar.add(viewMenu);
        
        // Help Menu
        JMenu helpMenu = createStyledMenu("Help");
        addMenuItem(helpMenu, "How to Play", "F1", e -> showHelp());
        addMenuItem(helpMenu, "Keyboard Shortcuts", "Ctrl+H", e -> showShortcuts());
        helpMenu.addSeparator();
        addMenuItem(helpMenu, "About Chess Master", null, e -> showAbout());
        menuBar.add(helpMenu);
    }
    
    private void createComponents() {
        // Main board panel
        boardPanel = new ChessBoardPanel(controller);
        
        // Game history panel
        historyPanel = new GameHistoryPanel();
        
        // Status panel
        createStatusPanel();
        
        // Control panel
        createControlPanel();
    }
    
    private void createStatusPanel() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, ACCENT_COLOR),
            new EmptyBorder(8, 15, 8, 15)
        ));
        
        // Left side - game status
        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftStatus.setOpaque(false);
        
        statusLabel = createStyledLabel("Ready to play", 12, Color.WHITE);
        leftStatus.add(statusLabel);
        
        statusPanel.add(leftStatus, BorderLayout.WEST);
        
        // Center - current player and move count
        JPanel centerStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerStatus.setOpaque(false);
        
        currentPlayerLabel = createStyledLabel("White to move", 14, ACCENT_COLOR);
        currentPlayerLabel.setFont(currentPlayerLabel.getFont().deriveFont(Font.BOLD));
        centerStatus.add(currentPlayerLabel);
        
        moveCountLabel = createStyledLabel("Move: 1", 12, Color.LIGHT_GRAY);
        centerStatus.add(moveCountLabel);
        
        statusPanel.add(centerStatus, BorderLayout.CENTER);
        
        // Right side - game time
        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightStatus.setOpaque(false);
        
        gameTimeLabel = createStyledLabel("Time: 00:00", 12, Color.LIGHT_GRAY);
        rightStatus.add(gameTimeLabel);
        
        statusPanel.add(rightStatus, BorderLayout.EAST);
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBackground(PANEL_COLOR);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, ACCENT_COLOR),
            new EmptyBorder(15, 15, 15, 15)
        ));
        controlPanel.setPreferredSize(new Dimension(250, 0));
        
        // Use BoxLayout for vertical arrangement
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        // Game control section
        JPanel gameControlSection = createControlSection("Game Control");
        
        newGameButton = createControlButton("New Game", "🆕");
        newGameButton.addActionListener(e -> confirmNewGame());
        gameControlSection.add(newGameButton);
        
        saveGameButton = createControlButton("Save Game", "💾");
        saveGameButton.addActionListener(e -> saveGame());
        gameControlSection.add(saveGameButton);
        
        loadGameButton = createControlButton("Load Game", "📂");
        loadGameButton.addActionListener(e -> loadGame());
        gameControlSection.add(loadGameButton);
        
        controlPanel.add(gameControlSection);
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Move control section
        JPanel moveControlSection = createControlSection("Move Control");
        
        undoButton = createControlButton("Undo", "↶");
        undoButton.addActionListener(e -> controller.undoMove());
        moveControlSection.add(undoButton);
        
        redoButton = createControlButton("Redo", "↷");
        redoButton.addActionListener(e -> controller.redoMove());
        moveControlSection.add(redoButton);
        
        controlPanel.add(moveControlSection);
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Game browser section
        JPanel browserSection = createControlSection("Games");
        
        JButton gameBrowserButton = createControlButton("Game Browser", "📋");
        gameBrowserButton.addActionListener(e -> showGameBrowser());
        browserSection.add(gameBrowserButton);
        
        controlPanel.add(browserSection);
        
        // Add flexible space
        controlPanel.add(Box.createVerticalGlue());
        
        // Add history panel
        JPanel historySection = createControlSection("Move History");
        historySection.add(historyPanel);
        controlPanel.add(historySection);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Center - chess board
        add(boardPanel, BorderLayout.CENTER);
        
        // East - control panel
        add(controlPanel, BorderLayout.EAST);
        
        // South - status panel
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Keyboard shortcuts
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        
        // Game shortcuts
        addKeyboardShortcut(inputMap, actionMap, "ctrl N", "newGame", e -> controller.startNewGame());
        addKeyboardShortcut(inputMap, actionMap, "ctrl S", "saveGame", e -> saveGame());
        addKeyboardShortcut(inputMap, actionMap, "ctrl O", "loadGame", e -> loadGame());
        addKeyboardShortcut(inputMap, actionMap, "ctrl Z", "undo", e -> controller.undoMove());
        addKeyboardShortcut(inputMap, actionMap, "ctrl Y", "redo", e -> controller.redoMove());
        addKeyboardShortcut(inputMap, actionMap, "F11", "fullscreen", e -> toggleFullscreen());
        
        // Update button states periodically
        Timer buttonUpdateTimer = new Timer(500, e -> updateButtonStates());
        buttonUpdateTimer.start();
    }
    
    private void applyDarkTheme() {
        // Apply dark theme to all components
        getContentPane().setBackground(DARK_THEME);
        
        // Set menu colors
        UIManager.put("Menu.background", PANEL_COLOR);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("MenuItem.background", PANEL_COLOR);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", ACCENT_COLOR);
        UIManager.put("MenuBar.background", PANEL_COLOR);
        
        SwingUtilities.updateComponentTreeUI(menuBar);
    }
    
    // Helper methods for creating styled components
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return menu;
    }
    
    private void addMenuItem(JMenu menu, String text, String accelerator, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(Color.WHITE);
        item.setBackground(PANEL_COLOR);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        if (accelerator != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        
        item.addActionListener(action);
        menu.add(item);
    }
    
    private JLabel createStyledLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        return label;
    }
    
    private JPanel createControlSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            title,
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12),
            ACCENT_COLOR
        ));
        
        return section;
    }
    
    private JButton createControlButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        
        // COLORES CON MUCHO MEJOR CONTRASTE
        button.setBackground(new Color(70, 80, 95)); // Fondo más oscuro
        button.setForeground(Color.BLACK); // *** TEXTO NEGRO ***
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Texto en negrita
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), // Borde azul más grueso
            new EmptyBorder(10, 15, 10, 15) // Más padding
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40)); // Botones más grandes
        
        // Hover effect mejorado
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 110, 125)); // Hover más claro
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO EN HOVER ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR.brighter(), 3),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 80, 95)); // Volver al color original
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO SIEMPRE ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return button;
    }
    
    private void addKeyboardShortcut(InputMap inputMap, ActionMap actionMap, 
                                   String key, String actionName, ActionListener action) {
        inputMap.put(KeyStroke.getKeyStroke(key), actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }
    
    // Public methods for controller to update GUI
    public void updateBoardDisplay() {
        if (boardPanel != null) {
            boardPanel.repaint();
        }
    }
    
    public void updateGameStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    
    public void updatePlayerTurn(boolean whiteToMove) {
        if (currentPlayerLabel != null) {
            String player = whiteToMove ? "White" : "Black";
            currentPlayerLabel.setText(player + " to move");
            currentPlayerLabel.setForeground(whiteToMove ? Color.WHITE : Color.LIGHT_GRAY);
        }
    }
    
    public void updateGameTime(long seconds) {
        if (gameTimeLabel != null) {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            
            String timeStr = hours > 0 ? 
                String.format("Time: %02d:%02d:%02d", hours, minutes, secs) :
                String.format("Time: %02d:%02d", minutes, secs);
                
            gameTimeLabel.setText(timeStr);
        }
    }
    
    public void addMoveToHistory(String move, int moveNumber) {
        if (historyPanel != null) {
            historyPanel.addMove(move, moveNumber);
            
            // Update move count
            if (moveCountLabel != null) {
                moveCountLabel.setText("Move: " + ((moveNumber + 1) / 2 + 1));
            }
        }
    }
    
    public void removeLastMoveFromHistory() {
        if (historyPanel != null) {
            historyPanel.removeLastMove();
        }
    }
    
    public void loadMoveHistory(List<String> moves) {
        if (historyPanel != null) {
            historyPanel.loadMoveHistory(moves);
        }
    }
    
    public void clearMoveHistory() {
        if (historyPanel != null) {
            historyPanel.clearHistory();
        }
    }
    
    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JButton createGameResultButton(String text) {
        JButton button = new JButton(text);
        
        // COLORES ARREGLADOS - TEXTO NEGRO SIEMPRE VISIBLE
        button.setBackground(new Color(220, 220, 220)); // Fondo gris claro
        button.setForeground(Color.BLACK); // *** TEXTO NEGRO ***
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 3), // Borde más grueso
            new EmptyBorder(12, 20, 12, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 45));
        
        // Hover effect ARREGLADO
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240)); // Hover más claro
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO EN HOVER ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR.brighter(), 4),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220)); // Volver al color original
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO SIEMPRE ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 3),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
        });
        
        return button;
    }
    
    public void showGameResult(String result) {
        // Create custom dialog for game result
        JDialog resultDialog = new JDialog(this, "Game Over", true);
        resultDialog.setSize(500, 300); // MÁS GRANDE
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setLayout(new BorderLayout());
        resultDialog.setResizable(false);
        
        // Main panel with dark theme
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_THEME);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30)); // MÁS ESPACIO
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(DARK_THEME);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Game Over title
        JLabel gameOverLabel = new JLabel("🏁 GAME OVER 🏁", SwingConstants.CENTER);
        gameOverLabel.setForeground(ACCENT_COLOR);
        gameOverLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(gameOverLabel, BorderLayout.CENTER);
        
        // Result content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DARK_THEME);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Parse result to get winner and type
        String winner = "";
        String resultType = "";
        String emoji = " ";
        Color resultColor = ACCENT_COLOR;
        
        if (result.toLowerCase().contains("checkmate")) {
            if (result.toLowerCase().contains("white")) {
                winner = "WHITE WINS!";
                emoji = " ";
                resultColor = Color.WHITE;
            } else {
                winner = "BLACK WINS!";
                emoji = " ";
                resultColor = Color.LIGHT_GRAY;
            }
            resultType = "by Checkmate";
        } else if (result.toLowerCase().contains("stalemate")) {
            winner = "DRAW";
            emoji = " ";
            resultType = "by Stalemate";
            resultColor = Color.YELLOW;
        } else if (result.toLowerCase().contains("draw")) {
            winner = "DRAW";
            emoji = " ";
            resultType = "Game Drawn";
            resultColor = Color.YELLOW;
        } else {
            winner = "GAME OVER";
            resultType = " ";
        }
        
        // Winner label - MÁS GRANDE Y CENTRADO
        JLabel winnerLabel = new JLabel(emoji + " " + winner + " " + emoji, SwingConstants.CENTER);
        winnerLabel.setForeground(resultColor);
        winnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32)); // MÁS GRANDE
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winnerLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Result type label
        JLabel typeLabel = new JLabel(resultType, SwingConstants.CENTER);
        typeLabel.setForeground(Color.LIGHT_GRAY);
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // MÁS GRANDE
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(winnerLabel);
        contentPanel.add(typeLabel);
        
        // Button panel - BOTONES MÁS GRANDES
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(DARK_THEME);
        buttonPanel.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        // Create larger, better-styled buttons
        JButton newGameBtn = createGameResultButton("🆕 New Game");
        newGameBtn.addActionListener(e -> {
            resultDialog.dispose();
            confirmNewGame(); // Usar el método existente
        });
        
        JButton saveBtn = createGameResultButton("💾 Save Game");
        saveBtn.addActionListener(e -> {
            resultDialog.dispose();
            saveGame();
        });
        
        JButton closeBtn = createGameResultButton("✖ Close");
        closeBtn.addActionListener(e -> resultDialog.dispose());
        
        buttonPanel.add(newGameBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);
        
        // Assemble dialog
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        resultDialog.add(mainPanel);
        resultDialog.setVisible(true);
        
        // Also update status
        updateGameStatus(winner + " " + resultType);
    }

    
    public void applyUserPreferences(UserPreferences preferences) {
        if (boardPanel != null) {
            boardPanel.applyPreferences(preferences);
        }
        
        // Apply sound preferences
        SoundManager.setSoundEnabled(preferences.isSoundEnabled());
    }
    
    // Private action methods
    private void confirmNewGame() {
        if (controller.isGameInProgress()) {
            int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to start a new game?\nThe current game will be lost.",
                "New Game Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (option == JOptionPane.YES_OPTION) {
                controller.startNewGame();
            }
        } else {
            controller.startNewGame();
        }
    }
    
    private void saveGame() {
        if (!controller.isGameInProgress()) {
            showMessage("No game in progress to save.", "Save Game");
            return;
        }
        
        String currentName = controller.getCurrentGameData().getGameName();
        String gameName = JOptionPane.showInputDialog(this,
            "Enter name for the game:",
            "Save Game",
            JOptionPane.QUESTION_MESSAGE);
            
        if (gameName != null && !gameName.trim().isEmpty()) {
            controller.saveGame(gameName.trim());
        }
    }
    
    private void saveGameAs() {
        if (!controller.isGameInProgress()) {
            showMessage("No game in progress to save.", "Save Game As");
            return;
        }
        
        String gameName = JOptionPane.showInputDialog(this,
            "Enter new name for the game:",
            "Save Game As",
            JOptionPane.QUESTION_MESSAGE);
            
        if (gameName != null && !gameName.trim().isEmpty()) {
            controller.saveGame(gameName.trim());
        }
    }
    
    private void loadGame() {
        GameBrowserDialog browser = new GameBrowserDialog(this, controller);
        browser.setVisible(true);
        
        String selectedGame = browser.getSelectedGame();
        if (selectedGame != null) {
            controller.loadGame(selectedGame);
        }
    }
    
    private void exportToPGN() {
        if (!controller.isGameInProgress() && controller.getCurrentGameData().getTotalMoves() == 0) {
            showMessage("No game to export.", "Export PGN");
            return;
        }
        
        String filename = JOptionPane.showInputDialog(this,
            "Enter filename for PGN export:",
            "Export to PGN",
            JOptionPane.QUESTION_MESSAGE);
            
        if (filename != null && !filename.trim().isEmpty()) {
            controller.exportToPGN(filename.trim());
        }
    }
    
    private void importFromPGN() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "PGN Files (*.pgn)", "pgn"));
            
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            showMessage("PGN import feature coming soon!", "Import PGN");
        }
    }
    
    private void showGameBrowser() {
        GameBrowserDialog browser = new GameBrowserDialog(this, controller);
        browser.setVisible(true);
    }
    
    private void showHelp() {
        String helpText = 
            "CHESS MASTER - HOW TO PLAY\n\n" +
            "BASIC RULES:\n" +
            "• Click a piece to select it\n" +
            "• Click a highlighted square to move\n" +
            "• Valid moves are shown in green\n" +
            "• Red highlighting indicates check\n\n" +
            "SPECIAL MOVES:\n" +
            "• Castling: Move king 2 squares toward rook\n" +
            "• En Passant: Capture pawn that just moved 2 squares\n" +
            "• Promotion: Pawn reaching end promotes to any piece\n\n" +
            "KEYBOARD SHORTCUTS:\n" +
            "• Ctrl+N: New Game\n" +
            "• Ctrl+S: Save Game\n" +
            "• Ctrl+O: Load Game\n" +
            "• Ctrl+Z: Undo Move\n" +
            "• Ctrl+Y: Redo Move\n" +
            "• Ctrl+F: Flip Board\n" +
            "• F11: Toggle Fullscreen";
            
        JTextArea helpArea = new JTextArea(helpText);
        helpArea.setEditable(false);
        helpArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        helpArea.setBackground(PANEL_COLOR);
        helpArea.setForeground(Color.WHITE);
        helpArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(helpArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showShortcuts() {
        String shortcutsText = 
            "KEYBOARD SHORTCUTS\n\n" +
            "GAME CONTROL:\n" +
            "Ctrl+N\t\tNew Game\n" +
            "Ctrl+S\t\tSave Game\n" +
            "Ctrl+Shift+S\tSave Game As\n" +
            "Ctrl+O\t\tLoad Game\n" +
            "Ctrl+E\t\tExport PGN\n" +
            "Ctrl+I\t\tImport PGN\n" +
            "Ctrl+Q\t\tExit\n\n" +
            "MOVE CONTROL:\n" +
            "Ctrl+Z\t\tUndo Move\n" +
            "Ctrl+Y\t\tRedo Move\n\n" +
            "VIEW CONTROL:\n" +
            "Ctrl+F\t\tFlip Board\n" +
            "Ctrl+R\t\tReset View\n" +
            "F11\t\tToggle Fullscreen\n\n" +
            "OTHER:\n" +
            "Ctrl+B\t\tGame Browser\n" +
            "Ctrl+H\t\tKeyboard Shortcuts\n" +
            "F1\t\tHelp";
            
        JTextArea shortcutsArea = new JTextArea(shortcutsText);
        shortcutsArea.setEditable(false);
        shortcutsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        shortcutsArea.setBackground(PANEL_COLOR);
        shortcutsArea.setForeground(Color.WHITE);
        shortcutsArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(shortcutsArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        String aboutText = 
            "♛ CHESS MASTER ♛\n" +
            "Professional Edition v1.0\n\n" +
            "A complete chess game implementation with:\n" +
            "• Full chess rules including special moves\n" +
            "• Professional game interface\n" +
            "• Game saving and loading\n" +
            "• PGN export support\n" +
            "• Move history and analysis\n\n" +
            "Built with Java Swing\n" +
            "Integrated with professional chess logic\n\n" +
            "© 2024 Chess Master Development Team";
            
        JTextArea aboutArea = new JTextArea(aboutText);
        aboutArea.setEditable(false);
        aboutArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aboutArea.setBackground(PANEL_COLOR);
        aboutArea.setForeground(Color.WHITE);
        aboutArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        aboutArea.setOpaque(true);
        
        JOptionPane.showMessageDialog(this, aboutArea, "About Chess Master", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void toggleFullscreen() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        if (device.getFullScreenWindow() == null) {
            // Enter fullscreen
            dispose();
            setUndecorated(true);
            device.setFullScreenWindow(this);
            setVisible(true);
        } else {
            // Exit fullscreen
            device.setFullScreenWindow(null);
            dispose();
            setUndecorated(false);
            setVisible(true);
        }
    }
    
    private void updateButtonStates() {
        if (controller != null) {
            undoButton.setEnabled(controller.canUndo());
            redoButton.setEnabled(controller.canRedo());
            saveGameButton.setEnabled(controller.isGameInProgress());
        }
    }
    
    private void handleWindowClosing() {
        if (controller.isGameInProgress()) {
            int option = JOptionPane.showConfirmDialog(this,
                "Do you want to save the current game before exiting?",
                "Exit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (option == JOptionPane.YES_OPTION) {
                saveGame();
                controller.shutdown();
                System.exit(0);
            } else if (option == JOptionPane.NO_OPTION) {
                controller.shutdown();
                System.exit(0);
            }
            // CANCEL_OPTION: do nothing, stay in game
        } else {
            controller.shutdown();
            System.exit(0);
        }
    }
}