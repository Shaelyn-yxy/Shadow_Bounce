/**
 * GameObject Class
 * The superclass of the object in this game
 *
 * Based on the sample code written by Eleanor McMurtry
 * Edited by XINYI YUAN
 */

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

public abstract class GameObject {
    private Image image;
    private Rectangle rect;
    private String shape;

    public GameObject(Point point, String imageSrc) {
        image = new Image(imageSrc);
        rect = image.getBoundingBoxAt(point);
        setShape(imageSrc);

    }

    /**
     * Calculate distance between two GameObjects
     */
    public double distanceOf(Point point){
        return point.asVector().sub(getRect().centre().asVector()).length();

    }

    public boolean intersects(GameObject other) {
        return rect.intersects(other.rect);
    }

    public void move(Vector2 dx) {
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    public void draw() {
        image.draw(rect.centre().x, rect.centre().y);
    }

    /**
     * Assign the shape by the key words in its imageSrc
     */
    private void setShape(String imageSrc) {
        if(imageSrc.contains("horizontal")){
            this.shape = "horizontal-";
        }else if(imageSrc.contains("vertical")){
            this.shape = "vertical-";
        } else{
            this.shape = "";
        }
    }


    public String getShape(){
        return this.shape;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Rectangle getRect() {
        return rect;
    }

    public abstract void update();
}
