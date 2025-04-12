// Importing necessary libraries for GUI and event handling
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// SnakeGame extends JPanel for drawing and implements ActionListener and KeyListener for game logic and controls
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    
    // Tile class represents each grid square on the board
    private class Tile {
        int x; // x-coordinate in grid units (not pixels)
        int y; // y-coordinate in grid units

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    // Game board dimensions (in pixels)
    int boardWidth;
    int boardHeight;

    // Size of each square tile on the grid (25x25 pixels)
    int tileSize = 25;
    
    // Snake head and body
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food tile
    Tile food;
    Random random; // for random food placement

    // Snake direction control (velocityX, velocityY)
    int velocityX; // horizontal movement: -1 (left), 0 (no move), 1 (right)
    int velocityY; // vertical movement: -1 (up), 0 (no move), 1 (down)

    Timer gameLoop; // timer to trigger game updates

    boolean gameOver = false; // flag to track game state
    JDialog gameOverDialog;   // custom dialog window when game ends

    // Constructor to initialize game board
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Set JPanel properties
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true); // allows key events to be received

        // Start game
        initializeGame();
    }
    
    // Initialize or reset the game
    private void initializeGame() {
        gameOver = false;

        // Set initial snake head position in grid units
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        // Set initial food location
        food = new Tile(10, 10);
        if (random == null) {
            random = new Random(); // initialize random if first run
        }
        placeFood(); // place food randomly

        // Initial movement direction: moving right
        velocityX = 1;
        velocityY = 0;

        // Start game timer if not already started
        if (gameLoop == null) {
            // Timer triggers `actionPerformed` every 100ms
            gameLoop = new Timer(100, this); 
        }
        gameLoop.start(); // start or resume the game loop
    }

    // Called automatically to draw everything on the screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // clears previous frame
        draw(g); // draw current frame
    }

    // Method to draw grid, food, snake, and score
    public void draw(Graphics g) {
        // Draw grid lines for reference
        for(int i = 0; i < boardWidth / tileSize; i++) {
            // Vertical line: x = i*tileSize
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            // Horizontal line: y = i*tileSize
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize); 
        }

        // Draw food in red
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Draw snake head in green
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Draw each part of snake body
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Draw score (number of body tiles)
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.white);
        g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
    }

    // Places food at a random tile on the board
    public void placeFood() {
        // `boardWidth / tileSize` = number of tiles horizontally
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // Handles snake movement, eating, and collision detection
    public void move() {
        // Check if head is on same tile as food
        if (collision(snakeHead, food)) {
            // Add new segment at food's position
            snakeBody.add(new Tile(food.x, food.y));
            placeFood(); // relocate food
        }

        // Move snake body segments
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                // First body part takes place of old head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                // Each segment moves to where the previous segment was
                Tile prev = snakeBody.get(i - 1);
                snakePart.x = prev.x;
                snakePart.y = prev.y;
            }
        }

        // Move the snake head in the current direction
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Check for collision with self
        for (Tile bodyPart : snakeBody) {
            if (collision(snakeHead, bodyPart)) {
                gameOver = true;
            }
        }

        // Check collision with wall boundaries
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }
    }

    // Check if two tiles occupy the same position
    public boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    // Called by Timer every frame (100ms)
    @Override
    public void actionPerformed(ActionEvent e) {
        move();    // update game logic
        repaint(); // request repaint of screen
        if (gameOver) {
            gameLoop.stop(); // stop game loop
            showGameOverDialog(); // show dialog
        }
    }

    // Show game over UI overlay
    private void showGameOverDialog() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame parentFrame = (JFrame) window;
            gameOverDialog = new JDialog(parentFrame, "Game Over", true);
            gameOverDialog.setLayout(new BorderLayout());

            // Custom panel with gradient background
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(45, 45, 45), 
                        0, getHeight(), new Color(10, 10, 10));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("GAME OVER");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.RED);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel scoreLabel = new JLabel("Your Score: " + snakeBody.size());
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel restartLabel = new JLabel("Press SPACE to restart");
            restartLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            restartLabel.setForeground(Color.YELLOW);
            restartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(titleLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(scoreLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(restartLabel);

            gameOverDialog.add(panel, BorderLayout.CENTER);
            gameOverDialog.setSize(300, 200);
            gameOverDialog.setLocationRelativeTo(parentFrame);

            gameOverDialog.setModal(false); // allow key events
            gameOverDialog.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameOverDialog.dispose(); // close dialog
                        initializeGame(); // reset game
                    }
                }
            });

            gameOverDialog.setFocusable(true);
            gameOverDialog.requestFocus();
            gameOverDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            gameOverDialog.setVisible(true);
        }
    }

    // Keyboard controls
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            // Prevent reverse direction
            if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            if (gameOverDialog != null && gameOverDialog.isVisible()) {
                gameOverDialog.dispose();
            }
            initializeGame(); // restart game
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} 

    @Override
    public void keyReleased(KeyEvent e) {} 
}
