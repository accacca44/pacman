import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite{

    private int dx;         //directional speeds
    private int dy;
    private final int SPEED = 4;

    public Player(int x, int y) {
        super(x, y);
        initPlayer();
    }

    private void initPlayer() {
        loadImage("src/images/pacman.png");
        getImageDimensions();
    }

    public int getSPEED() {
        return SPEED;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void move(){
        x += dx * SPEED;
        y += dy * SPEED;
        checkTunnels();
    }

    public void checkTunnels(){
        if(y >=  32*8 && y <= 32*10){
            if(x < 0)x=32*32;
            else if(x > 32*32)x=0*32;
        }
    }

    public Rectangle getOffsetBounds() {
        return new Rectangle(x + dx, y + dy, width, heigth);
    }

    public void stopMotion(){
        dx = dy = 0;
    }

    public void KeyLenyomva(KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if(dx == 1){
                stopMotion();
                return;
            }
            dx = -1;
            dy = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if(dx == -1){
                stopMotion();
                return;
            }
            dx = 1;
            dy = 0;
        }

        if (key == KeyEvent.VK_UP) {
            if(dy == 1){
                stopMotion();
                return;
            }
            dy = -1;
            dx = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            if(dy == -1){
                stopMotion();
                return;
            }
            dy = 1;
            dx = 0;
        }
    }

    public void keyReleased(KeyEvent e) {
        ;
    }
}
