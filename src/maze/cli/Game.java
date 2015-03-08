package maze.cli;

import maze.logic.Maze;
import maze.logic.*;

import java.util.Random;


public class Game {
    private Dragon[] dragon;
    private Sword sword = new Sword();
    private Player player = new Player();
    private Maze maze;
    private boolean deadDragons = false;

    public Game() {
        maze = new Maze();
        dragon = new Dragon[1];
        dragon[0].setRow(4);
        dragon[0].setColumn(1);
        sword.setRow(8);
        sword.setColumn(1);
        player.setRow(1);
        player.setColumn(1);
    }

    public Game(int n){
        maze = new Maze(n);
        dragon = new Dragon[1];
        int randRow, randCol;
        Random rand = new Random();
        while (player.getColumn() == 0 && player.getRow() == 0) {
            randRow = rand.nextInt(n);
            randCol = rand.nextInt(n);
            System.out.println("A");
            if(maze.getMaze(randRow,randCol) == ' '){
                player.setRow(randRow);
                player.setColumn(randCol);
                System.out.println("1");
            }
        }
        while (sword.getColumn() == 0 && sword.getRow() == 0) {
            randRow = rand.nextInt(n);
            randCol = rand.nextInt(n);
            System.out.println("B");
            if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn()){
                sword.setRow(randRow);
                sword.setColumn(randCol);
                System.out.println("2");
            }
        }
        for(Dragon d : dragon){
            while (d.getColumn() == 0 && d.getRow() == 0) {
                randRow = rand.nextInt(n);
                randCol = rand.nextInt(n);
                System.out.println("C");
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn()){
                    d.setRow(randRow);
                    d.setColumn(randCol);
                    System.out.println("3");
                }
            }
        }
    }

    public Game(int mazeSize, int numDragons){
        maze = new Maze(mazeSize);
        dragon = new Dragon[numDragons];
        int randRow, randCol;
        Random rand = new Random();
        while (player.getColumn() == 0 && player.getRow() == 0) {
            randRow = rand.nextInt(mazeSize);
            randCol = rand.nextInt(mazeSize);
            System.out.println("A");
            if(maze.getMaze(randRow,randCol) == ' '){
                player.setRow(randRow);
                player.setColumn(randCol);
                System.out.println("1");
            }
        }
        while (sword.getColumn() == 0 && sword.getRow() == 0) {
            randRow = rand.nextInt(mazeSize);
            randCol = rand.nextInt(mazeSize);
            System.out.println("B");
            if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn()){
                sword.setRow(randRow);
                sword.setColumn(randCol);
                System.out.println("2");
            }
        }
        for(Dragon d : dragon){
            while (d.getColumn() == 0 && d.getRow() == 0) {
                randRow = rand.nextInt(mazeSize);
                randCol = rand.nextInt(mazeSize);
                System.out.println("C");
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn()){
                    d.setRow(randRow);
                    d.setColumn(randCol);
                    System.out.println("3");
                }
            }
        }
    }
    public void play(){
        calculateBoard();

        int time = 0;

        while (!(player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && deadDragons)) {
            // ask for the next player move
            player.newPosition(maze,deadDragons);
            // if the player is in the same position as the sword change the 'H' to 'A'
            if (player.getRow() == sword.getRow() && player.getColumn() == sword.getColumn())
                player.setHero('A');

            for(Dragon d : dragon){
                time = d.sleepCalculation(time);

                // if the Dragon is alive calculate next Dragon position
                if (d.getAlive() && d.getDragon() == 'D')
                    d.dragonMovement(maze);

                if(!playerDragonInteraction(d)){
                    break;
                }
            }

            calculateBoard();
            /*
            System.out.println("dragon: " + dragon.getAlive() + " " + dragon.getRow() + "-" + dragon.getColumn());
            System.out.println("player: " + player.getHero() + " " + player.getRow() + "-" + player.getColumn());
            System.out.println("exit: " + maze.getRow() + "-" + maze.getColumn());
            System.out.println("sword: " + sword.getRow() + "-" + sword.getColumn());
            */
        }
        System.out.println(deadDragons ? "You died." : "You have reached the exit");
        System.out.println("The end.");
    }

    public void calculateBoard() {
        // insert the sword on the map
        if (player.getHero() != 'A')
            maze.setMaze(sword.getRow(),sword.getColumn(), 'E');

        for(Dragon d : dragon){
            // insert the Dragon on the map
            if (d.getAlive())
                maze.setMaze(d.getRow(),d.getColumn(), d.getDragon());

            // check if the Dragon is in the same place as the sword and change that location to 'F'
            if (d.getRow() == sword.getRow() && d.getColumn() == sword.getColumn())
                maze.setMaze(d.getRow(),d.getColumn(), 'F');

        }

        // insert the hero on the map
        maze.setMaze(player.getRow(), player.getColumn(), player.getHero());

        Output.printMap(maze.getMaze(),maze.getSize());

        // reset the board
        int i = 0;
        for(Dragon d : dragon){
            maze.setMaze(d.getRow(), d.getColumn(), ' ');
            if(d.getAlive())
                i++;
        }
        if(i == dragon.length)
            deadDragons = true;

        maze.setMaze(sword.getRow(), sword.getColumn(), ' ');
        maze.setMaze(player.getRow(), player.getColumn(), ' ');
    }

    public boolean playerDragonInteraction(Dragon dragon){
        if ((player.getRow() == dragon.getRow() + 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() - 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() + 1)
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() - 1))
        {
            // if the hero doesn't have a sword when next to the Dragon print the map and end the game (die)
            if(player.getHero() != 'A'){
                calculateBoard();
                return false;
            }

            // if the has a sword next to the Dragon, kill it (dragon.alive = false)
            else
                dragon.setAlive(false);
        }
        return true;
    }
}