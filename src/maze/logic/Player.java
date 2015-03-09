package maze.logic;

import maze.cli.Interface;

public class Player extends Maze {
    protected char hero = 'H';

    public Player(){
        this.column = 0;
        this.row = 0;
    }
    public char newPosition(Maze maze, boolean deadDragons){

        String s = Interface.getPlayerMove();
        char ch = s.charAt(0);
        //System.out.println(ch);

        switch (ch){
            case 'l':
                if(maze.getMaze(row,column - 1) == ' ' || ((column - 1 == maze.getColumn()) && row == maze.getRow() && deadDragons && hero == 'A') ){
                    column--;
                    return 'm';
                }

                break;
            case 'r':
                if(maze.getMaze(row,column + 1) == ' ' || ((column + 1 == maze.getColumn()) && row == maze.getRow() && deadDragons && hero == 'A') ){
                    column++;
                    return 'm';
                }

                break;
            case 'u':
                if(maze.getMaze(row - 1,column) == ' ' || (column == maze.getColumn() && (row - 1 == maze.getRow()) && deadDragons && hero == 'A') ){
                    row--;
                    return 'm';
                }

                break;
            case 'd':
                if(maze.getMaze(row + 1,column) == ' ' || (column == maze.getColumn() && (row + 1 == maze.getRow()) && deadDragons && hero == 'A') ){
                    row++;
                    return 'm';
                }
                break;
            case 's':
                return s.charAt(2);
            default:
                System.out.println("Invalid string. Type again");
                break;
        }
        return ' ';
    }

    public char getHero(){
        return hero;
    }

    public void setHero(char hero){
        this.hero = hero;
    }
}
