package maze.logic;

import maze.cli.Interface;
import maze.logic.weapons.*;

import java.util.Random;

public class Game {
    private Dragon[] dragons;
    private Sword sword = new Sword();
    private Darts darts[] = new Darts[]{};
    private Shield shield = new Shield();
    private Player player = new Player();
    private Maze maze;
    private int time = 0;
    private boolean deadDragons = false;

    public Game() {
        maze = new Maze();
        dragons = new Dragon[1];
        dragons[0] = new Dragon();
        dragons[0].setRow(3);
        dragons[0].setColumn(1);
        sword.setRow(8);
        sword.setColumn(1);
        player.setRow(1);
        player.setColumn(1);
        darts = new Darts[]{};
        shield.setVisible(false);
    }

    public Game(int mazeSize, int numDragons){
        maze = new Maze(mazeSize);
        dragons = new Dragon[numDragons];
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
            dragons[i] = new Dragon();
            while (dragons[i].getColumn() == 0 && dragons[i].getRow() == 0) {
                randRow = rand.nextInt(mazeSize);
                randCol = rand.nextInt(mazeSize);
                if(maze.getMaze(randRow,randCol) == ' ' && randRow != player.getRow() && randCol != player.getColumn() && randRow != sword.getRow() && randCol != sword.getColumn() && randRow != shield.getRow() && randCol != shield.getColumn()){
                    dragons[i].setRow(randRow);
                    dragons[i].setColumn(randCol);
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
        printBoard();

        while (!(player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && deadDragons)) {
            // ask for the next player move

            String move = Interface.getPlayerMove();
            char ch = player.newPosition(maze, deadDragons, move);

            if( !updateGame(ch, typeOfDragonMovement) ){
                printBoard();
                break;
            }

            printBoard();
        }
        System.out.println(!deadDragons ? "You died." : "You have reached the exit.");
        System.out.println("The end.");
    }

    public boolean updateGame(char ch, int typeOfDragonMovement){
        if (ch == ' ')
            return true;
        else if (ch != 'm' && darts.length != 0){ // if move is shoot do:
            int dragonIndex = shoot(ch);
            // one less dart on inventory
            player.setInventory(1,player.getInventory(1) - 1);

            if (dragonIndex != -1){
                // kill dragons
                dragons[dragonIndex].setAlive(false);
            }

        }

        if (player.getRow() == shield.getRow() && player.getColumn() == shield.getColumn() && shield.isVisible()){
            player.setInventory(0,1);
            shield.setVisible(false);
        }

        for (Darts dart : darts){
            if(player.getRow() == dart.getRow() && player.getColumn() == dart.getColumn() && dart.isVisible()){
                player.setInventory(1, player.getInventory(1) + 1);
                dart.setVisible(false);
            }
        }

        // if the player is in the same position as the sword change the 'H' to 'A'
        if (player.getRow() == sword.getRow() && player.getColumn() == sword.getColumn()){
            player.setHero('A');
        }


        if(dragonAttack()){
            return false;
        }

        for(Dragon d : dragons){
            if(typeOfDragonMovement != 0) {
                if (typeOfDragonMovement == 2)
                    time = d.sleepCalculation(time);

                // if the Dragon is alive calculate next Dragon position
                if (d.isAlive() && d.getDragon() == 'D'){
                    Random rand = new Random();
                    int randomNum = rand.nextInt(4);

                    d.dragonMovement(maze, randomNum);
                }

            }
            if (!playerDragonInteraction(d)) {
                return false; // endgame
            }
        }


        /*
        System.out.println("dragons: " + dragons.isAlive() + " " + dragons.getRow() + "-" + dragons.getColumn());
        System.out.println("player: " + player.getHero() + " " + player.getRow() + "-" + player.getColumn());
        System.out.println("exit: " + maze.getRow() + "-" + maze.getColumn());
        System.out.println("sword: " + sword.getRow() + "-" + sword.getColumn());
        */
        return true;
    }

    public void printBoard() {

        // insert the sword on the map
        if (player.getHero() != 'A')
            maze.setMaze(sword.getRow(),sword.getColumn(), 'E');

        for (Darts weapon : darts){
            if (weapon.isVisible())
                maze.setMaze(weapon.getRow(), weapon.getColumn(), 'W');
        }

        if(shield.isVisible())
            maze.setMaze(shield.getRow(), shield.getColumn(), 's');

        for(Dragon d : dragons){
            // insert the Dragon on the map
            if (d.isAlive())
                maze.setMaze(d.getRow(),d.getColumn(), d.getDragon());

            // check if the Dragon is in the same place as the sword and change that location to 'F'
            if (d.getRow() == sword.getRow() && d.getColumn() == sword.getColumn())
                maze.setMaze(d.getRow(),d.getColumn(), 'F');

        }
        
        // insert the hero on the map
        maze.setMaze(player.getRow(), player.getColumn(), player.getHero());

        Interface.printInventory(player);
        Interface.printMap(maze.getMaze(), maze.getSize());

        // reset the board
        for(Dragon d : dragons){
            maze.setMaze(d.getRow(), d.getColumn(), ' ');
        }

        for (Darts weapon : darts){
            maze.setMaze(weapon.getRow(), weapon.getColumn(), ' ');
        }

        if(shield.isVisible())
            maze.setMaze(shield.getRow(), shield.getColumn(), ' ');

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

            // if the has a sword next to the Dragon, kill it (dragons.alive = false)
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
                for (int i = 0; i < dragons.length; i++){
                    if(dragons[i].getRow() == wallRow && dragons[i].getColumn() > wallColumn && dragons[i].getColumn() < player.getColumn()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragons[i].getColumn() > dragons[dragonIndex].getColumn())
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
                for (int i = 0; i < dragons.length; i++){
                    if(dragons[i].getRow() == wallRow && dragons[i].getColumn() < wallColumn && dragons[i].getColumn() > player.getColumn()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragons[i].getColumn() < dragons[dragonIndex].getColumn())
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
                for (int i = 0; i < dragons.length; i++){
                    if(dragons[i].getColumn() == wallColumn && dragons[i].getRow() > wallRow && dragons[i].getRow() < player.getRow()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragons[i].getRow() > dragons[dragonIndex].getRow())
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
                for (int i = 0; i < dragons.length; i++){
                    if(dragons[i].getColumn() == wallColumn && dragons[i].getRow() < wallRow && dragons[i].getRow() > player.getRow()){
                        if (dragonIndex == -1)
                            dragonIndex = i;
                        else if(dragons[i].getRow() < dragons[dragonIndex].getRow())
                            dragonIndex = i;
                    }
                }
                break;
            default:
                break;
        }
        return dragonIndex;
    }

    public boolean dragonAttack(){
        if(player.getInventory(0) == 1)
            return false;

        for (int j = 1; j <= 3; j++){
            if (maze.getMaze(player.getRow() + j, player.getColumn()) == 'X')
                break;
            if (dragonAt(player.getRow() + j, player.getColumn())){
                return true;
            }
        }

        for (int j = 1; j <= 3; j++){
            if (maze.getMaze(player.getRow() - j, player.getColumn()) == 'X')
                break;
            if (dragonAt(player.getRow() - j, player.getColumn())){
                return true;
            }
        }

        for (int j = 1; j <= 3; j++){
            if (maze.getMaze(player.getRow(), player.getColumn() + j) == 'X')
                break;
            if (dragonAt(player.getRow(), player.getColumn() + j)){
                return true;
            }
        }

        for (int j = 1; j <= 3; j++){
            if (maze.getMaze(player.getRow(), player.getColumn() - j) == 'X')
                break;
            if (dragonAt(player.getRow(), player.getColumn() - j)){
                return true;
            }
        }

        return false;
    }

    public boolean dragonAt(int row, int column){
        for (Dragon d : dragons){
            if(d.getRow() == row && d.getColumn() == column && d.isAlive()){
                return true;
            }
        }
        return false;
    }

    public Maze getMaze() {
        return maze;
    }

    public Dragon[] getDragons() {
        return dragons;
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

    public int getNumberDragonsAlive(){
        int numberOfAliveDragons = 0;
        for(Dragon d : dragons){
            if(d.isAlive())
                numberOfAliveDragons++;
        }
        if(numberOfAliveDragons == 0)
            deadDragons = true;

        return numberOfAliveDragons;
    }

    public void setShield(int row, int column) {
        this.shield.setRow(row);
        this.shield.setColumn(column);
    }

    public void setDarts(Darts[] darts) {
        this.darts = darts;
    }

    public boolean setDarts(int index, int row, int column) {
        if (index >= 0 && index < darts.length){
            this.darts[index].setRow(row);
            this.darts[index].setColumn(column);
            return true;
        }
        return false;
    }
}