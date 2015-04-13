package maze.logic;

import java.io.Serializable;
import java.util.Random;

public class Dragon extends Maze implements Serializable{
    protected boolean alive = true;
    private int timeSleep = 0;
    private char dragon = 'D';

    public Dragon(){
        this.column = 0;
        this.row = 0;
    }

    public int sleepCalculation(int time){
        Random rand = new Random();
        if (dragon == 'd' && time < timeSleep)
            time++;
        else if (time >= timeSleep && dragon == 'd'){
            time = 0;
            this.dragon = 'D';
        }
        if(dragon == 'D' && rand.nextInt(5) == 0){
            dragon = 'd';
            timeSleep = rand.nextInt(3) + 2;
        }

        return time;
    }

    public boolean isAlive(){
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

    public boolean isAsleep(){
        return dragon == 'd';
    }
    public void setTimeSleep(int timeSleep){
        this.timeSleep = timeSleep;
    }
}
