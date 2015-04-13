package maze.gui;

import maze.logic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartGraphics {
    JFrame frame;
    MazeGraphics panelGraphics;
    JPanel panelButtons;
    JButton[] buttons;

    public StartGraphics() {
        frame = new JFrame("Maze");
        Game game = new Game(11,2,2);
        panelGraphics = new MazeGraphics(game);
        panelGraphics.setPreferredSize(new Dimension(800,800));
        panelGraphics.setMinimumSize(new Dimension(400, 400));
        panelButtons = new JPanel();
        panelButtons.setPreferredSize(new Dimension(180, 800));
        panelButtons.setMinimumSize(new Dimension(180, 400));
        panelButtons.setMaximumSize(new Dimension(180, 800));
        frame.setMinimumSize(new Dimension(640, 400));
        //panelButtons.setLayout(new BoxLayout(panelButtons,BoxLayout.Y_AXIS));

        JTextField mazeSize = new JTextField("Maze Size");
        JTextField numDragons = new JTextField("Number of dragons");

        buttons = new JButton[]{
                new JButton("Restart Game"),
                new JButton("Close Game"),
                new JButton("Load Game"),
                new JButton("Save Game"),
                new JButton("Settings")
        };
        Dimension buttonsSize = new Dimension(135, 100);
        buttons[0].setPreferredSize(buttonsSize); panelButtons.add("restart", buttons[0]);
        buttons[1].setPreferredSize(buttonsSize); panelButtons.add("close", buttons[1]);
        buttons[2].setPreferredSize(buttonsSize); panelButtons.add("load", buttons[2]);
        buttons[3].setPreferredSize(buttonsSize); panelButtons.add("save", buttons[3]);
        buttons[4].setPreferredSize(buttonsSize); panelButtons.add("settings", buttons[4]);

        //panelButtons.add(mazeSize);



        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panelButtons);
        frame.getContentPane().add(panelGraphics);
        frame.pack();
        frame.setVisible(true);
        panelGraphics.requestFocus();
    }

    private void buttonListeners(){
        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?");
                if (response == JOptionPane.YES_OPTION) {
                    Game game = new Game(11, 2, 2);
                    panelGraphics.setGame(game);
                    panelGraphics.state = "Playing";
                    panelGraphics.repaint();
                }
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the game?");
                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Game g = panelGraphics.game.load();
                    panelGraphics.setGame(g);
                    panelGraphics.setState("Playing");
                    panelGraphics.repaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Game loaded!", "Load", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    panelGraphics.game.save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Game saved", "Save", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        StartGraphics startGraphics = new StartGraphics();
        startGraphics.buttonListeners();
    }
}
