package maze.cli;

import maze.logic.Maze;
import maze.logic.*;


public class Game {
    private Dragon dragon = new Dragon();
    private Sword sword = new Sword();
    private Player player = new Player();
    private Maze maze = new Maze();

    public Game() {
        dragon.setRow(4);
        dragon.setColumn(1);
        sword.setRow(8);
        sword.setColumn(1);
        player.setRow(1);
        player.setColumn(1);

        calculateBoard();

        while (!(player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && !dragon.getAlive())) {
            // ask for the next player move
            player.newPosition(maze, dragon);

            // if the player is in the same position as the sword change the 'H' to 'A'
            if (player.getRow() == sword.getRow() && player.getColumn() == sword.getColumn())
                player.setHero('A');

            // if the Dragon is alive calculate next Dragon position
            if (dragon.getAlive())
                dragon.dragonMovement(maze);


            if ((player.getRow() == dragon.getRow() + 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() - 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() + 1)
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() - 1))
            {
                // if the hero doesn't have a sword when next to the Dragon print the map and end the game (die)
                if(player.getHero() != 'A'){
                    calculateBoard();
                    break;
                }

                // if the has a sword next to the Dragon, kill it (dragon.alive = false)
                else if(player.getHero() == 'A')
                    dragon.setAlive(false);
            }
            calculateBoard();
        }
        System.out.println(dragon.getAlive() ? "You died." : "You have reached the exit");
        System.out.println("The end.");
    }

    public void calculateBoard() {
        // insert the sword on the map
        if (player.getHero() != 'A')
            maze.setMaze(sword.getRow(),sword.getColumn(), 'E');

        // insert the Dragon on the map
        if (dragon.getAlive())
            maze.setMaze(dragon.getRow(),dragon.getColumn(), 'D');

        // check if the Dragon is in the same place as the sword and change that location to 'F'
        if (dragon.getRow() == sword.getRow() && dragon.getColumn() == sword.getColumn())
            maze.setMaze(dragon.getRow(),dragon.getColumn(), 'F');

        // insert the hero on the map
        maze.setMaze(player.getRow(), player.getColumn(), player.getHero());

        Output.printMap(maze.getMaze(),maze.getSize());

        // reset the board
        maze.setMaze(dragon.getRow(), dragon.getColumn(), ' ');
        maze.setMaze(sword.getRow(), sword.getColumn(), ' ');
        maze.setMaze(player.getRow(), player.getColumn(), ' ');
    }
}