/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.HighScore;

/**
 *
 * @author Balázs
 */
public class Database {
    private final String tableName = "highscores";
    private final Connection conn;
    private ArrayList<HighScore> highScores;

    public Database() {
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
        } catch(Exception ex) { System.out.println("Nem sikerült csatlakozni az adatbázishoz."); }
        this.conn = c;
        highScores = new ArrayList<>();
        loadHighScores();
    }
    
    private void loadHighScores() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while(rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                highScores.add(new HighScore(name, score));
            }
        } catch(Exception ex) { System.out.println("Hiba az adatbázis betöltésekor." + ex); }
    }
    
    public int storeToDatabase(String name, int score) {
        try(Statement stmt = conn.createStatement()) {
            String s = "INSERT INTO " + tableName + 
                    " (name, score) VALUES('" + name + "'," + score + 
                    ") ON DUPLICATE KEY UPDATE score=" + score;
            return stmt.executeUpdate(s);
        } catch(Exception ex) { System.out.println("Hiba az adatbázisba való felvétel közben." + ex); }
        return 0;
    }
    
    public int getHisScore(String name) {
        int i=0;
        while(!(highScores.get(i).name.equals(name)) && (i != highScores.size()-1)) {
            i++;
        }
        if((i == highScores.size()-1) && (!highScores.get(i).name.equals(name))) {
            return 0;
        }
        return highScores.get(i).score;
    }

    public ArrayList<HighScore> getHighScores() {
        return highScores;
    }
}
