/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import db.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author kuble
 */
public class MainWindow {
    private JFrame f;
    private GamePanel gp;
    
    private String player1Name = null;
    private String player2Name = null;
    private String color1 = null;
    private String color2 = null;
    
    private final JTextField p1TF;
    private final JTextField p2TF;
    private JComboBox<String> chooseColor1;
    private JComboBox<String> chooseColor2;
    private JButton submitBTN;
    
    private final String[] colors = {"black", "blue", "green", "orange", "red", "white", "yellow", "pink"};
    
    public MainWindow() {
        //Basics
        f = new JFrame("Tron");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(new Dimension(700,700));
        
        //Icon
        URL url = MainWindow.class.getClassLoader().getResource("res/tronicon.jpg");
        f.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        //Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Játék");
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Kilépés") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenu menuGameNew = new JMenu("Új játék");
        for(int i=0; i<10; i++) {
            int j = i+1;
            JMenuItem mi = new JMenuItem(new AbstractAction(j + ". pálya") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(player1Name != null && player2Name != null && color1 != null && color2 != null && !player1Name.equals("") && !player2Name.equals("")) {
                        gp.restart(player1Name, color1, player2Name, color2, j);
                    }
                }
            });
            menuGameNew.add(mi);
        }
        JMenuItem menuGameHighScores = new JMenuItem(new AbstractAction("Dicsőség tábla") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database db = new Database();
                new HighScoreWindow(db.getHighScores(), f);
            }
        });
        
        menuGame.add(menuGameNew);
        menuGame.add(menuGameHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        f.setJMenuBar(menuBar);
        
        //Indításkor
        p1TF = new JTextField("Első játékos neve");
        p2TF = new JTextField("Második játékos neve");
        chooseColor1 = new JComboBox<String>(colors);
        chooseColor2 = new JComboBox<String>(colors);
        chooseColor1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                color1 = chooseColor1.getSelectedItem().toString();
            }
        });
        chooseColor2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               color2 = chooseColor2.getSelectedItem().toString();
            }
        });
        submitBTN = new JButton("Elküldés");
        submitBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                player1Name = p1TF.getText();
                player2Name = p2TF.getText();
                
                if(player1Name == null || player2Name == null || color1 == null || color2 == null || player1Name.equals("") || player2Name.equals("") || player1Name.equals("Első játékos neve") || player2Name.equals("Második játékos neve")) {
                    submitBTN.setText("Mindkettőtök nevét kötelező megadni és mindkettőtöknek kötelező színt választani!");
                } else {
                    try {
                        f.setVisible(false);
                        f.getContentPane().removeAll();
                        f.setLayout(new BorderLayout());
                        gp = new GamePanel(player1Name, color1, player2Name, color2, 1);
                        gp.setPreferredSize(new Dimension(gp.getGamePanelWidth(),gp.getGamePanelHeight()));
                        f.add(gp);
                        f.repaint();
                        f.pack();
                        f.setVisible(true);
                    } catch (IOException ex) {}
                }
            }
        });
        
        if(player1Name == null || player2Name == null) {
            f.add(p1TF); f.add(chooseColor1);
            f.add(p2TF); f.add(chooseColor2);
            f.add(submitBTN);
            f.setLayout(new GridLayout(5,5));
        }
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
