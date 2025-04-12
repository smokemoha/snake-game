
/**
 * Main Application class for the Snake Game
 * This class initializes and sets up the game window using Java Swing
 */
import javax.swing.*;

public class App {
    /**
     * Main entry point of the Snake Game application
     * Sets up the game window and initializes the game components
     * 
     * Mathematical concepts used:
     * - Square geometry: width = height = 600 pixels for perfect square board
     * - Coordinate system: (0,0) starts at top-left corner of window
     * - Pixel-based measurements for window dimensions
     * 
     * @param args Command line arguments
     * @throws Exception Handles potential exceptions during game initialization
     */
    public static void main(String[] args) throws Exception {
        // Define the game board dimensions
        // Using a square board where width equals height for balanced gameplay
        // Mathematical relationship: width = height = 600
        int boardWidth = 600; // Width in pixels
        int boardHeight = boardWidth; // Height matches width for square board
        JFrame frame = new JFrame("Snake"); // Create window with title "Snake"
        frame.setVisible(true); // Make the window visible on screen
        frame.setSize(boardWidth, boardHeight); // Set window dimensions using calculated values
        frame.setLocationRelativeTo(null); // Center window using coordinate system calculation
        frame.setResizable(false); // Prevent window resizing for consistent gameplay
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application when window is closed
        // Initialize the main game component
        // Game logic initialization:
        // 1. Create game instance with board dimensions
        // 2. Add to window container
        // 3. Optimize window size
        // 4. Set input focus
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight); // Create new game instance
        frame.add(snakeGame); // Add game component to window's content pane
        frame.pack(); // Adjust window size to preferred dimensions of components
        snakeGame.requestFocus(); // Set keyboard focus for input handling
    }
}
