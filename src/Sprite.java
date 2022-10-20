import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;

public class Sprite {           //template class that can be shared with PacMan, Ghost, Points, Apples
    protected int x;            //position coordinates
    protected int y;

    protected int width;        //obj size
    protected int heigth;
    protected boolean visible;
    protected Image image;

    public Sprite(int x, int y){
        this.x = x;
        this.y = y;
        visible = true;
    }

    protected void getImageDimensions(){
        width = image.getWidth(null);
        heigth = image.getHeight(null);
    }

    protected void loadImage(String imageName){
        ImageIcon ii = new ImageIcon(imageName);
        image = ii.getImage();
    }

    public Image getImage(){
        return image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,width,heigth);
    }
}
