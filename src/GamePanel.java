import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH *(0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;


    GamePanel(){
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }
    public void newBall(){
        //for creating ball
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), random.nextInt((GAME_HEIGHT)-(BALL_DIAMETER)), BALL_DIAMETER, BALL_DIAMETER);

    }
    public void newPaddles(){
        //for creating panels
        paddle1 = new Paddle(0, (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT/2, 1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH, (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT/2, 2);

    }
    public void paint(Graphics g){
        //for painting stuff on screen
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);

    }
    public void draw(Graphics g){
        //for drawing stuff on screen
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

    }
    public void move(){
        //for moving items
        paddle1.move();
        paddle2.move();
        ball.move();

    }
    public void checkCollision(){
        //for checking collision of objects

        //this bounces ball off panels
        if(ball.intersects(paddle1)){
            ball.xVelocity = -ball.xVelocity;
            //for increasing difficulty
            ball.xVelocity++;
            if(ball.yVelocity>0){
                ball.yVelocity++;
            }else{
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){
            ball.xVelocity = -ball.xVelocity;
            //for increasing difficulty
            ball.xVelocity++;
            if(ball.yVelocity>0){
                ball.yVelocity++;
            }else{
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        //bounce ball off top & bottom edge of window
        if(ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }

        // & for stopping panels at edges of window
        if(paddle1.y <= 0){
            paddle1.y = 0;
        }
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        }
        if(paddle2.y <= 0){
            paddle2.y = 0;
        }
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
        }

        //give a player 1 point & create new ball & paddle
        if(ball.x <= 0){
            score.player2++;
//            newPaddles();
            newBall();
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER){
            score.player1++;
            newPaddles();
            newBall();
        }
    }
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            if(delta >= 1){
                move();
                checkCollision();
                repaint();
                delta--;
//                System.out.println("TEST");
            }
        }

    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);

        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);

        }
    }
}
