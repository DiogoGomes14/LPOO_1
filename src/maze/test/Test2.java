package maze.test;

import maze.logic.Game;
import maze.logic.weapons.Darts;

import static org.junit.Assert.assertEquals;

public class Test2 {

    @org.junit.Test
    public void testSimpleDragonMove() {
        Game game = new Game();
        int iniRow = game.getDragons()[0].getRow();
        int iniColumn = game.getDragons()[0].getColumn();

        game.dragonMovement(game.getDragons()[0],3); // move down

        assertEquals(iniRow + 1, game.getDragons()[0].getRow());
        assertEquals(iniColumn, game.getDragons()[0].getColumn());
    }

    @org.junit.Test
    public void testDragonMoveWall() {
        Game game = new Game();
        int iniRow = game.getDragons()[0].getRow();
        int iniColumn = game.getDragons()[0].getColumn();

        game.dragonMovement(game.getDragons()[0], 1); // move down

        assertEquals(iniRow, game.getDragons()[0].getRow());
        assertEquals(iniColumn, game.getDragons()[0].getColumn());
    }

    @org.junit.Test
    public void testDragonSleep() {
        Game game = new Game();

        game.getDragons()[0].setDragon('d');

        assertEquals('d', game.getDragons()[0].getDragon());
    }

    @org.junit.Test
    public void testMultipleDragons() {
        Game game = new Game(21,10,0);

        assertEquals(10, game.getDragons().length);
    }

    @org.junit.Test
    public void testDragonFire() {
        Game game = new Game();

        boolean dragonAttack = game.dragonAttack();

        assertEquals(true, dragonAttack);
    }

    @org.junit.Test
    public void testPickupShield() {
        Game game = new Game();

        game.setShield(1,2);
        game.getShield().setVisible(true);
        game.getPlayer().newPosition(game.getMaze(),false,"r");
        game.updateGame('m');

        assertEquals(1, game.getPlayer().getInventory(0));
    }

    @org.junit.Test
    public void testPickupDarts() {
        Game game = new Game();

        Darts[] darts = new Darts[1];
        darts[0] = new Darts();

        game.setDarts(darts);
        if(!game.setDarts(0, 1, 2)){
            return;
        }
        game.getPlayer().newPosition(game.getMaze(),false,"r");
        game.updateGame('m');

        assertEquals(1, game.getPlayer().getInventory(1));
    }

    @org.junit.Test
    public void testShootDarts() {
        Game game = new Game();
        game.getDragons()[0].setRow(6);
        game.getPlayer().setInventory(1, 2);

        game.updateGame('d');
        assertEquals(1, game.getPlayer().getInventory(1));
        assertEquals(false, game.getDragons()[0].isAlive());
    }

}
