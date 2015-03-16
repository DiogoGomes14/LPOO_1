package maze.test;

import maze.logic.Game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Test {
    // TODO: change the hard implemented values to be dynamic

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
        game.updateGame('m' ,0);

        assertEquals('A', game.getPlayer().getHero());
    }

    @org.junit.Test
    public void testDyingByDragon() {
        Game game = new Game();

        game.getPlayer().setColumn(1);
        game.getPlayer().setRow(2);

        assertEquals(false, game.updateGame('m', 0));
    }

    @org.junit.Test
    public void testKillingDragon() {
        Game game = new Game();

        game.getPlayer().setColumn(1);
        game.getPlayer().setRow(2);
        game.getPlayer().setHero('A');

        assertEquals(true, game.updateGame('m', 0));
    }

    @org.junit.Test
    public void testVictory() {
        Game game = new Game();

        game.getPlayer().setRow(5);
        game.getPlayer().setColumn(8);
        game.getPlayer().setHero('A');

        game.getPlayer().newPosition(game.getMaze(),true,"r");

        game.updateGame('m',0);

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
