public class GhostController extends Thread{
    private Ghost ghost;

    public GhostController(Ghost ghost){
        this.ghost = ghost;
    }

    @Override
    public void run(){
        while(!Ghost.isGameOver()){
            try {
                ghost.randomMovement();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
