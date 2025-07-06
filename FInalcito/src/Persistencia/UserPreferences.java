package Persistencia;

import java.io.*;

public class UserPreferences implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String PREFS_FILE = "chess_preferences.dat";
    
    private String theme = "Classic";
    private boolean soundEnabled = true;
    private boolean showCoordinates = true;
    private boolean highlightMoves = true;
    private boolean animationsEnabled = true;
    private int animationSpeed = 5;
    
    // Getters and setters
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
    
    public boolean isShowCoordinates() { return showCoordinates; }
    public void setShowCoordinates(boolean showCoordinates) { this.showCoordinates = showCoordinates; }
    
    public boolean isHighlightMoves() { return highlightMoves; }
    public void setHighlightMoves(boolean highlightMoves) { this.highlightMoves = highlightMoves; }
    
    public boolean isAnimationsEnabled() { return animationsEnabled; }
    public void setAnimationsEnabled(boolean animationsEnabled) { this.animationsEnabled = animationsEnabled; }
    
    public int getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(int animationSpeed) { this.animationSpeed = animationSpeed; }
    
    // Save and load methods
    public static void save(UserPreferences prefs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PREFS_FILE))) {
            oos.writeObject(prefs);
        } catch (IOException e) {
            System.err.println("Error saving preferences: " + e.getMessage());
        }
    }
    
    public static UserPreferences load() {
        File prefsFile = new File(PREFS_FILE);
        if (prefsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(prefsFile))) {
                return (UserPreferences) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading preferences: " + e.getMessage());
            }
        }
        return new UserPreferences(); // Return default preferences
    }
}
