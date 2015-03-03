package maze.logic;

import java.util.Random;

public class Dragon extends Maze {
    protected boolean alive = true;
    private int timeSleep = 0;
    private char dragon = 'D';

    public Dragon(){
        this.column = 0;
        this.row = 0;
    }

    public void dragonMovement(Maze maze){
        // generate a random number and if equivalent move made it go to the wall don't move it

        Random rand = new Random();
        int randomNum = rand.nextInt(4);

        switch (randomNum){
            case 0: // Move left
                if(maze.getMaze(row, column - 1) == ' ')
                    column--;
                break;
            case 1: // Move right
                if(maze.getMaze(row, column + 1) == ' ')
                    column++;
                break;
            case 2: // Move up
                if(maze.getMaze(row - 1, column) == ' ')
                    row--;
                break;
            case 3: // Move down
                if(maze.getMaze(row + 1,column) == ' ')
                    row++;
                break;
            default:
                break;
        }
    }

    public boolean getAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public char getDragon(){
        return dragon;
    }
    public void setDragon(char dragon){
        this.dragon =  dragon;
    }

    public int getTimeSleep(){
        return timeSleep;
    }
    public void setTimeSleep(int timeSleep){
        this.timeSleep = timeSleep;
    }
}
