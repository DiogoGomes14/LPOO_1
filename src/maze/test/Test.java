package maze.test;

import maze.logic.Game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Test {

    @org.junit.Test
    public void testSimpleMove() {
        Game game = new Game();

        game.getPlayer().newPosition(game.getMaze(),false,"d");

        assertEquals(2, game.getPlayer().getRow());
        assertEquals(1, game.getPlayer().getColumn());
    }

    @org.junit.Test
    public void testWallMove() {
        Game game = new Game();

        game.getPlayer().newPosition(game.getMaze(),false,"l");

        assertEquals(1, game.getPlayer().getRow());
        assertEquals(1, game.getPlayer().getColumn());
    }

    @org.junit.Test
    public void testSword() {
        Game game = new Game();

        game.getPlayer().setColumn(1);
        game.getPlayer().setRow(7);

        game.getPlayer().newPosition(game.getMaze(),false,"d");
        game.updateGame('m');

        assertEquals('A', game.getPlayer().getHero());
    }

    @org.junit.Test
    public void testDyingByDragon() {
        Game game = new Game();

        game.getPlayer().setColumn(1);
        game.getPlayer().setRow(2);

        assertEquals('D', game.updateGame('m'));
    }

    @org.junit.Test
    public void testKillingDragon() {
        Game game = new Game();

        game.getPlayer().setColumn(1);
        game.getPlayer().setRow(2);
        game.getPlayer().setHero('A');
        game.getPlayer().setInventory(0,1);
        game.getDragons()[0].setColumn(1);
        game.getDragons()[0].setRow(3);
        game.getDragons()[0].setTimeSleep(10);

        assertEquals('C', game.updateGame('m'));
    }

    @org.junit.Test
    public void testVictory() {
        Game game = new Game();

        game.getPlayer().setRow(5);
        game.getPlayer().setColumn(8);
        game.getPlayer().setHero('A');

        game.getPlayer().newPosition(game.getMaze(),true,"r");

        game.updateGame('m');

        assertEquals(game.getMaze().getRow(), game.getPlayer().getRow());
        assertEquals(game.getMaze().getColumn(), game.getPlayer().getColumn());
    }

    @org.junit.Test
    public void testFalseExit() {
        Game game = new Game();

        game.getPlayer().setRow(5);
        game.getPlayer().setColumn(8);

        game.getPlayer().newPosition(game.getMaze(),true,"r");

        assertNotEquals(game.getMaze().getColumn(), game.getPlayer().getColumn());

        game.getPlayer().setHero('A');
        game.getPlayer().newPosition(game.getMaze(),false,"r");

        assertNotEquals(game.getMaze().getColumn(), game.getPlayer().getColumn());
    }

}
