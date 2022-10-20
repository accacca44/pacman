import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Menu extends JPanel {
    JButton startButton;
    JButton exitButton;
    JButton leaderBoard;
    Game game;

    public Menu(){
        setBackground(Color.GRAY);
        GridLayout gl = new GridLayout(3,1);
        gl.setVgap(20);
        setLayout(gl);

        startButton = new JButton("START GAME!");
        exitButton = new JButton("EXIT GAME!");
        leaderBoard = new JButton("LEADERBOARD");

        startButton.setBackground(Color.LIGHT_GRAY);
        exitButton.setBackground(Color.LIGHT_GRAY);
        leaderBoard.setBackground(Color.LIGHT_GRAY);

        add(startButton);
        add(leaderBoard);
        add(exitButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.startGame();
                } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        leaderBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.showLeaderBoard();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void setinGame(Game game){
        this.game = game;
    }
}
