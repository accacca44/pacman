import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends JPanel implements ActionListener {

    //timer to activate the actionListener, work as an update() function
    private Timer timer;
    final int DELAY = 15;

    //the player character
    private Player player;

    private ArrayList<Point> points;
    private ArrayList<Wall> walls;
    private ArrayList<Ghost> ghosts;

    //boolean value for deciding if their is a gameover
    private boolean ingame;

    //map sizes are bound to tiles, so the map is 19*19tiles
    private static final int TILE_SIZE = 32;
    final static int mapWidth = TILE_SIZE*19;
    final static int mapHeight = TILE_SIZE*19;

    //soundfiles
    File mounch;        //eating sound
    File death;         //death sound
    Clip mounchClip;
    Clip deathClip;

    //txt file that stores all the previous scores
    File savedScores;
    private JButton saveScore;
    private JButton exitGameBtn;
    private JTextField nameInput;

    //matrix representation of the map
    final static private int[][] props = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,0,1},
        {1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,1},
        {1,1,1,1,0,1,1,1,0,0,0,1,1,1,0,1,1,1,1},
        {1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1},
        {1,1,1,1,0,1,0,1,1,0,1,1,0,1,0,1,1,1,1},
        {0,0,0,0,0,0,0,1,2,2,2,1,0,0,0,0,0,0,0},
        {1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1},
        {1,0,0,0,0,1,0,0,0,3,0,0,0,1,0,0,0,0,1},
        {1,1,1,1,0,1,1,1,0,0,0,1,1,1,0,1,1,1,1},
        {1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,1},
        {1,0,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    public Map() throws IOException {
        setLayout(null);
        initScoreSave();        //initiates the components for saving a score
        initOutputFile();       //loading in the score-containing file
        addListeners();
    }

    //INITS
    private void initOutputFile() throws IOException {
        savedScores = new File("scores.txt");
        if(!savedScores.exists()){
            savedScores.createNewFile();
        }
    }

    private void initScoreSave() {
        exitGameBtn = new JButton("EXIT GAME");
        exitGameBtn.setVisible(false);
        exitGameBtn.setBounds(7*19,25*19+50,340,30);
        add(exitGameBtn);

        saveScore = new JButton("SAVE SCORE");
        saveScore.setVisible(false);
        saveScore.setBounds(17*19,25*19,150,30);
        saveScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameInput.getText() + "\r\n";
                nameInput.setText("");

                String playerScore = currentScore() + "\r\n";
                saveScore.setBackground(Color.GREEN);
                nameInput.setText(playerName + playerScore);

                try {
                    FileWriter fw = new FileWriter(savedScores,true);
                    fw.append(playerName);
                    fw.append(playerScore);
                    fw.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        add(saveScore);


        nameInput = new JTextField("Enter your name here...");
        nameInput.setVisible(false);
        nameInput.setBounds(7*19,25*19,150,30);
        add(nameInput);
    }

    private void addListeners() {
        exitGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void initMap() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        requestFocusInWindow();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                player.KeyLenyomva(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });
        setFocusable(true);
        setBackground(Color.BLACK);
        ingame = true;

        setPreferredSize(new Dimension(mapWidth, mapHeight));

        player = new Player(9*32, 11*32);

        initProps();

        timer = new Timer(DELAY, this);
        timer.start();

        initSounds();
    }

    private void initProps(){
        points = new ArrayList<>();
        walls = new ArrayList<>();
        ghosts = new ArrayList<>();

        for(int row = 0; row < props.length ;row++){
            for(int col = 0; col < props.length; col++){
                if(props[row][col] == 0){
                    points.add(new Point(32*col,32*row));
                }
                if(props[row][col] == 1){
                    walls.add(new Wall(32*col,32*row));
                }
                if(props[row][col] == 2){
                    ghosts.add(new Ghost(32*col,32*row));
                }
            }
        }
    }

    private void initSounds() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        mounch = new File("src/sounds/pacman_chomp.wav");
        mounchClip = AudioSystem.getClip();
        mounchClip.open(AudioSystem.getAudioInputStream(mounch));

        death = new File("src/sounds/pacman_death.wav");
        deathClip = AudioSystem.getClip();
        deathClip.open(AudioSystem.getAudioInputStream(death));
    }

    public Ghost getGhost(int index){
        return ghosts.get(index);
    }

    //GRAPHICS
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ingame) {

            drawObjects(g);

        } else {
            if(!player.isVisible())
            {
                drawGameOver(g);
                playGameOverSound();
            }else{
                drawYouWon(g);
                //playYouWonSound(g);
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(),
                    this);
        }

        for (Point point : points) {
            if (point.isVisible()) {
                g.drawImage(point.getImage(), point.getX(), point.getY(), this);
            }
        }

        for(Wall wall : walls){
            if(wall.isVisible()){
                g.drawImage(wall.getImage(), wall.getX(), wall.getY(), this);
            }
        }

        for(Ghost ghost : ghosts){
            if(ghost.isVisible()){
                g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), this);
            }
        }

        int currScore = Point.maxPoints-points.size();
        Font big = new Font("Helvetica", Font.BOLD, 18);
        g.setFont(big);
        g.setColor(Color.BLACK);
        g.drawString("SCORE: " + currScore, 5, 15);
    }

    private void drawGameOver(Graphics g) {

        String msg = "Game Over!";
        Font big = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics fm = getFontMetrics(big);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(msg, (32*19 - fm.stringWidth(msg)) / 2, 32*19 / 2);

        g.setColor(Color.RED);
        g.drawString("Score: " + currentScore() ,(32*19 - fm.stringWidth(msg)) / 2, 32*19 / 2 + 32);

        setButtondsVisible();
    }

    private void drawYouWon(Graphics g){
        String msg = "You Won!";
        int currScore = 179-points.size();
        Font big = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics fm = getFontMetrics(big);
        Font small = new Font("Helvetica", Font.BOLD, 18);

        g.setColor(Color.YELLOW);
        g.setFont(big);
        g.drawString(msg, (32*19 - fm.stringWidth(msg)) / 2,
                32*19 / 2 - 50);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString("Score: " + currentScore(), (32*19 - fm.stringWidth(msg)) / 2,
                32*19 / 2);

        setButtondsVisible();
    }

    private void setButtondsVisible() {
        saveScore.setVisible(true);
        nameInput.setVisible(true);
        exitGameBtn.setVisible(true);
    }

    //UPDATES
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getSource().toString());

        inGame();

        updatePlayer();
        updatePoints();

        checkCollisions();
        updateSounds();

        repaint();
        //printFocus();
    }

    private void inGame() {

        if (!ingame) {
            timer.stop();
        }
    }

    private void updatePlayer() {

        if (player.isVisible()) {
            player.move();
        }
    }

    private void updatePoints()  {

        if (points.isEmpty()) {

            ingame = false;
            return;
        }

        for (int i = 0; i < points.size(); i++) {

            Point p = points.get(i);

            if (!p.isVisible()) {points.remove(i);i--;}
        }
    }

    private void updateSounds() {
        if(mounchClip == null)return;
        if(mounchClip.getFramePosition() == 0)return;
        if(!mounchClip.isRunning()){
            mounchClip.setFramePosition(0);
        }
    }

    public void checkCollisions() {

        Rectangle r3 = player.getOffsetBounds();
        Rectangle r1 = player.getOffsetBounds();
        for (Point p : points) {

            Rectangle r2 = p.getBounds();

            try{
                if (r1.intersects(r2)) {
                    p.setVisible(false);
                    mounchClip.start();
                }
            } catch (Exception e){
                System.out.println("Mouch sound not loaded");
            }
        }

        for (Wall wall : walls) {

            Rectangle r2 = wall.getBounds();

            if (r3.intersects(r2)) {
               player.stopMotion();
            }
        }

        for(Ghost ghost : ghosts){
            Rectangle r4 = ghost.getBounds();

            if(r3.intersects(r4)){
                //game over when colliding with ghosts
                player.setVisible(false);
                Ghost.setGameOver(true);
                ingame = false;
            }
        }


    }

    public static int[][] getProps(){
        return props;
    }

    public int currentScore(){
        return Point.maxPoints - points.size();
    }

    private void playGameOverSound() {
        mounchClip.stop();
        deathClip.setFramePosition(0);
        deathClip.start();
    }

}
