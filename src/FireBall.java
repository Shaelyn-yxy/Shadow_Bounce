/**
 * Fire ball class
 */
import bagel.*;
import bagel.util.*;
public class FireBall extends Ball {
    private static final String FIRE_BALL_IMAGE = "res/fireball.png";

    public FireBall(Point point, Vector2 direction) {
        super(point, direction);
        super.setImage(new Image(FIRE_BALL_IMAGE));

    }



}
