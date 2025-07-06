package GUI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private static boolean soundEnabled = true;
    
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }
    
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public static void playMoveSound() {
        playSound("move.wav");
    }
    
    public static void playCaptureSound() {
        playSound("capture.wav");
    }
    
    public static void playCheckSound() {
        playSound("check.wav");
    }
    
    public static void playCastleSound() {
        playSound("castle.wav");
    }
    
    public static void playEnPassantSound() {
        playSound("capture.wav"); // Use capture sound for en passant
    }
    
    private static void playSound(String soundFile) {
        if (!soundEnabled) return;
        
        // Simple implementation - in a real app, you'd load actual sound files
        try {
            // This is a placeholder - you'll need actual sound files
            System.out.println("Playing sound: " + soundFile);
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}