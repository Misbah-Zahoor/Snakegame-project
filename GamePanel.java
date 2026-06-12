import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int CELL_SIZE = 28;
    static final int GRID_SIZE = 20;
    static final int SCREEN_SIZE = CELL_SIZE * GRID_SIZE;

    private Queue<Character> directionQueue;
    private Snake snake;
    private Point food;
    private Random random;
    private Timer gameTimer;

    private char currentDir = 'R';
    private int score = 0;
    private int delay = 300;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(new Color(18, 18, 18));
        setFocusable(true);
        addKeyListener(this);

        snake = new Snake();
        directionQueue = new LinkedList<>();
        random = new Random();

        spawnFood();

        gameTimer = new Timer(delay, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processDirection();
        moveSnake();
        repaint();
    }

    private void processDirection() {
        if (!directionQueue.isEmpty()) {
            char next = directionQueue.poll();
            if (next == 'L' && currentDir == 'R') return;
            if (next == 'R' && currentDir == 'L') return;
            if (next == 'U' && currentDir == 'D') return;
            if (next == 'D' && currentDir == 'U') return;
            currentDir = next;
        }
    }

    private void moveSnake() {
        Point head = snake.getHead();
        Point newHead;

        switch (currentDir) {
            case 'R': newHead = new Point(head.x + 1, head.y); break;
            case 'L': newHead = new Point(head.x - 1, head.y); break;
            case 'U': newHead = new Point(head.x, head.y - 1); break;
            default:  newHead = new Point(head.x, head.y + 1); break;
        }

        if (newHead.equals(food)) {
            snake.grow(newHead);
            score += 10;
            spawnFood();
        } else {
            snake.move(newHead);
        }
    }

    private void spawnFood() {
        do {
            food = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (snake.collidesWith(food));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawFood(g);
        drawSnake(g);
        drawScore(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSnake(Graphics g) {
        LinkedList<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            if (i == 0) {
                g.setColor(new Color(100, 220, 100));
            } else {
                g.setColor(new Color(50, 160, 50));
            }
            g.fillRoundRect(
                    p.x * CELL_SIZE + 2,
                    p.y * CELL_SIZE + 2,
                    CELL_SIZE - 4,
                    CELL_SIZE - 4,
                    8, 8
            );
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(new Color(220, 60, 60));
        g.fillOval(
                food.x * CELL_SIZE + 4,
                food.y * CELL_SIZE + 4,
                CELL_SIZE - 8,
                CELL_SIZE - 8
        );
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 8, 18);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  directionQueue.offer('L'); break;
            case KeyEvent.VK_RIGHT: directionQueue.offer('R'); break;
            case KeyEvent.VK_UP:    directionQueue.offer('U'); break;
            case KeyEvent.VK_DOWN:  directionQueue.offer('D'); break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}