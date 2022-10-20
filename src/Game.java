import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Game extends JFrame {

    //Game frame contains 3 cards in a cardLayout>> MENU, MAP, LEADERBOARD
    private Map map;
    private JPanel cardHolder;
    private CardLayout cl;
    private final String MAPCARD = "MAPCARD";
    private final String MENUCARD = "MENUCARD";
    private final String LEADCARD = "LEADCARD";

    //Ghosts are controlled by individual threads
    Ghost [] ghosts;
    GhostController [] ghostController;

    public Game() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        initCards();
        initFrame();
    }

    private void initCards() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //by default the menu appears on the screen
        Menu menu = new Menu();
        menu.setinGame(this);
        LeaderBoard leaderBoard = new LeaderBoard(this);
        map = new Map();

        cardHolder = new JPanel(new CardLayout());
        cardHolder.add(menu);
        cardHolder.add(map);
        cardHolder.add(leaderBoard);

        this.add(cardHolder);
        cl = (CardLayout)(cardHolder.getLayout());
    }

    private void initFrame() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        setPreferredSize(new Dimension(19*32,20*32));
        setResizable(false);
        pack();

        setTitle("PacMan");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initGhosts() {
        ghosts = new Ghost[3];
        ghostController = new GhostController[3];
        for(int i=0;i< ghosts.length;i++){
            ghosts[i] = map.getGhost(i);
            ghostController[i] = new GhostController(ghosts[i]);
        }
    }

    public void startGhosts(){
        for(GhostController gc : ghostController){
            gc.start();
        }
    }

    public void startGame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //switching to map, where the game is played
        cl.next(cardHolder);
        map.initMap();
        initGhosts();
        startGhosts();
    }

    public void showLeaderBoard(){
        cl.next(cardHolder);
        cl.next(cardHolder);
    }

    public void backToMenu(){
        cl.first(cardHolder);
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        Game c = new Game();
        c.setVisible(true);
    }

}
