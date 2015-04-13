package maze.logic;

import maze.cli.Interface;
import maze.logic.weapons.Darts;
import maze.logic.weapons.Shield;
import maze.logic.weapons.Sword;

import java.io.*;
import java.util.Random;

public class Game implements Serializable{
    private Dragon[] dragons;
    private Sword sword = new Sword();
    private Darts darts[] = new Darts[]{};
    private Shield shield = new Shield();
    private Player player = new Player();
    private Maze maze;
    private int time = 0;
    private boolean deadDragons = false;
    private int dragonMovement = 0;

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

    public Game(int mazeSize, int numDragons, int dragonMovement){
        maze = new Maze(mazeSize);
        dragons = new Dragon[numDragons];
        this.dragonMovement = dragonMovement;
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

    public void play(){
        char state = ' ';

        printBoard();


        while (state != 'D' && state != 'W') {

            // ask for the next player move
            String move = Interface.getPlayerMove();

            char ch = player.newPosition(maze, deadDragons, move);
            state = updateGame(ch);

            printBoard();
        }
        System.out.println(state == 'D' ? "You died." : "You have reached the exit.");
        System.out.println("The end.");
    }

    public char updateGame(char ch){
        if (ch != 'm' && ch != 'l' && ch != 'r' && ch != 'u' && ch != 'd')
            return 'F';
        else if (ch != 'm' && player.getInventory(1) != 0){ // Dart throw
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
            return 'D'; //endgame (hero is dead)
        }

        for(Dragon d : dragons){
            if(dragonMovement != 0) {
                if (dragonMovement == 2)
                    time = d.sleepCalculation(time);

                // if the Dragon is alive calculate next Dragon position
                if (d.isAlive() && d.getDragon() == 'D'){
                    Random rand = new Random();
                    int randomNum = rand.nextInt(4);

                    dragonMovement(d,randomNum);
                }

            }
            if(d.isAlive()){
                if (!playerDragonInteraction(d)) {
                    return 'D'; // endgame (hero is dead)
                }
            }
        }

        updateDragonsAlive();

        if (player.getRow() == maze.getRow() && player.getColumn() == maze.getColumn() && player.getHero() == 'A' && deadDragons)
                return 'W';
        return 'C';
    }

    public boolean playerDragonInteraction(Dragon dragon){
        if ((player.getRow() == dragon.getRow() + 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() - 1 && player.getColumn() == dragon.getColumn())
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() + 1)
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn() - 1)
                || (player.getRow() == dragon.getRow() && player.getColumn() == dragon.getColumn())
                )
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

        for (int j = player.getRow() + 1; j <= player.getRow() + 3 && j < maze.getSize(); j++){
            if (maze.getMaze(j, player.getColumn()) == 'X')
                break;
            if (dragonAt(j, player.getColumn(), true)){
                return true;
            }
        }

        for (int j = player.getRow() - 1; j >= player.getRow() - 3 && j > 0; j--){
            if (maze.getMaze(j, player.getColumn()) == 'X')
                break;
            if (dragonAt(j, player.getColumn(), true)){
                return true;
            }
        }

        for (int j = player.getColumn() + 1; j <= player.getColumn() + 3 && j < maze.getSize(); j++){
            if (maze.getMaze(player.getRow(), j) == 'X')
                break;
            if (dragonAt(player.getRow(), j, true)){
                return true;
            }
        }

        for (int j = player.getColumn() - 1; j >= player.getColumn() - 3 && j > 0; j--){
            if (maze.getMaze(player.getRow(), j) == 'X')
                break;
            if (dragonAt(player.getRow(), j, true)){
                return true;
            }
        }

        return false;
    }

    public void dragonMovement(Dragon drag, int num){
        switch (num){
            case 0: // Move left
                if(maze.getMaze(drag.getRow(), drag.getColumn() - 1) == ' ' && !dragonAt(drag.getRow(), drag.getColumn() - 1, false))
                    drag.setColumn(drag.getColumn() - 1);
                break;
            case 1: // Move right
                if(maze.getMaze(drag.getRow(), drag.getColumn() + 1) == ' ' && !dragonAt(drag.getRow(), drag.getColumn() + 1, false))
                    drag.setColumn(drag.getColumn() + 1);
                break;
            case 2: // Move up
                if(maze.getMaze(drag.getRow() - 1, drag.getColumn()) == ' ' && !dragonAt(drag.getRow() - 1, drag.getColumn(), false))
                    drag.setRow(drag.getRow() - 1);
                break;
            case 3: // Move down
                if(maze.getMaze(drag.getRow() + 1, drag.getColumn()) == ' ' && !dragonAt(drag.getRow() + 1, drag.getColumn(), false))
                    drag.setRow(drag.getRow() + 1);
                break;
            default:
                break;
        }
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

    public boolean dragonAt(int row, int column, boolean checkAsleep){
        for (Dragon d : dragons){
            if(checkAsleep){
                if(d.getRow() == row && d.getColumn() == column && d.isAlive() && !d.isAsleep()){
                    return true;
                }
            }
            else {
                if(d.getRow() == row && d.getColumn() == column && d.isAlive()){
                    return true;
                }
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

    public boolean isAllDragonsDead() {
        return deadDragons;
    }

    public int updateDragonsAlive(){
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

    public void save() throws IOException {
        ObjectOutputStream os= null;
        try {
            os= new ObjectOutputStream(
                    new FileOutputStream("file.dat"));
            os.writeObject(this);
            System.out.println("Saved.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (os!= null)
                os.close();
        }

    }

    public Game load() throws IOException {
        ObjectInputStream is = null;
        Game game = null;
        try {
            is = new ObjectInputStream(
                    new FileInputStream("file.dat"));
            game = (Game) is.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null)
                is.close();
        }
        return game;
    }

}