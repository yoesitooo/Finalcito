package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Game History Panel - CON BOTONES DE MEJOR CONTRASTE
 */
public class GameHistoryPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final Color PANEL_COLOR = new Color(50, 54, 62);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    
    private JTextArea historyArea;
    private JScrollPane scrollPane;
    private List<String> moveList;
    private int selectedMoveIndex = -1;
    
    public GameHistoryPanel() {
        moveList = new ArrayList<>();
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(PANEL_COLOR);
        setPreferredSize(new Dimension(230, 300));
        
        // Title
        JLabel titleLabel = new JLabel("Move History", SwingConstants.CENTER);
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // History text area
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(40, 44, 52));
        historyArea.setForeground(Color.WHITE);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        historyArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        historyArea.setLineWrap(false);
        historyArea.setWrapStyleWord(false);
        
        // Add mouse listener for move navigation
        historyArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Double-click to navigate to move
                    int position = historyArea.viewToModel2D(e.getPoint());
                    String text = historyArea.getText();
                    
                    // Find which move was clicked
                    int moveIndex = findMoveIndexAtPosition(text, position);
                    if (moveIndex != -1) {
                        selectMove(moveIndex);
                    }
                }
            }
        });
        
        // Scroll pane
        scrollPane = new JScrollPane(historyArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        scrollPane.getVerticalScrollBar().setBackground(PANEL_COLOR);
        scrollPane.getHorizontalScrollBar().setBackground(PANEL_COLOR);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Control buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBackground(PANEL_COLOR);
        
        // Solo botón Clear - Export PGN se quitó porque ya existe en el menú principal
        JButton clearButton = createSmallButton("Clear");
        clearButton.addActionListener(e -> clearHistory());
        panel.add(clearButton);
        
        return panel;
    }
    
    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        
        // *** COLORES CON MEJOR CONTRASTE ***
        button.setBackground(new Color(70, 80, 95)); // Fondo más oscuro
        button.setForeground(Color.BLACK); // *** TEXTO NEGRO ***
        button.setFont(new Font("Segoe UI", Font.BOLD, 10)); // Texto en negrita
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), // Borde más grueso
            new EmptyBorder(5, 10, 5, 10) // Más padding
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect mejorado
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 110, 125)); // Hover más claro
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO EN HOVER ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR.brighter(), 3),
                    new EmptyBorder(5, 10, 5, 10)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 80, 95)); // Volver al color original
                button.setForeground(Color.BLACK); // *** TEXTO NEGRO SIEMPRE ***
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                    new EmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Add a move to the history
     */
    public void addMove(String move, int moveNumber) {
        moveList.add(move);
        updateDisplay();
        scrollToBottom();
    }
    
    /**
     * Remove the last move from history
     */
    public void removeLastMove() {
        if (!moveList.isEmpty()) {
            moveList.remove(moveList.size() - 1);
            updateDisplay();
        }
    }
    
    /**
     * Load complete move history
     */
    public void loadMoveHistory(List<String> moves) {
        moveList.clear();
        moveList.addAll(moves);
        updateDisplay();
    }
    
    /**
     * Clear all history
     */
    public void clearHistory() {
        moveList.clear();
        selectedMoveIndex = -1;
        updateDisplay();
    }
    
    /**
     * Update the display with current move list
     */
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < moveList.size(); i++) {
            if (i % 2 == 0) {
                // White move - start new line with move number
                int moveNum = (i / 2) + 1;
                if (i > 0) sb.append("\n");
                sb.append(String.format("%2d. %-8s", moveNum, moveList.get(i)));
            } else {
                // Black move - continue same line
                sb.append(String.format(" %-8s", moveList.get(i)));
            }
        }
        
        historyArea.setText(sb.toString());
        
        // Highlight selected move if any
        if (selectedMoveIndex >= 0 && selectedMoveIndex < moveList.size()) {
            highlightMove(selectedMoveIndex);
        }
    }
    
    /**
     * Scroll to bottom of history
     */
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    /**
     * Find which move index corresponds to a text position
     */
    private int findMoveIndexAtPosition(String text, int position) {
        if (position < 0 || position >= text.length()) {
            return -1;
        }
        
        // Count moves up to this position
        String beforePosition = text.substring(0, position);
        String[] lines = beforePosition.split("\n");
        
        int moveIndex = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            // Count moves in this line
            if (line.matches(".*\\d+\\..*")) {
                // Line starts with move number
                String[] parts = line.split("\\s+");
                for (int i = 1; i < parts.length; i++) {
                    if (!parts[i].trim().isEmpty()) {
                        moveIndex++;
                    }
                }
            }
        }
        
        return Math.min(moveIndex - 1, moveList.size() - 1);
    }
    
    /**
     * Select and highlight a specific move
     */
    private void selectMove(int moveIndex) {
        if (moveIndex >= 0 && moveIndex < moveList.size()) {
            selectedMoveIndex = moveIndex;
            highlightMove(moveIndex);
            
            // TODO: Notify controller to show board position at this move
            // controller.showPositionAtMove(moveIndex);
        }
    }
    
    /**
     * Highlight a specific move in the text
     */
    private void highlightMove(int moveIndex) {
        // This is a simplified highlighting - in a real implementation,
        // you might want to use a more sophisticated approach
        try {
            String text = historyArea.getText();
            String[] moves = text.split("\\s+");
            
            // Find the position of the move in the text
            // This is a basic implementation - could be improved
            historyArea.setSelectionStart(0);
            historyArea.setSelectionEnd(0);
            
        } catch (Exception e) {
            System.err.println("Error highlighting move: " + e.getMessage());
        }
    }
    
    /**
     * Get the current move list
     */
    public List<String> getMoveList() {
        return new ArrayList<>(moveList);
    }
    
    /**
     * Get the currently selected move index
     */
    public int getSelectedMoveIndex() {
        return selectedMoveIndex;
    }
    
    /**
     * Get move at specific index
     */
    public String getMoveAt(int index) {
        if (index >= 0 && index < moveList.size()) {
            return moveList.get(index);
        }
        return null;
    }
    
    /**
     * Get total number of moves
     */
    public int getMoveCount() {
        return moveList.size();
    }
    
    /**
     * Check if history is empty
     */
    public boolean isEmpty() {
        return moveList.isEmpty();
    }
}