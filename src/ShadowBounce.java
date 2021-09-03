/**
 * Shadow Bounce
 * Project 2B, Semester 2, 2019
 * Written by XINYI YUAN 906737
 */

import bagel.*;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Random;

public class ShadowBounce extends AbstractGame {
    private Board board ;
    private Bucket bucket;
    private ArrayList<Ball> balls;
    private FireBall fireBall;
    private static final Point BALL_POSITION = new Point(512, 32);
    private static final Point BUCKET_POSITION = new Point(512, 744);
    private static final double BALL_SPEED = 10;
    private static final double DESTROY_RANGE = 70;
    private static final String[] CSV_FILES = new String[]{"res/0.csv","res/1.csv","res/2.csv","res/3.csv","res/4.csv"};
    // record the rest of shots
    private int numShot = 20;
    private int fileIndex = 0;
    private boolean turnStart = false;


    public ShadowBounce() {
        board = new Board(CSV_FILES[fileIndex]);
        bucket = new Bucket(BUCKET_POSITION);
        // all balls(include fire ball) will put in an arrayList
        balls = new ArrayList<Ball>();
    }

    @Override
    public void update(Input input) {

        // If we don't have a ball and the mouse button was clicked, create a new all,turn start
        if (input.wasPressed(MouseButtons.LEFT) && balls.size() == 0) {
            balls.add(new Ball(BALL_POSITION, input.directionToMouse(BALL_POSITION)));
            numShot --;
            turnStart = true;
        }
        // when balls are on the screen
        if (balls.size() != 0){
            for (Ball ball : new ArrayList<Ball>(balls)) {
                ball.update();
                // Delete the ball when it leaves the screen
                if (ball.outOfScreen()) {
                    balls.remove(ball);
                }
                //if ball intersects to bucket, add one shot
                if (ball.intersects(bucket)){
                    numShot += 1;
                    balls.remove(ball);
                }
            }
        }

        // all collision and bounce will happen in this loop
        // except greyPegs, all Pegs and powerUp will be removed if they intersects to a ball
        // ball will bounce if it strikes pegs and powerUp
        for (Ball ball: new ArrayList<Ball>(balls)) {
            for (Peg b_peg : new ArrayList<Peg>(board.getBluePegs())) {
                // determine whether they had bounced
                if (bounce(b_peg, ball) == 1) {
                    // if it is a fireball, destroy the pegs in DESTROY_RANGE
                    if(ball instanceof FireBall){
                        destroyNeighbour(b_peg);
                    }
                    board.getBluePegs().remove(b_peg);
                }
            }
            for (Peg r_peg : new ArrayList<Peg>(board.getRedPegs())) {
                if (bounce(r_peg, ball) == 1) {
                    if(ball instanceof FireBall){
                        destroyNeighbour(r_peg);
                    }
                    board.getRedPegs().remove(r_peg);
                }
            }
            // greyPeg can not be destroyed
            for (Peg g_peg : new ArrayList<Peg>(board.getGreyPegs())){
                if (bounce(g_peg, ball) == 1){
                    if(ball instanceof FireBall){
                        destroyNeighbour(g_peg);
                    }
                }
            }
            if(board.getGreenPeg() != null){
                if(bounce(board.getGreenPeg(), ball) == 1){
                    if(ball instanceof FireBall){
                        destroyNeighbour(board.getGreenPeg());
                    }
                    else{
                        createBalls(ball, board.getGreenPeg());
                        board.setGreenPeg(null) ;
                    }
                }
            }
            // generate a fire ball if a ball intersects the powerUp
            if(board.getPowerUp()!= null){
                if(bounce(board.getPowerUp(), ball) == 1){
                    board.setPowerUp(null);
                    Vector2 direction = ball.getVelocity().div(BALL_SPEED);
                    fireBall = new FireBall(ball.getRect().centre(), direction);
                    balls.remove(ball);
                    balls.add(fireBall);
                }
            }
        }
        System.out.println(numShot);

        //if current turn finishes, initiate a new turn
        if (turnStart && balls.size() == 0) {
            turnStart = false;
            board.initTurn();
        }
        // if all red balls are cleared, move to next board
        if (board.getRedPegs().size() == 0) {
            fileIndex++;
            if (fileIndex < 5) {
                balls.clear();
                board = new Board(CSV_FILES[fileIndex]);
            }
            // if no more board, WIN!!!!!
            else {
                Window.close();
                System.out.println("win");
            }
        }
        bucket.update();
        board.update();
        if(board.getPowerUp()!=null){
            board.getPowerUp().update();
        }

         //if no more shots, lose the game!!!!
        if (numShot == 0 && !turnStart) {
            Window.close();
            System.out.println("LOSE");
        }
    }

    public static void main (String[]args){
        new ShadowBounce().run();
    }

    /**
     * Determine which which side of the obj the ball intersected and the ball will bounce according to the side.
     *
     * @return whether they had bounced
     * 0 means they did not intersect, 1 means they had intersected and bounced
     */
    public int bounce (GameObject obj, Ball ball){
        Side side = obj.getRect().intersectedAt(ball.getRect().centre(), ball.getVelocity());
        int flag = 0;
        if (side.equals(Side.LEFT) || side.equals(Side.RIGHT)) {
            ball.setVelocity(new Vector2(-ball.getVelocity().x, ball.getVelocity().y));
            flag = 1;
        }
        if (side.equals(Side.TOP) || side.equals(Side.BOTTOM)) {
            ball.setVelocity(new Vector2(ball.getVelocity().x, -ball.getVelocity().y));
            flag = 1;
        }
        return flag;
    }

    /**
     * Create two balls with the striking ball's type at peg's position
     */
    public void createBalls (Ball ball, Peg peg){
        Vector2 left_direction = peg.getRect().topLeft().asVector().sub(peg.getRect().centre().asVector());
        Vector2 right_direction = peg.getRect().topRight().asVector().sub(peg.getRect().centre().asVector());
        if (ball instanceof FireBall) {
            balls.add(new FireBall(peg.getRect().centre(), left_direction.normalised()));
            balls.add(new FireBall(peg.getRect().centre(), right_direction.normalised()));
        }else{
            balls.add(new Ball(peg.getRect().centre(), left_direction.normalised()));
            balls.add(new Ball(peg.getRect().centre(), right_direction.normalised()));

        }
    }

    /**
     * Remove all pegs(except greyPegs) within DESTROY_RANGE of struck peg's centre
     */
    public void destroyNeighbour(Peg peg){
        for (Peg p : new ArrayList<Peg>(board.getBluePegs())) {
            if (peg.distanceOf(p.getRect().centre()) < DESTROY_RANGE) {
                board.getBluePegs().remove(p);
            }
        }
        for (Peg p : new ArrayList<Peg>(board.getRedPegs())) {
            if (peg.distanceOf(p.getRect().centre()) < DESTROY_RANGE) {
                board.getRedPegs().remove(p);
            }
        }
        // if there is a greenPeg within DESTROY_RANGE, it will create two fireBalls
        if(board.getGreenPeg()!= null) {
            if (peg.distanceOf(board.getGreenPeg().getRect().centre()) < DESTROY_RANGE) {
                createBalls(fireBall, board.getGreenPeg());
                board.setGreenPeg(null);

            }
        }
    }
}


