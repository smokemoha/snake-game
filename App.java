/**
 * Main Application class for the Snake Game
 * This class initializes and sets up the game window using Java Swing
 */

 import javax.swing.*; // Importing Swing library for GUI components like JFrame

 public class App {
 
     /**
      * Main entry point of the Snake Game application
      * Sets up the game window and initializes the game components
      *
      * Mathematical concepts used:
      * - Square geometry: width = height = 600 pixels for a perfect square board.
      * - Coordinate system: Origin (0,0) is the top-left of the window.
      * - Pixels are the unit of measurement for both window and grid layout.
      *
      * @param args Command line arguments (not used here)
      * @throws Exception Handles unexpected issues during game setup
      */
     public static void main(String[] args) throws Exception {
         
         // ---------------------------
         // Define the game board size
         // ---------------------------
         
         // Using a square board helps ensure consistent gameplay in all directions.
         // The board is 600 pixels wide and 600 pixels tall.
         // Each tile is 25x25 pixels (see SnakeGame class), so we have:
         //   boardWidth / tileSize = 600 / 25 = 24 tiles across and down
         int boardWidth = 600;         // Width of the board in pixels
         int boardHeight = boardWidth; // Height equals width for a square board
 
         // --------------------------------
         // Create and configure game window
         // --------------------------------
 
         // JFrame is the main window where the game will be displayed
         JFrame frame = new JFrame("Snake"); // Create a window titled "Snake"
         
         // Make the window appear on the screen
         frame.setVisible(true);
 
         // Set the total size of the window in pixels
         frame.setSize(boardWidth, boardHeight);
 
         // Position the window in the center of the screen
         // This calculates the center of the user's screen based on screen resolution
         frame.setLocationRelativeTo(null);
 
         // Disable resizing to keep the game area dimensions fixed
         frame.setResizable(false);
 
         // Ensure the application exits when the window is closed
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
         // --------------------------------------
         // Create and attach the Snake game panel
         // --------------------------------------
 
         // Step 1: Create the game panel with specified width and height
         SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
 
         // Step 2: Add the game panel to the JFrame’s content area
         frame.add(snakeGame);
 
         // Step 3: Pack resizes the window to fit the preferred sizes of added components
         // This ensures the window exactly fits the SnakeGame panel (600x600)
         frame.pack();
 
         // Step 4: Request keyboard focus so that SnakeGame can receive input
         // Without this, key events may not be detected
         snakeGame.requestFocus();
     }
 }
 