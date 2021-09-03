/**
 * Ball class
 * Use the sample code which Written by Eleanor McMurtry
 */

import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

public class Ball extends GameObject {
    private Vector2 velocity;
    private static final double GRAVITY = 0.15;
    private static final double BALL_SPEED = 10;

    public Ball(Point point, Vector2 direction) {
        super(point, "res/ball.png");
        this.velocity = direction.mul(BALL_SPEED);
    }


    public boolean outOfScreen() {
        return super.getRect().top() > Window.getHeight();
    }

    @Override
    public void update() {
        velocity = this.velocity.add(Vector2.down.mul(GRAVITY));
        super.move(velocity);
        // ball will bounce if it reached the left or right side.
        if (super.getRect().left() < 0 || super.getRect().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }

        super.draw();
    }

    /**
     * getter and setter
     */

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
