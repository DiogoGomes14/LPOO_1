package maze.logic;

import maze.cli.Input;

public class Player extends Maze {
    protected char hero = 'H';

    public Player(){
        this.column = 0;
        this.row = 0;
    }
    public void newPosition(Maze maze, Dragon dragon){

        char ch = Input.getPlayerMove();
        //System.out.println(ch);

        switch (ch){
            case 'l':
                if(maze.getMaze(row,column - 1) == ' ' || ((column - 1 == maze.getColumn()) && row == maze.getRow() && !dragon.alive && hero == 'A') )
                    column--;
                break;
            case 'r':
                if(maze.getMaze(row,column + 1) == ' ' || ((column + 1 == maze.getColumn()) && row == maze.getRow() && !dragon.alive && hero == 'A') )
                    column++;
                break;
            case 'u':
                if(maze.getMaze(row - 1,column) == ' ' || (column == maze.getColumn() && (row - 1 == maze.getRow()) && !dragon.alive && hero == 'A') )
                    row--;
                break;
            case 'd':
                if(maze.getMaze(row + 1,column) == ' ' || (column == maze.getColumn() && (row + 1 == maze.getRow()) && !dragon.alive && hero == 'A') )
                    row++;
                break;
            default:
                System.out.println("Invalid string. Type again");
                break;
        }
    }

    public char getHero(){
        return hero;
    }

    public void setHero(char hero){
        this.hero = hero;
    }
}
