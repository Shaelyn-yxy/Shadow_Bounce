/**
 * PowerUp Class
 *
 * 1/10 chance to create a powerUp and move randomly
 * If a ball intersects powerUp, the ball will be activated to a fire ball(in shadowBounce)
 */

import bagel.*;
import bagel.util.*;
public class PowerUp extends GameObject {
    private Vector2 velocity;
    private Point destination;

    private static final double SPEED = 3;
    private static final double CLOSE_RANGE = 5;

    public PowerUp(Point point) {
        super(point, "res/powerup.png");
        randDestination();
    }

    /**
     * update() includes bucket's moving and drawing.
     * if powerUp in CLOSE_RANGE, select another random destination and move to it
     */
    @Override
    public void update() {
        while (distanceOf(destination) < CLOSE_RANGE) {
            randDestination();
        }
        move(velocity);
        super.draw();

    }

    /**
     * Choose a random destination on the screen, and modify the velocity
     */
    private void randDestination() {
        destination = new Point(Math.random() * Window.getWidth(), Math.random() * Window.getHeight());
        Vector2 direction = destination.asVector().sub(getRect().centre().asVector());
        velocity = direction.normalised().mul(SPEED);
    }
}

