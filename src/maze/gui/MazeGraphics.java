package maze.gui;

import maze.logic.Dragon;
import maze.logic.Game;
import maze.logic.weapons.Darts;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazeGraphics extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    Game game;
    BufferedImage heroSprite;
    BufferedImage wallSprite;
    BufferedImage dragonSprite;
    BufferedImage swordSprite;
    BufferedImage dartSprite;
    BufferedImage shieldSprite;
    BufferedImage exitSprite;
    BufferedImage fireSprite;

    public MazeGraphics(Game game) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.game = game;

        try {
            heroSprite = ImageIO.read(new File("sprites\\hero.png"));
            wallSprite = ImageIO.read(new File("sprites\\wall.png"));
            dragonSprite = ImageIO.read(new File("sprites\\dragon.png"));
            swordSprite = ImageIO.read(new File("sprites\\sword.png"));
            dartSprite = ImageIO.read(new File("sprites\\dart.png"));
            shieldSprite = ImageIO.read(new File("sprites\\shield.png"));
            exitSprite = ImageIO.read(new File("sprites\\exit.png"));
            fireSprite = ImageIO.read(new File("sprites\\fire.png"));
        } catch (IOException e) {
            System.err.println("Failed to read sprites");
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Maze");
        Game game = new Game();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(1280, 800));
        f.getContentPane().add(new MazeGraphics(game));
        f.pack();
        f.setVisible(true);
        f.requestFocus();
    }

    public void paintComponent(Graphics g) {
        //TODO recheck this entire method
        super.paintComponent(g); // clear background ...

        int size = this.game.getMaze().getSize();
        int width = getWidth()/size;
        int height = getHeight()/size;

        // color of the lines
        g.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.game.getMaze().getMaze(i, j) == 'X') {
                    g.drawImage(wallSprite, width * j, height * i, width, height, null);
                    g.drawRect(width * j, height * i, width * (j + 1), height * (i + 1));
                }
                else if(this.game.getMaze().getMaze(i, j) == 'S'){
                    g.drawImage(wallSprite, width * j, height * i, width, height, null);
                    g.drawImage(exitSprite, width * j, height * i, width, height, null);
                    g.drawRect(width * j, height * i, width * (j + 1), height * (i + 1));
                }
                else {
                    g.drawRect(width * j, height * i, width * (j + 1), height * (i + 1));
                }
            }
        }

        g.drawImage(heroSprite, width * this.game.getPlayer().getRow(), height * this.game.getPlayer().getRow(), width, height, null);

        if (this.game.getShield().isVisible()){
            g.drawImage(shieldSprite, width * this.game.getShield().getColumn(), height * this.game.getShield().getRow(), width, height, null);
        }

        // check if player has sword and draw it if true (in the top left corner of the player position)
        if (this.game.getPlayer().getHero() == 'A'){
            //TODO verify if in the correct position in the square
            g.drawImage(swordSprite, width * this.game.getPlayer().getColumn(), height * this.game.getPlayer().getRow(), width/4, height/4, null);
        }
        else { // draw in it's original position if false
            g.drawImage(swordSprite, width * this.game.getSword().getColumn(), height * this.game.getSword().getRow(), width, height, null);
        }

        // checking if player has shield and draw it if true (in the bottom right corner)
        if (this.game.getPlayer().getInventory(0) == 1){
            //TODO verify if in the correct position in the square
            g.drawImage(shieldSprite, width * this.game.getShield().getColumn() + (3 * width)/4, height * this.game.getShield().getRow() + (3*height)/4, width/4, height, null);
        }

        for (Darts dart : this.game.getDarts()){
            if(dart.isVisible()){
                g.drawImage(dartSprite, width * dart.getColumn(), height * dart.getRow(), width, height, null);
            }
        }
        for (Dragon dragon : this.game.getDragons()){
            if (dragon.isAlive()){
                g.drawImage(dragonSprite, width * dragon.getColumn(), height * dragon.getRow(), width, height, null);
            }
        }
        //g.fillRect(0,0,getWidth(),getHeight()/2);
        //g.drawString();
    }

    //TODO add input

    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                char ch = this.game.getPlayer().newPosition(this.game.getMaze(), this.game.isDeadDragons(), "l");

                if( !this.game.updateGame(ch, 0/*typeOfDragonMovement*/) ){ // TODO change
                    //break;
                    // TODO show game done
                }
                repaint();
                break;

            case KeyEvent.VK_RIGHT:
                repaint();
                break;

            case KeyEvent.VK_UP:

                repaint();
                break;

            case KeyEvent.VK_DOWN:

                repaint();
                break;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void mousePressed(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent arg0) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}