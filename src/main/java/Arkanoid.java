import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Arkanoid extends JPanel implements KeyListener {

    private JFrame frame;
    private ArrayList<Rectangle> bricks;
    private Rectangle paddle;
    private Rectangle specialBrick;
    private ArrayList<Ball> balls;
    private int ballXDir = 1;
    private int ballYDir = -2;
    private boolean condition = false;

    public Arkanoid() {
        frame = new JFrame("Arkanoid");
        frame.setResizable(false);
        frame.setSize(420, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.addKeyListener(this);
        frame.add(this);
        frame.setVisible(true);

        bricks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle brick = new Rectangle(i * 60 + 30, j * 20 + 50, 50, 10);
                bricks.add(brick);
            }
        }
        specialBrick = new Rectangle(330, 150, 50, 10);
        paddle = new Rectangle(150, 550, 100, 10);
        balls = new ArrayList<>();
        Rectangle ball = new Rectangle(290, 340, 10, 10);
        Ball ball1 = new Ball(ball, 1, -2);
        balls.add(ball1);

        Thread gameThread = new Thread(new Runnable() {
            int time = 10;
            int counter = 0;

            int j = 0;
            int b = 0;

            public void run() {
                while (true) {
                    ArrayList<Ball> ballsContainer = new ArrayList<>();
                    if (b == 3) {
                        for (Ball ballt : balls) {
                            if (ballt.getTime() == 8) {
                                ballsContainer.add(ballt);
                            }
                        }
                        b = 0;
                    }
                    if (j == 5) {
                        for (Ball ballt : balls) {
                            if (ballt.getTime() == 10) {
                                ballsContainer.add(ballt);
                            }
                        }
                        j = 0;
                    }
                    for (Ball ballGet : ballsContainer) {
                        time = ballGet.getTime();
                        Rectangle ball = ballGet.getBall();
                        ballXDir = ballGet.getBallXDir();
                        ballYDir = ballGet.getBallYDir();
                        ball.x += ballXDir;
                        ball.y += ballYDir;
                        if (ball.x < 0 || ball.x > getWidth() - ball.width) {
                            ballXDir *= -1;
                        }
                        if (ball.y < 0) {
                            ballYDir *= -1;
                        }
                        if (ball.intersects(paddle)) {
                            int distance = ball.x + ball.width / 2 - paddle.x - paddle.width / 2;

                            int max_distance = (paddle.width / 2) + (ball.width / 2);

                            double percentage = (double) distance / (double) max_distance;

                            if (percentage >= -0.4 && percentage <= 0.4) {
                                ballYDir = -2;
                                time = 10;
                            } else {
                                ballYDir = -1;
                                time = 8;
                            }
                        }

                        if (ball.y > getHeight()) {
                            counter = 0;
                            time = 10;
                            restartWindow("Game Over");
                            break;
                        }

                        for (int i = bricks.size() - 1; i >= 0; i--) {
                            Rectangle brick = bricks.get(i);
                            if (ball.intersects(brick)) {
                                bricks.remove(i);
                                ballYDir *= -1;
                                counter++;
                            }
                            if (counter == 31) {
                                counter = 0;
                                time = 10;
                                restartWindow("Game Won");
                                break;
                            }
                        }
                        if (specialBrick != null && ball.intersects(specialBrick)) {
                            specialBrick = null;
                            ballYDir *= -1;
                            counter++;
                            Rectangle ball1 = new Rectangle(290, 340, 10, 10);
                            Ball nBall = new Ball(ball1, 1, -2);
                            balls.add(nBall);
                            ballGet.ballSetter(ball, ballXDir, ballYDir, time);
                            break;
                        }
                        ballGet.ballSetter(ball, ballXDir, ballYDir, time);
                    }
                    while (condition) {
                    }
                    repaint();
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b++;
                    j++;
                }
            }
        });
        gameThread.start();
    }

    public static void main(String[] args) {
        new Arkanoid();
    }

    public void restartWindow(String text) {
        JLabel restartButton = new JLabel("Restart", SwingConstants.CENTER);
        condition = true;
        restartButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                condition = false;
                resetGame();
            }
        });
        restartButton.setFont(new Font("Arial", Font.BOLD, 24));
        restartButton.setForeground(Color.RED);

        JLabel gameOverLabel = new JLabel(text, SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameOverLabel.setForeground(Color.WHITE);
        JLabel rectangle = new JLabel();
        JPanel gameOverPanel = new JPanel(new GridLayout(5, 1));
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.add(rectangle);
        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(restartButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(gameOverPanel);

        frame.revalidate();
        frame.repaint();
    }

    public void resetGame() {
        frame.getContentPane().removeAll();
        frame.requestFocusInWindow();
        frame.add(this);
        bricks.clear();
        balls.clear();
        specialBrick = null;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle brick = new Rectangle(i * 60 + 30, j * 20 + 50, 50, 10);
                bricks.add(brick);
            }
        }

        paddle.x = 150;
        paddle.y = 550;

        specialBrick = new Rectangle(330, 150, 50, 10);
        Rectangle ball = new Rectangle(290, 340, 10, 10);
        Ball ball1 = new Ball(ball, 1, -2);
        balls.add(ball1);

        frame.revalidate();
        frame.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, brick.width, brick.height);
        }
        if (specialBrick != null) {
            g.setColor(Color.BLUE);
            g.fillRect(specialBrick.x, specialBrick.y, specialBrick.width, specialBrick.height);
        }
        g.setColor(Color.RED);
        g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);

        g.setColor(Color.WHITE);

        for (Ball ballGet : balls) {
            Rectangle ball = ballGet.getBall();
            g.fillOval(ball.x, ball.y, ball.width, ball.height);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.x -= 10;
            if (paddle.x < 0) {
                paddle.x = 0;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.x += 10;
            if (paddle.x > getWidth() - paddle.width) {
                paddle.x = getWidth() - paddle.width;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}