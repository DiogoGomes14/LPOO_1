package maze.gui;

import maze.logic.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Diogo Gomes on 12/04/2015.
 * Package: maze.gui.
 * Project: LPOO_1.
 */
public class StartGraphics {
    public static void main(String[] args) {
    JFrame f = new JFrame("Maze");
    Game game = new Game(9,2);
    JPanel panel = new MazeGraphics(game);
    //JButton button = new JButton();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setPreferredSize(new Dimension(1280, 800));
    f.getContentPane().add(panel);
    f.pack();
    f.setVisible(true);
    panel.requestFocus();
}
}
