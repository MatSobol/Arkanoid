import java.awt.*;

public class Ball {
    private Rectangle ball;
    private int ballXDir;
    private int ballYDir;
    private int time;

    public Ball(Rectangle ball, int ballXDir, int ballYDir) {
        this.ball = ball;
        this.ballXDir = ballXDir;
        this.ballYDir = ballYDir;
        this.time = 10;
    }

    public Rectangle getBall() {
        return ball;
    }

    public int getTime() {
        return time;
    }

    public int getBallXDir() {
        return ballXDir;
    }

    public int getBallYDir() {
        return ballYDir;
    }

    public void ballSetter(Rectangle ball, int ballXDir, int ballYDir, int time) {
        this.ball = ball;
        this.ballXDir = ballXDir;
        this.ballYDir = ballYDir;
        this.time = time;
    }

}
