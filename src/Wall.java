public class Wall extends Sprite{
    public Wall(int x, int y) {
        super(x, y);
        initWall();
    }

    private void initWall(){
        loadImage("src/images/leftRightCol.png");
        getImageDimensions();
    }
}
