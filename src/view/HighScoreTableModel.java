/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;
import model.HighScore;

/**
 *
 * @author Balázs
 */
public class HighScoreTableModel extends AbstractTableModel {
    private final ArrayList<HighScore> highScores;
    private final String[] colNames;

    public HighScoreTableModel(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
        Collections.sort(this.highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore o1, HighScore o2) {
                return o2.score - o1.score;
            }
            
        });
        this.colNames = new String[] {"Név", "Győzelmek száma"};
    }

    
    @Override
    public int getRowCount() {
        if(highScores.size() > 10) {
            return 10;
        }
        return highScores.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HighScore h = highScores.get(rowIndex);
        if(columnIndex == 0) return h.name;
        else if(columnIndex == 1) return h.score;
        return (h.score == 0) ? "Nincs még pontja." : h.score;
    }
    
    @Override
    public String getColumnName(int i) { return colNames[i]; }
}
