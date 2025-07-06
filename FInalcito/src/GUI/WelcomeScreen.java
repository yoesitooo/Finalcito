package GUI;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import Persistencia.GameDataManager;
import Persistencia.GameMetadata;

/**
 * Welcome Screen - SIN BOTÓN PREFERENCES
 */
public class WelcomeScreen extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static final Color DARK_THEME = new Color(40, 44, 52);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    private static final Color BUTTON_COLOR = new Color(60, 68, 82);
    private static final Color HOVER_COLOR = new Color(80, 88, 102);
    
    private JPanel mainPanel;
    private JLabel lastGameLabel;
    private JButton continueButton;
    private Timer animationTimer;
    private float animationOffset = 0;
    
    public WelcomeScreen() {
        initializeWindow();
        createComponents();
        setupAnimations();
        loadLastGameInfo();
    }
    
    private void initializeWindow() {
        setTitle("Chess Master - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createComponents() {
        mainPanel = new WelcomePanel();
        mainPanel.setBackground(DARK_THEME);
        mainPanel.setLayout(null);
        setContentPane(mainPanel);
        
        // Title
        JLabel titleLabel = createStyledLabel(" CHESS MASTER ", 48, Color.WHITE, true);
        titleLabel.setBounds(150, 80, 500, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = createStyledLabel("Professional Chess Experience", 16, ACCENT_COLOR, false);
        subtitleLabel.setBounds(200, 140, 400, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);
        
        // Last game info panel
        createLastGamePanel();
        
        // Main buttons - SIN PREFERENCES
        createMainButtons();
        
        // Version info
        JLabel versionLabel = createStyledLabel("Version 1.0 - Premium Edition", 10, Color.GRAY, false);
        versionLabel.setBounds(20, 560, 200, 20);
        mainPanel.add(versionLabel);
        
        // Current time
        JLabel timeLabel = createStyledLabel(getCurrentTime(), 10, Color.GRAY, false);
        timeLabel.setBounds(650, 560, 130, 20);
        mainPanel.add(timeLabel);
    }
    
    private void createLastGamePanel() {
        // Last game panel
        JPanel lastGamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2.setColor(new Color(50, 54, 62));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Draw border
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 15, 15));
            }
        };
        lastGamePanel.setBounds(50, 200, 300, 120);
        lastGamePanel.setOpaque(false);
        lastGamePanel.setLayout(null);
        
        JLabel lastGameTitle = createStyledLabel("Last Game", 14, ACCENT_COLOR, true);
        lastGameTitle.setBounds(20, 10, 100, 20);
        lastGamePanel.add(lastGameTitle);
        
        lastGameLabel = createStyledLabel("No games found", 12, Color.LIGHT_GRAY, false);
        lastGameLabel.setBounds(20, 35, 260, 60);
        lastGamePanel.add(lastGameLabel);
        
        mainPanel.add(lastGamePanel);
    }
    
    private void createMainButtons() {
        // Continue Game button
        continueButton = createStyledButton("Continue Game", 450, 220, 280, 50);
        continueButton.addActionListener(e -> continueLastGame());
        continueButton.setEnabled(false); // Will be enabled if last game exists
        mainPanel.add(continueButton);
        
        // New Game button
        JButton newGameButton = createStyledButton("New Game", 450, 290, 280, 50);
        newGameButton.addActionListener(e -> startNewGame());
        mainPanel.add(newGameButton);
        
        // Load Game button
        JButton loadGameButton = createStyledButton("Load Game", 450, 360, 280, 50);
        loadGameButton.addActionListener(e -> loadGame());
        mainPanel.add(loadGameButton);
        
        // QUITAMOS EL BOTÓN PREFERENCES COMPLETAMENTE
        
        // Exit button - ahora más centrado
        JButton exitButton = createStyledButton("Exit", 515, 430, 130, 40);
        exitButton.addActionListener(e -> exitApplication());
        mainPanel.add(exitButton);
    }
    
    private JLabel createStyledLabel(String text, int fontSize, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }
    
    private JButton createStyledButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                Color bgColor = isEnabled() ? (isHovered ? HOVER_COLOR : BUTTON_COLOR) : Color.GRAY;
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                
                // Border
                g2.setColor(isEnabled() ? ACCENT_COLOR : Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 10, 10));
                
                // Text
                g2.setColor(isEnabled() ? Color.WHITE : Color.LIGHT_GRAY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
            }
            
            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                repaint();
            }
        };
        
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", true);
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", false);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void setupAnimations() {
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationOffset += 0.1f;
                if (animationOffset > Math.PI * 2) {
                    animationOffset = 0;
                }
                mainPanel.repaint();
            }
        });
        animationTimer.start();
    }
    
    private void loadLastGameInfo() {
        try {
            GameMetadata lastGame = GameDataManager.getLastPlayedGame();
            if (lastGame != null) {
                String gameInfo = String.format(
                    "<html><b>%s</b><br>" +
                    "Date: %s<br>" +
                    "Moves: %d | Duration: %s<br>" +
                    "Result: %s</html>",
                    lastGame.getGameName(),
                    lastGame.getCreationDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    lastGame.getTotalMoves(),
                    lastGame.getFormattedDuration(),
                    lastGame.getGameResultDescription()
                );
                lastGameLabel.setText(gameInfo);
                continueButton.setEnabled(true);
            }
        } catch (Exception e) {
            System.err.println("Error loading last game info: " + e.getMessage());
        }
    }
    
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    // Button actions
    private void continueLastGame() {
        try {
            GameMetadata lastGame = GameDataManager.getLastPlayedGame();
            if (lastGame != null) {
                dispose();
                ChessGameController controller = new ChessGameController();
                controller.loadGame(lastGame.getGameName());
                controller.getGameWindow().setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading last game: " + e.getMessage(), 
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void startNewGame() {
        dispose();
        ChessGameController controller = new ChessGameController();
        controller.startNewGame();
        controller.getGameWindow().setVisible(true);
    }
    
    private void loadGame() {
        GameBrowserDialog browser = new GameBrowserDialog(this);
        browser.setVisible(true);
        
        if (browser.getSelectedGame() != null) {
            dispose();
            ChessGameController controller = new ChessGameController();
            controller.loadGame(browser.getSelectedGame());
            controller.getGameWindow().setVisible(true);
        }
    }
    
    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit Chess Master?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // Custom panel for background effects
    private class WelcomePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, DARK_THEME,
                getWidth(), getHeight(), new Color(60, 64, 72)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Animated chess pieces in background
            drawAnimatedBackground(g2);
        }
        
        private void drawAnimatedBackground(Graphics2D g2) {
            g2.setColor(new Color(255, 255, 255, 20));
            g2.setFont(new Font("Arial Unicode MS", Font.BOLD, 80));
            
            // Floating chess pieces
            String[] pieces = {"♔", "♕", "♖", "♗", "♘", "♙"};
            for (int i = 0; i < pieces.length; i++) {
                float x = 50 + i * 120 + (float)(Math.sin(animationOffset + i) * 20);
                float y = 400 + (float)(Math.cos(animationOffset + i * 0.5) * 30);
                g2.drawString(pieces[i], x, y);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize managers
                GameDataManager.initialize();
                
                WelcomeScreen welcome = new WelcomeScreen();
                welcome.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting Chess Master: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}