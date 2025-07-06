package GUI;

import kernel.piezas.TipoDePieza;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Promotion Dialog - VERSIÓN MEJORADA CON BOTONES MÁS GRANDES
 */
public class PromotionDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private static final Color DARK_THEME = new Color(40, 44, 52);
    private static final Color PANEL_COLOR = new Color(50, 54, 62);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    private static final Color BUTTON_COLOR = new Color(70, 80, 95);
    private static final Color HOVER_COLOR = new Color(100, 110, 125);
    
    private TipoDePieza selectedPiece = null;
    private boolean isWhitePawn;
    
    public PromotionDialog(Frame parent) {
        this(parent, true); // Default to white pawn
    }
    
    public PromotionDialog(Frame parent, boolean isWhitePawn) {
        super(parent, "Pawn Promotion", true);
        this.isWhitePawn = isWhitePawn;
        
        initializeDialog();
        createComponents();
        setupKeyboardShortcuts();
    }
    
    private void initializeDialog() {
        setSize(600, 350); // MÁS GRANDE PARA ACOMODAR BOTONES
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Set background
        getContentPane().setBackground(DARK_THEME);
    }
    
    private void createComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Pieces selection panel
        JPanel piecesPanel = createPiecesPanel();
        add(piecesPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(" Promote Pawn ", SwingConstants.CENTER);
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22)); // MÁS GRANDE
        panel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel instructionLabel = new JLabel("Select the piece to promote to:", SwingConstants.CENTER);
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // MÁS GRANDE
        panel.add(instructionLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPiecesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 25, 25)); // MÁS ESPACIO
        panel.setBackground(DARK_THEME);
        panel.setBorder(new EmptyBorder(40, 30, 40, 30)); // MÁS MARGIN
        
        // Create piece buttons - MÁS GRANDES
        JButton queenButton = createPieceButton(TipoDePieza.REINA, "Queen", "Q");
        JButton rookButton = createPieceButton(TipoDePieza.TORRE, "Rook", "R");
        JButton bishopButton = createPieceButton(TipoDePieza.ALFIL, "Bishop", "B");
        JButton knightButton = createPieceButton(TipoDePieza.CABALLO, "Knight", "N");
        
        panel.add(queenButton);
        panel.add(rookButton);
        panel.add(bishopButton);
        panel.add(knightButton);
        
        // Auto-select Queen (most common choice)
        queenButton.requestFocus();
        
        return panel;
    }
    
    private JButton createPieceButton(TipoDePieza pieceType, String name, String shortcut) {
        // Get piece symbol
        String symbol = getPieceSymbol(pieceType);
        
        JButton button = new JButton() {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                Color bgColor = isHovered ? HOVER_COLOR : BUTTON_COLOR;
                if (hasFocus()) {
                    bgColor = ACCENT_COLOR;
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // BORDES MÁS REDONDOS
                
                // Border
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(hasFocus() ? 4 : 3)); // BORDE MÁS GRUESO
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
                
                // Piece symbol - MÁS GRANDE Y MEJOR POSICIONADO
                g2.setFont(new Font("Arial", Font.BOLD, 60)); // MUCHO MÁS GRANDE
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(symbol)) / 2;
                int y = (getHeight() / 2) + (fm.getAscent() / 2) - 20; // MEJOR CENTRADO
                g2.drawString(symbol, x, y);
                
                // Name - MÁS GRANDE Y MEJOR POSICIONADO
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14)); // MÁS GRANDE
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                x = (getWidth() - fm.stringWidth(name)) / 2;
                y = getHeight() - 25; // MEJOR POSICIÓN
                g2.drawString(name, x, y);
                
                // Shortcut - MÁS VISIBLE
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11)); // MÁS GRANDE Y BOLD
                g2.setColor(Color.LIGHT_GRAY);
                fm = g2.getFontMetrics();
                String shortcutText = "(" + shortcut + ")";
                x = (getWidth() - fm.stringWidth(shortcutText)) / 2;
                y = getHeight() - 8; // MEJOR POSICIÓN
                g2.drawString(shortcutText, x, y);
            }
        };
        
        // BOTONES MUCHO MÁS GRANDES
        button.setPreferredSize(new Dimension(120, 140)); // MUCHO MÁS GRANDE
        button.setMinimumSize(new Dimension(120, 140));
        button.setMaximumSize(new Dimension(120, 140));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener
        button.addActionListener(e -> {
            selectedPiece = pieceType;
            dispose();
        });
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.putClientProperty("isHovered", true);
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.putClientProperty("isHovered", false);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(DARK_THEME);
        panel.setBorder(new EmptyBorder(15, 20, 20, 20)); // MÁS ESPACIO
        
        JButton cancelButton = createStyledButton("Cancel", false);
        cancelButton.addActionListener(e -> {
            selectedPiece = null;
            dispose();
        });
        
        panel.add(cancelButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setBackground(isPrimary ? ACCENT_COLOR : BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // MÁS GRANDE
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2), // BORDE MÁS GRUESO
            new EmptyBorder(10, 20, 10, 20) // MÁS PADDING
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(isPrimary ? ACCENT_COLOR.brighter() : HOVER_COLOR);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(isPrimary ? ACCENT_COLOR : BUTTON_COLOR);
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void setupKeyboardShortcuts() {
        // Setup keyboard shortcuts
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "selectQueen");
        getRootPane().getActionMap().put("selectQueen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = TipoDePieza.REINA;
                dispose();
            }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "selectRook");
        getRootPane().getActionMap().put("selectRook", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = TipoDePieza.TORRE;
                dispose();
            }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "selectBishop");
        getRootPane().getActionMap().put("selectBishop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = TipoDePieza.ALFIL;
                dispose();
            }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "selectKnight");
        getRootPane().getActionMap().put("selectKnight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = TipoDePieza.CABALLO;
                dispose();
            }
        });
        
        // Escape to cancel
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = null;
                dispose();
            }
        });
        
        // Enter to select focused piece (default Queen)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectDefault");
        getRootPane().getActionMap().put("selectDefault", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPiece = TipoDePieza.REINA; // Default to Queen
                dispose();
            }
        });
    }
    
    private String getPieceSymbol(TipoDePieza pieceType) {
        switch (pieceType) {
            case REINA:   return " ";
            case TORRE:   return " ";
            case ALFIL:   return " ";
            case CABALLO: return " ";
            default:      return " "; // Default to Queen
        }
    }

    /**
     * Get the selected piece type
     */
    public TipoDePieza getSelectedPiece() {
        return selectedPiece;
    }
    
    /**
     * Set whether this is for a white or black pawn
     */
    public void setWhitePawn(boolean isWhite) {
        this.isWhitePawn = isWhite;
        repaint(); // Refresh to show correct piece colors
    }
    
    /**
     * Show the dialog and return the selected piece
     */
    public static TipoDePieza showPromotionDialog(Frame parent, boolean isWhitePawn) {
        PromotionDialog dialog = new PromotionDialog(parent, isWhitePawn);
        dialog.setVisible(true);
        return dialog.getSelectedPiece();
    }
}