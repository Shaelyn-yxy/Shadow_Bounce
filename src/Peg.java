/**
 * Peg class
 *
 * Use the sample code which Written by Eleanor McMurtry
 */


import bagel.util.Point;

public class Peg extends GameObject {
    public Peg(Point point, String imageSrc) {
        super(point, imageSrc);
    }

    @Override
    public void update() {
        super.draw();
    }
}
