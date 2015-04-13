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
    int mazeSize;
    int numDragons;
    int typeMovement;

    public StartGraphics() {
        frame = new JFrame("Maze");
        mazeSize = 11;
        numDragons = 3;
        typeMovement = 2;
        Game game = new Game(mazeSize,numDragons,typeMovement);
        panelGraphics = new MazeGraphics(game);
        panelGraphics.setPreferredSize(new Dimension(800,800));
        panelGraphics.setMinimumSize(new Dimension(400, 400));
        panelButtons = new JPanel();
        panelButtons.setPreferredSize(new Dimension(180, 800));
        panelButtons.setMinimumSize(new Dimension(180, 400));
        panelButtons.setMaximumSize(new Dimension(180, 800));
        frame.setMinimumSize(new Dimension(950, 800));

        buttons = new JButton[]{
                new JButton("Restart Game"),
                new JButton("Close Game"),
                new JButton("Load Game"),
                new JButton("Save Game"),
                new JButton("Maze Size"),
                new JButton("Number of Dragons"),
                new JButton("Dragon Movement")
        };
        Dimension buttonsSize = new Dimension(150, 100);
        buttons[0].setPreferredSize(buttonsSize); panelButtons.add("restart", buttons[0]);
        buttons[1].setPreferredSize(buttonsSize); panelButtons.add("close", buttons[1]);
        buttons[2].setPreferredSize(buttonsSize); panelButtons.add("load", buttons[2]);
        buttons[3].setPreferredSize(buttonsSize); panelButtons.add("save", buttons[3]);
        buttons[4].setPreferredSize(buttonsSize); panelButtons.add("mazeSize", buttons[4]);
        buttons[5].setPreferredSize(buttonsSize); panelButtons.add("numDragons", buttons[5]);
        buttons[6].setPreferredSize(buttonsSize); panelButtons.add("typeMovement", buttons[6]);

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
                    Game game = new Game(mazeSize, numDragons, typeMovement);
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

        buttons[4].addActionListener(new ActionListener() {
            JPanel mazeSizePanel = new JPanel();
            JTextArea textArea = new JTextArea("Maze Size: " + Integer.toString(mazeSize));
            JDialog mazeSizeDialog = new JDialog();
            public void actionPerformed(ActionEvent e) {
                mazeSizePanel.add(textArea);

                JButton increase = new JButton("Increase");
                JButton decrease = new JButton("Decrease");
                mazeSizePanel.setSize(100, 100);
                mazeSizeDialog.setSize(new Dimension(200, 200));
                mazeSizeDialog.setVisible(true);
                mazeSizeDialog.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.gridy = 1;
                c.gridx = 0;
                c.gridwidth = 2;
                mazeSizeDialog.add(mazeSizePanel, c);

                c.gridy = 5;
                c.gridx = GridBagConstraints.RELATIVE;
                c.gridwidth = 1;
                mazeSizeDialog.add(increase, c);
                mazeSizeDialog.add(decrease, c);


                increase.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if ((mazeSize += 2) > 50) {
                            mazeSize = 51;
                        }
                        textArea.setText("Maze Size: " + Integer.toString(mazeSize));
                        mazeSizePanel.repaint();
                    }
                });
                decrease.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if ((mazeSize-=2) < 10) {
                            mazeSize = 9;
                        }
                        textArea.setText("Maze Size: " + Integer.toString(mazeSize));
                        mazeSizePanel.repaint();
                    }
                });
            }
        });

        buttons[5].addActionListener(new ActionListener() {
            JPanel numDragonsText = new JPanel();
            JTextArea textArea = new JTextArea("Number of dragons: " + Integer.toString(numDragons));
            JDialog numDragonsDialog = new JDialog();
            public void actionPerformed(ActionEvent e) {
                numDragonsText.add(textArea);

                JButton increase = new JButton("Increase");
                JButton decrease = new JButton("Decrease");
                numDragonsText.setSize(100, 100);
                numDragonsDialog.setSize(new Dimension(200, 200));
                numDragonsDialog.setVisible(true);
                numDragonsDialog.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.gridy = 1; c.gridx = 0; c.gridwidth = 2;
                numDragonsDialog.add(numDragonsText,c);

                c.gridy = 5; c.gridx = GridBagConstraints.RELATIVE;
                c.gridwidth = 1;
                numDragonsDialog.add(increase,c);
                numDragonsDialog.add(decrease,c);


                increase.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(numDragons++ > mazeSize / 2 - 1 ){
                            numDragons = mazeSize / 2;
                        }
                        textArea.setText("Number of dragons: " + Integer.toString(numDragons));
                        numDragonsText.repaint();
                    }
                });
                decrease.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(numDragons-- < 2){
                            numDragons = 1;
                        }
                        textArea.setText("Number of dragons: " + Integer.toString(numDragons));
                        numDragonsText.repaint();
                    }
                });
            }
        });

        buttons[6].addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog movementDialog = new JDialog();
                movementDialog.setSize(new Dimension(400, 100));
                movementDialog.setVisible(true);
                movementDialog.setLayout(new FlowLayout());
                JPanel movementPanel = new JPanel();

                JButton immobile = new JButton("Immobile");
                JButton mobile = new JButton("Mobile");
                JButton sleep = new JButton("Mobile and Sleep");
                movementDialog.add(immobile);
                movementDialog.add(mobile);
                movementDialog.add(sleep);
                movementDialog.add(movementPanel);



                immobile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        typeMovement = 0;
                    }
                });
                mobile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        typeMovement = 1;
                    }
                });
                sleep.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        typeMovement = 2;
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        StartGraphics startGraphics = new StartGraphics();
        startGraphics.buttonListeners();
    }
}
