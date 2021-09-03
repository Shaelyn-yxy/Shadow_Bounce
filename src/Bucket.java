/**
 * Bucket Class
 *
 * Bucket will move back and forth across the bottom of the screen
 * If a ball intersects bucket, get an additional shot(achieve in Board)
 */

import bagel.*;
import bagel.util.*;

public class Bucket extends GameObject{
    private Vector2 velocity;
    private static final double SPEED = 4.0;

    public Bucket(Point point) {
        super(point, "res/bucket.png");
        velocity = Vector2.left.mul(SPEED);
    }

    @Override
    public void update() {
        super.move(velocity);
        // while bucket reaches the left or right side, reversing its direction
        if (super.getRect().left() < 0 || super.getRect().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }
        super.draw();
    }
}
