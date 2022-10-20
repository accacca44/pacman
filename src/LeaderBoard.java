import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

//panel that displays the top 5 runs
public class LeaderBoard extends JPanel {
    private JLabel [] positions;
    private final Game game;
    private File scores;
    private ArrayList<Record> records;

    public LeaderBoard(Game game) throws IOException {
        this.game = game;

        setLayout(new GridLayout(6,1));
        intiPositions();                                //filling up labels with the top scores
        setBackButton();                                //backButton taking to Menu
        printScore();
    }

    private void intiPositions() throws IOException {
        initScores();                                   //init the txt file, that contains all the saved data
        loadScore();                                    //loading all the scores into a list of Records
        positions = new JLabel[5];
        for(int i = 0; i < 5; i++) {
            positions[i] = new JLabel("No Score");
            positions[i].setHorizontalAlignment((int) CENTER_ALIGNMENT);
            add(positions[i]);
        }
        Collections.sort(records,new RecordComparator());       //sorting the scores to get the top 5
        fillLabels();                                           //filling up the labels with the top 5 scores
    }

    private void initScores() throws IOException {
        scores = new File("scores.txt");
        if(!scores.exists()){
            scores.createNewFile();
        }
    }

    private void loadScore() throws FileNotFoundException {
        records = new ArrayList<>();

        Scanner scan = new Scanner(scores);
        while(scan.hasNextLine()){
            records.add(new Record(scan.nextLine(), Integer.parseInt(scan.nextLine())));
        }
    }

    private void fillLabels() {
        for(int i = 0; i < 5; i++){
            if(records.size() > i) {
                positions[i].setText(records.get(i).toString());
            }
        }
    }

    private void printScore() {
        for(int i = 0; i < records.size(); i++){
            System.out.println(records.get(i).getPlayerName() + " " + records.get(i).getPlayerScore());
        }
    }

    private void setBackButton() {
        JButton backButton = new JButton("BACK");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.backToMenu();
            }
        });
        add(backButton);

    }


}
