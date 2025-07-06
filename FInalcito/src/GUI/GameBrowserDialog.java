package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Persistencia.ChessGameData;
import Persistencia.GameDataManager;
import Persistencia.GameMetadata;
import Persistencia.PGNHandler;

/**
 * Game Browser Dialog - Professional game management interface
 */
public class GameBrowserDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private static final Color DARK_THEME = new Color(40, 44, 52);
    private static final Color PANEL_COLOR = new Color(50, 54, 62);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    
    private ChessGameController controller;
    private JTable gamesTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JButton refreshButton;
    private String selectedGame = null;
    
    public GameBrowserDialog(Frame parent, ChessGameController controller) {
        super(parent, "Game Browser", true);
        this.controller = controller;
        
        initializeDialog();
        createComponents();
        setupEventHandlers();
        loadGamesList();
    }
    
    // Constructor without controller for welcome screen
    public GameBrowserDialog(Frame parent) {
        this(parent, null);
    }
    
    private void initializeDialog() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setBackground(DARK_THEME);
    }
    
    private void createComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_THEME);
        
        // Title panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("♖ Game Browser ♜", SwingConstants.CENTER);
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel subtitleLabel = new JLabel("Load, manage, and organize your chess games", SwingConstants.CENTER);
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(DARK_THEME);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(searchLabel, BorderLayout.WEST);
        
        searchField = new JTextField();
        searchField.setBackground(PANEL_COLOR);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            new EmptyBorder(5, 8, 5, 8)
        ));
        searchField.addActionListener(e -> filterGames());
        panel.add(searchField, BorderLayout.CENTER);
        
        refreshButton = createStyledButton("Refresh", false);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_THEME);
        panel.setBorder(new EmptyBorder(0, 20, 10, 20));
        
        // Create table
        String[] columnNames = {"Game Name", "Date", "Moves", "Duration", "Result", "Size"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        gamesTable = new JTable(tableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(gamesTable);
        scrollPane.setBackground(DARK_THEME);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupTable() {
        gamesTable.setBackground(PANEL_COLOR);
        gamesTable.setForeground(Color.WHITE);
        gamesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gamesTable.setRowHeight(25);
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesTable.setSelectionBackground(ACCENT_COLOR);
        gamesTable.setSelectionForeground(Color.WHITE);
        gamesTable.setGridColor(new Color(70, 74, 82));
        
        // Header styling
        gamesTable.getTableHeader().setBackground(new Color(60, 64, 72));
        gamesTable.getTableHeader().setForeground(Color.WHITE);
        gamesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        gamesTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ACCENT_COLOR));
        
        // Column widths
        gamesTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Name
        gamesTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Date
        gamesTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Moves
        gamesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Duration
        gamesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Result
        gamesTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Size
        
        // Custom cell renderer for result column
        gamesTable.getColumnModel().getColumn(4).setCellRenderer(new ResultCellRenderer());
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_THEME);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // Left side - info
        JLabel infoLabel = new JLabel("Double-click a game to load it directly");
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panel.add(infoLabel, BorderLayout.WEST);
        
        // Right side - buttons
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setBackground(DARK_THEME);
        
        loadButton = createStyledButton("Load Game", true);
        loadButton.setForeground(Color.BLACK);
        loadButton.setEnabled(false);
        loadButton.addActionListener(e -> loadSelectedGame());
        
        exportButton = createStyledButton("Export PGN", false);
        exportButton.setEnabled(false);
        exportButton.addActionListener(e -> exportSelectedGame());
        
        deleteButton = createStyledButton("Delete", false);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteSelectedGame());
        
        JButton cancelButton = createStyledButton("Cancel", false);
        cancelButton.addActionListener(e -> {
            selectedGame = null;
            dispose();
        });
        
        buttonGroup.add(loadButton);
        buttonGroup.add(exportButton);
        buttonGroup.add(deleteButton);
        buttonGroup.add(cancelButton);
        
        panel.add(buttonGroup, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setBackground(isPrimary ? ACCENT_COLOR : new Color(60, 68, 82));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(isPrimary ? ACCENT_COLOR.brighter() : new Color(80, 88, 102));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(isPrimary ? ACCENT_COLOR : new Color(60, 68, 82));
            }
        });
        
        return button;
    }
    
    private void setupEventHandlers() {
        // Table selection listener
        gamesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = gamesTable.getSelectedRow() != -1;
                loadButton.setEnabled(hasSelection);
                exportButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            }
        });
        
        // Double-click to load
        gamesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && gamesTable.getSelectedRow() != -1) {
                    loadSelectedGame();
                }
            }
        });
        
        // Search field listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterGames(); }
        });
    }
    
    private void loadGamesList() {
        tableModel.setRowCount(0);
        
        try {
            List<String> gameNames = GameDataManager.getAvailableGames();
            
            for (String gameName : gameNames) {
                GameMetadata gameInfo = GameDataManager.getGameMetadata(gameName);
                
                if (gameInfo != null) {
                    Object[] rowData = {
                        gameInfo.getGameName(),
                        gameInfo.getCreationDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                        gameInfo.getTotalMoves(),
                        gameInfo.getFormattedDuration(),
                        gameInfo.getGameResultDescription(),
                        gameInfo.getFormattedFileSize()
                    };
                    tableModel.addRow(rowData);
                }
            }
            
            // Update title with count
            setTitle("Game Browser (" + gameNames.size() + " games)");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading games list: " + e.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filterGames() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            loadGamesList();
            return;
        }
        
        // Simple filtering - could be enhanced
        tableModel.setRowCount(0);
        
        try {
            List<String> gameNames = GameDataManager.getAvailableGames();
            int matchCount = 0;
            
            for (String gameName : gameNames) {
                if (gameName.toLowerCase().contains(searchText)) {
                    GameMetadata gameInfo = GameDataManager.getGameMetadata(gameName);
                    
                    if (gameInfo != null) {
                        Object[] rowData = {
                            gameInfo.getGameName(),
                            gameInfo.getCreationDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                            gameInfo.getTotalMoves(),
                            gameInfo.getFormattedDuration(),
                            gameInfo.getGameResultDescription(),
                            gameInfo.getFormattedFileSize()
                        };
                        tableModel.addRow(rowData);
                        matchCount++;
                    }
                }
            }
            
            setTitle("Game Browser (" + matchCount + " matches)");
            
        } catch (Exception e) {
            System.err.println("Error filtering games: " + e.getMessage());
        }
    }
    
    private void loadSelectedGame() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String gameName = (String) tableModel.getValueAt(selectedRow, 0);
        
        if (controller != null) {
            // Direct load through controller
            dispose();
            controller.loadGame(gameName);
        } else {
            // Return selection for welcome screen
            selectedGame = gameName;
            dispose();
        }
    }
    
    private void exportSelectedGame() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String gameName = (String) tableModel.getValueAt(selectedRow, 0);
        
        String pgnFilename = JOptionPane.showInputDialog(this,
            "Enter filename for PGN export:",
            gameName + "_export");
            
        if (pgnFilename != null && !pgnFilename.trim().isEmpty()) {
            try {
                ChessGameData gameData = GameDataManager.loadGame(gameName);
                if (gameData != null) {
                    PGNHandler pgnHandler = new PGNHandler();
                    if (pgnHandler.exportGame(gameData, pgnFilename.trim())) {
                        JOptionPane.showMessageDialog(this,
                            "Game exported successfully to: " + pgnFilename + ".pgn",
                            "Export Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Failed to export game to PGN",
                            "Export Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to load game for export",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting game: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSelectedGame() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String gameName = (String) tableModel.getValueAt(selectedRow, 0);
        
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the game '" + gameName + "'?\nThis action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (option == JOptionPane.YES_OPTION) {
            try {
                if (GameDataManager.deleteGame(gameName)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this,
                        "Game deleted successfully",
                        "Delete Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete game",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting game: " + e.getMessage(),
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Custom cell renderer for game result
    private class ResultCellRenderer extends JLabel implements TableCellRenderer {
        
        public ResultCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setText(value.toString());
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                
                // Color code results
                String result = value.toString().toLowerCase();
                if (result.contains("white") || result.equals("1-0")) {
                    setForeground(Color.WHITE);
                } else if (result.contains("black") || result.equals("0-1")) {
                    setForeground(Color.LIGHT_GRAY);
                } else if (result.contains("draw") || result.equals("1/2-1/2")) {
                    setForeground(Color.YELLOW);
                } else if (result.contains("progress")) {
                    setForeground(ACCENT_COLOR);
                } else {
                    setForeground(Color.LIGHT_GRAY);
                }
            }
            
            return this;
        }
    }
    
    // Public getter for selected game (used by welcome screen)
    public String getSelectedGame() {
        return selectedGame;
    }
}
