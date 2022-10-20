public class Point extends Sprite{

    final static int maxPoints = 179;

    public Point(int x, int y) {
        super(x, y);
        initPoint();
    }

    private void initPoint(){
        loadImage("src/images/point.png");
        getImageDimensions();
    }
}
