package maze.logic;

public class Player extends Maze {
    protected char hero = 'H';

    // Value: number of those things.
    // Index: 0 - Shield; 1 - Darts; ...
    protected int[] inventory = {0,0};

    public Player(){
        this.column = 0;
        this.row = 0;
    }

    public char newPosition(Maze maze, boolean deadDragons, String move){
        char ch = move.charAt(0);

        switch (ch){
            case 'l':
                if(maze.getMaze(row,column - 1) == ' ' || ((column - 1 == maze.getColumn()) && row == maze.getRow() && deadDragons && hero == 'A') ){
                    column--;
                    return 'm'; // Player moved
                }

                break;
            case 'r':
                if(maze.getMaze(row,column + 1) == ' ' || ((column + 1 == maze.getColumn()) && row == maze.getRow() && deadDragons && hero == 'A') ){
                    column++;
                    return 'm'; // Player moved
                }

                break;
            case 'u':
                if(maze.getMaze(row - 1,column) == ' ' || ((column == maze.getColumn() && row - 1 == maze.getRow()) && deadDragons && hero == 'A') ){
                    row--;
                    return 'm'; // Player moved
                }

                break;
            case 'd':
                if(maze.getMaze(row + 1,column) == ' ' || ((column == maze.getColumn() && row + 1 == maze.getRow()) && deadDragons && hero == 'A') ){
                    row++;
                    return 'm'; // Player moved
                }
                break;
            case 's':
                return move.charAt(2); // Player shot
            default:
                System.out.println("Invalid string. Type again");
                break;
        }
        return ' '; //
    }

    public char getHero(){
        return hero;
    }

    public void setHero(char hero){
        this.hero = hero;
    }

    public int getInventory(int index) {
        return inventory[index];
    }

    public void setInventory(int index, int value) {
        this.inventory[index] = value;
    }
}
