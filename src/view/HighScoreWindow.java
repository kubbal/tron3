/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import model.HighScore;

/**
 *
 * @author kuble
 */
public class HighScoreWindow extends JDialog {
    private final JTable table;
    
    public HighScoreWindow(ArrayList<HighScore> highScores, JFrame parent) {
        super(parent, true);

        table = new JTable(new HighScoreTableModel(highScores));
        table.setFillsViewportHeight(true);
        
        add(new JScrollPane(table));
        setSize(400, 250);
        setTitle("Dicsőség tábla: A 10 legjobb játékos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
