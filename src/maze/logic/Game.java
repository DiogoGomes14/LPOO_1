package maze.logic;

import maze.cli.Interface;
import maze.logic.weapons.*;

import java.util.Random;

// TODO add dragon ranged attack if less than 3 blocks away

public class Game {
    private Dragon[] dragon;
    private Sword sword = new Sword();
    private Darts darts[];
    private Shield shield = new Shield();
    private Player player = new Player();
    private Maze maze;
    private int time = 0;
    private boolean deadDragons = false;

    public Game() {
        maze = new Maze();
        dragon = new Dragon[1];
        dragon[0] = new Dragon();
        dragon[0].setRow(3);
        dragon[0].setColumn(1);
        sword.setRow(8);
        sword.setColumn(1);
        player.setRow(1);
        player.setColumn(1);
        darts = new Darts[0];
    }

    public Game(int mazeSize, int numDragons){
        maze = new Maze(mazeSize);
        dragon = new Dragon[numDragons];
        int randRow, randCol;
        Random rand = new Random();

        // set initial player position
        while (player.getColumn() == 0 && player.getRow() == 0) {
            randRow = rand.nextInt(mazeSize);
            randCol = rand.nextInt(mazeSize);
            if(maze.getMaze(randRow,randCol) == ' '){
                player.setRow(randRow);
                player.setColumn(randCol);
            }
        }

        // set initial sword position
        while (sword.getColumn() == 0 && sword.getRow() == 0) {
            randRow = rand.nextInt(mazeSize);
            randCol = rand.nextInt(mazeSize);
            if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn()){
                sword.setRow(randRow);
                sword.setColumn(randCol);
            }
        }

        // set initial shield position
        while (shield.getColumn() == 0 && shield.getRow() == 0) {
            randRow = rand.nextInt(mazeSize);
            randCol = rand.nextInt(mazeSize);
            if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn()){
                shield.setRow(randRow);
                shield.setColumn(randCol);
            }
        }

        // set initial dragons positions
        for(int i = 0; i < numDragons; i++){
            dragon[i] = new Dragon();
            while (dragon[i].getColumn() == 0 && dragon[i].getRow() == 0) {
                randRow = rand.nextInt(mazeSize);
                randCol = rand.nextInt(mazeSize);
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn() && randRow != shield.getRow() && randCol != shield.getColumn()){
                    dragon[i].setRow(randRow);
                    dragon[i].setColumn(randCol);
                }
            }
        }

        darts = new Darts[rand.nextInt(5) + 1];
        // set initial darts positions
        for(int i = 0; i < darts.length; i++){
            darts[i] = new Darts();
            while (darts[i].getColumn() == 0 && darts[i].getRow() == 0) {
                randRow = rand.nextInt(mazeSize);
                randCol = rand.nextInt(mazeSize);
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn() && randRow != shield.getRow() && randCol != shield.getColumn()){
                    darts[i].setRow(randRow);
                    darts[i].setColumn(randCol);
                }
            }
        }
    }

    public void play(int typeOfDragonMovement){
        calculateBoard();

        while (!(player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && deadDragons)) {
            // ask for the next player move

            String move = Interface.getPlayerMove();
            char ch = player.newPosition(maze, deadDragons, move);

            if( !updateGame(ch, typeOfDragonMovement) ){
                calculateBoard();
                break;
            }

            calculateBoard();
        }
        System.out.println(!deadDragons ? "You died." : "You have reached the exit.");
        System.out.println("The end.");
    }

    public boolean updateGame(char ch, int typeOfDragonMovement){
        if (ch == ' ')
            return true;
        else if (ch != 'm' && darts.length != 0){ // if move is shoot do:
            int dragonIndex = shoot(ch);
            if (dragonIndex != -1){

                // one less dart on inventory
                player.setInventory(1,player.getInventory(1) - 1);

                // kill dragon
                dragon[dragonIndex].setAlive(false);
            }

        }

        if (player.getRow() == shield.getRow() && player.getColumn() == shield.getColumn()){
            player.setInventory(0,1);
        }

        for (Darts dart : darts){
            if(player.getRow() == dart.getRow() && player.getColumn() == dart.getColumn()){
                player.setInventory(1, player.getInventory(1) + 1);
            }
        }

        // if the player is in the same position as the sword change the 'H' to 'A'
        if (player.getRow() == sword.getRow() && player.getColumn() == sword.getColumn())
            player.setHero('A');


        for(Dragon d : dragon){
            if(typeOfDragonMovement != 0) {
                if (typeOfDragonMovement == 2)
                    time = d.sleepCalculation(time);

                // if the Dragon is alive calculate next Dragon position
                if (d.getAlive() && d.getDragon() == 'D')
                    d.dragonMovement(maze);
            }
            if (!playerDragonInteraction(d)) {
                return false; // endgame
            }
        }


        /*
        System.out.println("dragon: " + dragon.getAlive() + " " + dragon.getRow() + "-" + dragon.getColumn());
        System.out.println("player: " + player.getHero() + " " + player.getRow() + "-" + player.getColumn());
        System.out.println("exit: " + maze.getRow() + "-" + maze.getColumn());
        System.out.println("sword: " + sword.getRow() + "-" + sword.getColumn());
        */
        return true;
    }

    public void calculateBoard() {
        //TODO print darts and shield

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

        Interface.printMap(maze.getMaze(), maze.getSize());

        // reset the board
        int numberOfAliveDragons = 0;
        for(Dragon d : dragon){
            maze.setMaze(d.getRow(), d.getColumn(), ' ');
            if(d.getAlive())
                numberOfAliveDragons++;
        }
        if(numberOfAliveDragons == 0)
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
                return false;
            }

            // if the has a sword next to the Dragon, kill it (dragon.alive = false)
            else
                dragon.setAlive(false);
        }
        return true;
    }

    public int shoot(char direction) {
        int wallRow = 0;
        int wallColumn = 0;
        int dragonIndex = -1;

        switch (direction) {
            case 'l':
                for (int i = 1; i <= maze.getSize(); i++){
                    if(maze.getMaze(player.getRow(), player.getColumn() - i) == 'X'){
                        wallColumn = player.getColumn() - i;
                        wallRow = player.getRow();
                        break;
                    }
                }
                for (int i = 0; i < dragon.length; i++){
                    if(dragon[i].getRow() == wallRow && dragon[i].getColumn() > wallColumn && dragon[i].getColumn() < player.getColumn()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragon[i].getColumn() > dragon[dragonIndex].getColumn())
                            dragonIndex = i;
                    }
                }
                break;
            case 'r':
                for (int i = 1; i <= maze.getSize(); i++){
                    if(maze.getMaze(player.getRow(), player.getColumn() + i) == 'X'){
                        wallColumn = player.getColumn() + i;
                        wallRow = player.getRow();
                        break;
                    }
                }
                for (int i = 0; i < dragon.length; i++){
                    if(dragon[i].getRow() == wallRow && dragon[i].getColumn() < wallColumn && dragon[i].getColumn() > player.getColumn()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragon[i].getColumn() < dragon[dragonIndex].getColumn())
                            dragonIndex = i;
                    }
                }
                break;
            case 'u':
                for (int i = 1; i <= maze.getSize(); i++){
                    if(maze.getMaze(player.getRow() - i, player.getColumn()) == 'X'){
                        wallRow = player.getRow() - i;
                        wallColumn = player.getColumn();
                        break;
                    }
                }
                for (int i = 0; i < dragon.length; i++){
                    if(dragon[i].getColumn() == wallColumn && dragon[i].getRow() > wallRow && dragon[i].getRow() < player.getRow()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragon[i].getRow() > dragon[dragonIndex].getRow())
                            dragonIndex = i;
                    }
                }
                break;
            case 'd':
                for (int i = 1; i <= maze.getSize(); i++){
                    if(maze.getMaze(player.getRow() + i, player.getColumn()) == 'X'){
                        wallRow = player.getRow() + i;
                        wallColumn = player.getColumn();
                        break;
                    }
                }
                for (int i = 0; i < dragon.length; i++){
                    if(dragon[i].getColumn() == wallColumn && dragon[i].getRow() < wallRow && dragon[i].getRow() > player.getRow()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragon[i].getRow() < dragon[dragonIndex].getRow())
                            dragonIndex = i;
                    }
                }
                break;
            default:
                break;
        }
        return dragonIndex;
    }

    public Maze getMaze() {
        return maze;
    }

    public Dragon[] getDragon() {
        return dragon;
    }

    public Sword getSword() {
        return sword;
    }

    public Darts[] getDarts() {
        return darts;
    }

    public Shield getShield() {
        return shield;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTime() {
        return time;
    }

    public boolean isDeadDragons() {
        return deadDragons;
    }

    public void setDeadDragons(boolean deadDragons) {
        this.deadDragons = deadDragons;
    }
}