package maze.cli;

import maze.logic.Maze;
import maze.logic.*;

import java.util.Random;


public class Game {
    private Dragon dragon = new Dragon();
    private Sword sword = new Sword();
    private Player player = new Player();
    private Maze maze;

    public Game() {
        System.out.println("Type 0 if you want to play in the predefined maze or a different number to play in a random one: ");
        int n = Input.getNumber();
        if( n == 0 ){
            maze = new Maze();
            dragon.setRow(4);
            dragon.setColumn(1);
            sword.setRow(8);
            sword.setColumn(1);
            player.setRow(1);
            player.setColumn(1);
        }
        else {
            do {
                System.out.println("Type in the size of the maze(odd number, > 8 and < 100: ");
                n = Input.getNumber();
            } while (!(n > 8 && n < 100 && n % 2 != 0));
            maze = new Maze(n);
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
            while (dragon.getColumn() == 0 && dragon.getRow() == 0) {
                randRow = rand.nextInt(n);
                randCol = rand.nextInt(n);
                System.out.println("C");
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn()){
                    dragon.setRow(randRow);
                    dragon.setColumn(randCol);
                    System.out.println("3");
                }
            }

        }
    }

    public void play(){
        calculateBoard();

        int i = 0;
        Random rand = new Random();

        while (!(player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && !dragon.getAlive())) {
            // ask for the next player move
            player.newPosition(maze, dragon);
            // if the player is in the same position as the sword change the 'H' to 'A'
            if (player.getRow() == sword.getRow() && player.getColumn() == sword.getColumn())
                player.setHero('A');

            if (dragon.getDragon() == 'd' && i < dragon.getTimeSleep())
                i++;
            else if (i >= dragon.getTimeSleep() && dragon.getDragon() == 'd'){
                i = 0;
                dragon.setDragon('D');
            }

            if (dragon.getDragon() == 'D' && rand.nextInt(5) == 0){
                dragon.setDragon('d');
                dragon.setTimeSleep(rand.nextInt(3) + 2);
            }
            // if the Dragon is alive calculate next Dragon position
            if (dragon.getAlive() && dragon.getDragon() == 'D')
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
            /*
            System.out.println("dragon: " + dragon.getAlive() + " " + dragon.getRow() + "-" + dragon.getColumn());
            System.out.println("player: " + player.getHero() + " " + player.getRow() + "-" + player.getColumn());
            System.out.println("exit: " + maze.getRow() + "-" + maze.getColumn());
            System.out.println("sword: " + sword.getRow() + "-" + sword.getColumn());
            */
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
            maze.setMaze(dragon.getRow(),dragon.getColumn(), dragon.getDragon());

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