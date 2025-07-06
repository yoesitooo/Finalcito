package Persistencia;

import kernel.tablero.Tablero;
import java.io.*;
import java.util.*;

public class GameDataManager {
    private static final String GAMES_FOLDER = "saved_games";
    private static final String GAME_EXTENSION = ".chess";
    
    static {
        initialize();
    }
    
    public static void initialize() {
        try {
            File gamesDir = new File(GAMES_FOLDER);
            if (!gamesDir.exists()) {
                boolean created = gamesDir.mkdirs();
                if (created) {
                    System.out.println("Carpeta de juegos guardados creada: " + gamesDir.getAbsolutePath());
                } else {
                    System.err.println("No se pudo crear la carpeta de juegos guardados");
                }
            } else {
                System.out.println("Carpeta de juegos encontrada: " + gamesDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error inicializando GameDataManager: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static boolean saveGame(ChessGameData gameData, String gameName) {
        if (gameData == null || gameName == null || gameName.trim().isEmpty()) {
            System.err.println("Error: Datos de juego o nombre inválidos");
            return false;
        }
        
        try {
            // Limpiar el nombre del archivo
            String cleanName = cleanFileName(gameName.trim());
            File gameFile = new File(GAMES_FOLDER, cleanName + GAME_EXTENSION);
            
            // Asegurar que el directorio padre existe
            File parentDir = gameFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            System.out.println("Intentando guardar en: " + gameFile.getAbsolutePath());
            
            // Guardar con manejo mejorado de errores
            try (FileOutputStream fos = new FileOutputStream(gameFile);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                
                // Verificar que los datos no son null antes de escribir
                if (gameData.getTablero() == null) {
                    System.err.println("Error: Tablero es null");
                    return false;
                }
                
                if (gameData.getMetadata() == null) {
                    System.err.println("Error: Metadata es null");
                    return false;
                }
                
                oos.writeObject(gameData.getTablero());
                oos.writeObject(gameData.getMetadata());
                oos.writeObject(gameData.getMoveHistory() != null ? gameData.getMoveHistory() : new ArrayList<String>());
                
                oos.flush();
                fos.flush();
                
                System.out.println("Juego guardado exitosamente: " + cleanName);
                return true;
            }
            
        } catch (IOException e) {
            System.err.println("Error de E/S guardando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado guardando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static ChessGameData loadGame(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            System.err.println("Error: Nombre de juego inválido");
            return null;
        }
        
        try {
            String cleanName = cleanFileName(gameName.trim());
            File gameFile = new File(GAMES_FOLDER, cleanName + GAME_EXTENSION);
            
            if (!gameFile.exists()) {
                System.err.println("Error: Archivo de juego no encontrado: " + gameFile.getAbsolutePath());
                return null;
            }
            
            System.out.println("Cargando juego desde: " + gameFile.getAbsolutePath());
            
            ChessGameData gameData = new ChessGameData();
            try (FileInputStream fis = new FileInputStream(gameFile);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                
                gameData.setTablero((Tablero) ois.readObject());
                gameData.setMetadata((GameMetadata) ois.readObject());
                
                // Manejar posible lista null
                Object moveHistoryObj = ois.readObject();
                if (moveHistoryObj instanceof List) {
                    gameData.setMoveHistory((List<String>) moveHistoryObj);
                } else {
                    gameData.setMoveHistory(new ArrayList<String>());
                }
                
                System.out.println("Juego cargado exitosamente: " + cleanName);
                return gameData;
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de juego no encontrado: " + gameName);
            return null;
        } catch (IOException e) {
            System.err.println("Error de E/O cargando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Error de clase no encontrada cargando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado cargando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<String> getAvailableGames() {
        List<String> games = new ArrayList<>();
        
        try {
            File gamesDir = new File(GAMES_FOLDER);
            
            if (!gamesDir.exists() || !gamesDir.isDirectory()) {
                System.out.println("Directorio de juegos no existe o no es un directorio");
                return games;
            }
            
            File[] files = gamesDir.listFiles((dir, name) -> name.endsWith(GAME_EXTENSION));
            
            if (files != null) {
                for (File file : files) {
                    String gameName = file.getName();
                    gameName = gameName.substring(0, gameName.length() - GAME_EXTENSION.length());
                    games.add(gameName);
                }
                System.out.println("Encontrados " + games.size() + " juegos guardados");
            } else {
                System.out.println("No se pudieron listar los archivos del directorio");
            }
            
        } catch (Exception e) {
            System.err.println("Error obteniendo lista de juegos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return games;
    }
    
    public static boolean deleteGame(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            System.err.println("Error: Nombre de juego inválido para eliminar");
            return false;
        }
        
        try {
            String cleanName = cleanFileName(gameName.trim());
            File gameFile = new File(GAMES_FOLDER, cleanName + GAME_EXTENSION);
            
            if (!gameFile.exists()) {
                System.err.println("Error: Archivo de juego no existe para eliminar: " + gameFile.getAbsolutePath());
                return false;
            }
            
            boolean deleted = gameFile.delete();
            if (deleted) {
                System.out.println("Juego eliminado exitosamente: " + cleanName);
            } else {
                System.err.println("No se pudo eliminar el archivo: " + gameFile.getAbsolutePath());
            }
            
            return deleted;
            
        } catch (Exception e) {
            System.err.println("Error eliminando juego '" + gameName + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static GameMetadata getGameMetadata(String gameName) {
        try {
            ChessGameData gameData = loadGame(gameName);
            return gameData != null ? gameData.getMetadata() : null;
        } catch (Exception e) {
            System.err.println("Error obteniendo metadata del juego '" + gameName + "': " + e.getMessage());
            return null;
        }
    }
    
    public static GameMetadata getLastPlayedGame() {
        try {
            List<String> games = getAvailableGames();
            GameMetadata lastGame = null;
            
            for (String gameName : games) {
                GameMetadata metadata = getGameMetadata(gameName);
                if (metadata != null) {
                    if (lastGame == null || 
                        (metadata.getLastModified() != null && lastGame.getLastModified() != null &&
                         metadata.getLastModified().isAfter(lastGame.getLastModified()))) {
                        lastGame = metadata;
                    }
                }
            }
            
            return lastGame;
            
        } catch (Exception e) {
            System.err.println("Error obteniendo último juego: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Limpia el nombre del archivo removiendo caracteres inválidos
     */
    private static String cleanFileName(String fileName) {
        if (fileName == null) return "unnamed_game";
        
        // Reemplazar caracteres problemáticos
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_")
                      .replaceAll("\\s+", "_")
                      .trim();
    }
    
    /**
     * Verifica que el sistema de persistencia está funcionando
     */
    public static boolean testPersistence() {
        try {
            // Crear un juego de prueba
            ChessGameData testData = new ChessGameData();
            GameMetadata testMetadata = new GameMetadata();
            testMetadata.setGameName("test_game");
            testData.setMetadata(testMetadata);
            testData.setMoveHistory(new ArrayList<String>());
            
            // Crear un tablero simple de prueba
            kernel.tablero.Tablero testBoard = kernel.tablero.Tablero.crearTableroStandar();
            testData.setTablero(testBoard);
            
            // Intentar guardar
            boolean saved = saveGame(testData, "persistence_test");
            if (!saved) {
                System.err.println("Test de persistencia falló: No se pudo guardar");
                return false;
            }
            
            // Intentar cargar
            ChessGameData loaded = loadGame("persistence_test");
            if (loaded == null) {
                System.err.println("Test de persistencia falló: No se pudo cargar");
                return false;
            }
            
            // Limpiar archivo de prueba
            deleteGame("persistence_test");
            
            System.out.println("Test de persistencia exitoso");
            return true;
            
        } catch (Exception e) {
            System.err.println("Test de persistencia falló con excepción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}