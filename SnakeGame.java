import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    boolean gameOver = false;
    // Dialog for game over
    JDialog gameOverDialog;
    
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        initializeGame();
    }
    
    // Method to initialize or reset the game
    private void initializeGame() {
        // Reset game over state
        gameOver = false;
        
        // Initialize snake
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        // Initialize food
        food = new Tile(10, 10);
        if (random == null) {
            random = new Random();
        }
        placeFood();

        // Set initial direction
        velocityX = 1;
        velocityY = 0;
        
        // Start or restart game timer
        if (gameLoop == null) {
            gameLoop = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames
        }
        gameLoop.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        //Food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.white);
        g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            showGameOverDialog();
        }
    }
    
      // Method to show game over dialog
      private void showGameOverDialog() {
        // Get the parent window (JFrame)
        Window window = SwingUtilities.getWindowAncestor(this);
        
        if (window instanceof JFrame) {
            JFrame parentFrame = (JFrame) window;
            
            // Create game over dialog
            gameOverDialog = new JDialog(parentFrame, "Game Over", true);
            gameOverDialog.setLayout(new BorderLayout());
            
            // Create panel with a nice gradient background
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Create gradient background
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(45, 45, 45), 
                        0, getHeight(), new Color(10, 10, 10));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Game over title
            JLabel titleLabel = new JLabel("GAME OVER");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.RED);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Score label
            JLabel scoreLabel = new JLabel("Your Score: " + snakeBody.size());
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Restart instruction
            JLabel restartLabel = new JLabel("Press SPACE to restart");
            restartLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            restartLabel.setForeground(Color.YELLOW);
            restartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add components to panel with spacing
            panel.add(titleLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(scoreLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            panel.add(restartLabel);
            
            gameOverDialog.add(panel, BorderLayout.CENTER);
            gameOverDialog.setSize(300, 200);
            gameOverDialog.setLocationRelativeTo(parentFrame);
            
            // Make dialog non-modal so keyboard events still work
            gameOverDialog.setModal(false);
            
            // Add key listener to the dialog
            gameOverDialog.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameOverDialog.dispose();
                        initializeGame();
                    }
                }
            });
            
            // Make sure the dialog can receive key events
            gameOverDialog.setFocusable(true);
            gameOverDialog.requestFocus();
            
            gameOverDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            gameOverDialog.setVisible(true);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // Game controls
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }
        } 
        // Restart game with space bar when game is over
        // When the space key is pressed AND the game is over...
        else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            // Check if there's a game over dialog window AND if it's currently visible
            if (gameOverDialog != null && gameOverDialog.isVisible()) {
                // If so, close (remove) the game over dialog window
                gameOverDialog.dispose();
            }
            
            // Reset and restart the game
            initializeGame();
        }
    }

    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}