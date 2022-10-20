import java.util.ArrayList;
import java.util.Random;

public class Ghost extends Sprite{
    public Ghost(int x, int y) {
        super(x, y);
        initGhost();
    }

    //ghosts pixel-position converted to tile-position
    private int tileX;
    private int tileY;

    //previous tile positions
    private int prevX;
    private int prevY;

    //map represented by a matrix for calculating possible paths
    private final int[][] map = Map.getProps();
    private final int TILE_SIZE = 32;

    //a list of possible directions as vectors (0,1) or (-1,1)
    ArrayList<Integer> dirsX;
    ArrayList<Integer> dirsY;

    //boolean var which stops the threads after the game is over
    static boolean gameOver;

    private void initGhost() {
        prevX = getX()/TILE_SIZE;
        prevY = getY()/TILE_SIZE;

        loadImage("src/images/ghost_t.png");
        getImageDimensions();
    }

    //this function selects a possible direction and moves 1 tile
    public void randomMovement() throws InterruptedException {
        //calculating the possible directions
        int index = setDirection();
        makeStep(index);
    }

    private int setDirection() {
        setTiles();

        dirsX = new ArrayList<>();
        dirsY = new ArrayList<>();
        int[] dirX = {0,0,1,-1};    // DOWN, UP, RIGHT, LEFT
        int[] dirY = {1,-1,0,0};

        for(int i = 0; i < 4; i++){
            int nextX = tileX + dirX[i];
            int nextY = tileY +  dirY[i];
            //checking if moving to next tile is safe
            if(isInside(nextX, nextY) && isFree(nextX, nextY)){
                dirsX.add(nextX);
                dirsY.add(nextY);
            }
        }

        //if there is multiple directional choices, don't go backwards
        if(dirsX.size() > 1){
            for(int ii = 0; ii < dirsY.size(); ii++){
                if(dirsX.get(ii) == prevX && dirsY.get(ii) == prevY){
                    dirsX.remove(ii);
                    dirsY.remove(ii);
                    ii--;
                }
            }
        }

        //selecting a random direction
        Random rand = new Random();
        int ans = rand.nextInt()%dirsX.size();
        if(ans < 0)ans = -ans;
        return ans;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        Ghost.gameOver = gameOver;
    }

    //converts form pixel to a 32x32 tile
    private void setTiles(){
        tileX = getX()/TILE_SIZE;
        tileY = getY()/TILE_SIZE;
    }

    //returns true if there are no walls
    private boolean isFree(int x, int y) {
        return (map[y][x] != 1);
    }

    //returns false if out-of map
    private boolean isInside(int x, int y) {
        if(x < 0 || y < 0)return false;
        if(x > 18 || y > 18)return false;
        return true;
    }

    private void makeStep(int index) throws InterruptedException {
        setTiles();
        prevX = tileX;
        prevY = tileY;

        int targetX = 32*dirsX.get(index);
        int targetY = 32*dirsY.get(index);

        int dx = dirsX.get(index) - tileX;
        int dy = dirsY.get(index) - tileY;

        while(getX() != targetX || getY() != targetY){
            setX(getX() + dx);
            setY(getY() + dy);
            Thread.sleep(5);
        }

    }
}
