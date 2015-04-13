package maze.gui;

import maze.logic.Dragon;
import maze.logic.Game;
import maze.logic.weapons.Darts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class MazeGraphics extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    public Game game;
    BufferedImage heroSprite;
    BufferedImage wallSprite;
    BufferedImage dragonSprite;
    BufferedImage sleepingDragonSprite;
    BufferedImage swordSprite;
    BufferedImage dartSprite;
    BufferedImage shieldSprite;
    BufferedImage exitSprite;
    BufferedImage fireSprite;
    BufferedImage floorSprite;
    String state = "Playing";
    Font f;

    public MazeGraphics(Game game) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.game = game;

        f = new Font("Dialog", Font.PLAIN, 66);
        try {
            heroSprite = ImageIO.read(new File("sprites\\hero.png"));
            wallSprite = ImageIO.read(new File("sprites\\wall.png"));
            dragonSprite = ImageIO.read(new File("sprites\\dragon.png"));
            sleepingDragonSprite = ImageIO.read(new File("sprites\\sleeping.png"));
            swordSprite = ImageIO.read(new File("sprites\\sword.png"));
            dartSprite = ImageIO.read(new File("sprites\\dart.png"));
            shieldSprite = ImageIO.read(new File("sprites\\shield.png"));
            exitSprite = ImageIO.read(new File("sprites\\exit.png"));
            fireSprite = ImageIO.read(new File("sprites\\fire.png"));
            floorSprite = ImageIO.read(new File("sprites\\floor.png"));
        } catch (IOException e) {
            System.err.println("Failed to read sprites");
            //e.printStackTrace();
        }
    }

    private void playing(Graphics g){
        super.paintComponent(g); // clear background ...

        int size = this.game.getMaze().getSize();
        int width = getWidth()/size;
        int height = getHeight()/size;

        // color of the lines
        //g.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.game.getMaze().getMaze(i, j) == 'X') {
                    g.drawImage(wallSprite, width * j, height * i, width, height, null);
                    //g.drawRect(width * j, height * i, width * (j + 1), height * (i + 1));
                }
                else if(this.game.getMaze().getMaze(i, j) == 'S'){
                    g.drawImage(floorSprite, width * j, height * i, width, height, null);
                    g.drawImage(exitSprite, width * j, height * i, width, height, null);
                    //g.drawRect(width * j, height * i, width * (j + 1), height * (i + 1));
                }
                else {
                    g.drawImage(floorSprite, width * j, height * i, width, height, null);
                }
            }
        }

        g.drawImage(heroSprite, width * this.game.getPlayer().getColumn(), height * this.game.getPlayer().getRow(), width, height, null);

        if (this.game.getShield().isVisible()){
            g.drawImage(shieldSprite, width * this.game.getShield().getColumn(), height * this.game.getShield().getRow(), width, height, null);
        }

        // check if player has sword and draw it if true (in the top left corner of the player position)
        if (this.game.getPlayer().getHero() == 'A'){
            //TODO verify if in the correct position in the square
            g.drawImage(swordSprite, width * this.game.getPlayer().getColumn(), height * this.game.getPlayer().getRow() + (3 * height)/4, width/4, height/4, null);
        }
        else { // draw in it's original position if false
            g.drawImage(swordSprite, width * this.game.getSword().getColumn(), height * this.game.getSword().getRow(), width, height, null);
        }

        // checking if player has shield and draw it if true (in the bottom right corner)
        if (this.game.getPlayer().getInventory(0) == 1){
            //TODO verify if in the correct position in the square
            g.drawImage(shieldSprite, width * this.game.getPlayer().getColumn() + (3 * width)/4, height * this.game.getPlayer().getRow() + (3 * height)/4, width/4, height/4, null);
        }

        for (Darts dart : this.game.getDarts()){
            if(dart.isVisible()){
                g.drawImage(dartSprite, width * dart.getColumn(), height * dart.getRow(), width, height, null);
            }
        }
        for (Dragon dragon : this.game.getDragons()){
            if (dragon.isAlive()){
                g.drawImage(!dragon.isAsleep() ? dragonSprite : sleepingDragonSprite, width * dragon.getColumn(), height * dragon.getRow(), width, height, null);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("Darts: " + this.game.getPlayer().getInventory(1), 10, 20);
        g.drawString((this.game.getPlayer().getInventory(0) == 1 ? "Shield" : ""), 10, 40);
        g.drawString((this.game.getPlayer().getHero() == 'A' ? "Sword" : ""), 10, 60);
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void paintComponent(Graphics g) {

        if(state.equals("Playing")){
            playing(g);
        }
        else if (state.equals("Victory")) {
            g.setFont(f);
            //g.setColor(Color.WHITE);
            //g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLUE);
            g.drawString("You Win!!!", getWidth() / 4, getHeight() / 2);
        }
        else if (state.equals("End")){
            g.setFont(f);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", getWidth() / 4, getHeight() / 2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(state.equals("Playing")){
            char ch;
            char updateState;
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    ch = this.game.getPlayer().newPosition(this.game.getMaze(), this.game.isAllDragonsDead(), "l");
                    System.out.println("Left");
                    updateState = this.game.updateGame(ch);
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }

                    /*
                    System.out.print( this.game.getPlayer().getRow() + " - " +  this.game.getPlayer().getColumn() + ";\t");
                    for (Dragon d : this.game.getDragons()){
                        System.out.print(d.getRow() + " - " + d.getColumn() + ";");
                    }
                    System.out.println();*/
                    repaint();
                    break;
                case KeyEvent.VK_RIGHT:
                    ch = this.game.getPlayer().newPosition(this.game.getMaze(), this.game.isAllDragonsDead(), "r");
                    System.out.println("Right");
                    updateState = this.game.updateGame(ch);
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    repaint();
                    break;
                case KeyEvent.VK_UP:
                    ch = this.game.getPlayer().newPosition(this.game.getMaze(), this.game.isAllDragonsDead(), "u");
                    System.out.println("Up");
                    updateState = this.game.updateGame(ch);
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    ch = this.game.getPlayer().newPosition(this.game.getMaze(), this.game.isAllDragonsDead(), "d");
                    System.out.println("Down");
                    updateState = this.game.updateGame(ch);
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    repaint();
                    break;
                case KeyEvent.VK_D:
                    System.out.println("Shot right");
                    updateState = this.game.updateGame('r');
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    break;
                case KeyEvent.VK_A:
                    System.out.println("Shot left");
                    updateState = this.game.updateGame('l');
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    break;
                case KeyEvent.VK_W:
                    System.out.println("Shot up");
                    updateState = this.game.updateGame('u');
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    break;
                case KeyEvent.VK_S:
                    System.out.println("Shot down");
                    updateState = this.game.updateGame('d');
                    if(updateState == 'W') {
                        System.out.println("Victory");
                        state = "Victory";
                    }
                    else if (updateState == 'D') {
                        System.out.println("Game Over");
                        state = "End";
                    }
                    break;
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        this.grabFocus();
    }
    public void mouseDragged(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent arg0) {this.grabFocus();}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void setState(String state) {
        this.state = state;
    }
}