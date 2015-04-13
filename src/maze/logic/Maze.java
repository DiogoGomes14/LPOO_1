package maze.logic;

import java.io.Serializable;
import java.util.Random;
import java.util.Stack;

public class Maze implements Serializable{
    private final char[][] maze;
    private final int size;
    protected int row; //exit
    protected int column; //exit

    public Maze(){
        maze = new char[][]{{'X','X','X','X','X','X','X','X','X','X'},
                            {'X',' ',' ',' ',' ',' ',' ',' ',' ','X'},
                            {'X',' ','X','X',' ','X',' ','X',' ','X'},
                            {'X',' ','X','X',' ','X',' ','X',' ','X'},
                            {'X',' ','X','X',' ','X',' ','X',' ','X'},
                            {'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
                            {'X',' ','X','X',' ','X',' ','X',' ','X'},
                            {'X',' ','X','X',' ','X',' ','X',' ','X'},
                            {'X',' ','X','X',' ',' ',' ',' ',' ','X'},
                            {'X','X','X','X','X','X','X','X','X','X'}};
        row = 5;
        column = 9;
        size = 10;
    }
    public Maze(int n){
        size = n;
        int randomNum, visitedCellsSize = (n-1)/2;
        maze = new char[n][n];
        char visitedCells[][] = new char[visitedCellsSize][visitedCellsSize];
        Stack<Integer> stack = new Stack<Integer>();
        Random rand = new Random();

        // used to fill a 2d array with odd cells empty
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i%2 == 0 || j%2 == 0){
                    maze[i][j] = 'X';
                    continue;
                }
                visitedCells[(i-1)/2][(j-1)/2] = '.';
                maze[i][j] = ' ';
            }
        }

        // Selecting initial position and exit
        randomNum = rand.nextInt(visitedCellsSize - 1);

        if (rand.nextInt(2) == 0){
            int row = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[row][randomNum] = '+';

            stack.push(randomNum);
            stack.push(row);

            if (2*row == 0){
                maze[(this.row = 0)][(this.column = randomNum*2 + 1)] = 'S';
            }
            else {
                maze[(this.row = 2*row + 2)][(this.column = randomNum*2 + 1)] = 'S';
            }
        }
        else {
            int column = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[randomNum][column] = '+';

            stack.push(column);
            stack.push(randomNum);

            if (2*column == 0){
                maze[(this.row = randomNum*2 + 1)][(this.column = 0)] = 'S';
            }
            else {
                maze[(this.row = randomNum*2 + 1)][(this.column = 2*column + 2)] = 'S';
            }
        }

        int column, row;
        boolean[] check = {false,false,false,false}; // used to check if all 4 directions where tried and failed
        while (!stack.empty()){
            row = stack.pop();
            column = stack.peek();
            stack.push(row);

            if (check[0] && check[1] && check[2] && check[3]){
                stack.pop();
                stack.pop();
                check[0] = check[1] = check[2] = check[3] = false;
            }

            int r = rand.nextInt(4);
            switch (r){
                case 0:  // right
                    check[0] = true;
                    if(column + 1 < visitedCellsSize && visitedCells[row][column + 1] == '.'){
                        visitedCells[row][column + 1] = '+';
                        maze[row * 2 + 1][column * 2 + 2] = ' ';
                        stack.push(column + 1);
                        stack.push(row);
                        check[0] = check[1] = check[2] = check[3] = false;
                    }
                    break;
                case 1: // left
                    check[1] = true;
                    if(column - 1 >= 0 && visitedCells[row][column - 1] == '.'){
                        visitedCells[row][column - 1] = '+';
                        maze[row * 2 + 1][column * 2] = ' ';
                        stack.push(column - 1);
                        stack.push(row);
                        check[0] = check[1] = check[2] = check[3] = false;
                    }
                    break;
                case 2: // down
                    check[2] = true;
                    if(row + 1 < visitedCellsSize && visitedCells[row + 1][column] == '.'){
                        visitedCells[row + 1][column] = '+';
                        maze[row * 2 + 2][column * 2 + 1] = ' ';
                        stack.push(column);
                        stack.push(row + 1);
                        check[0] = check[1] = check[2] = check[3] = false;
                    }
                    break;
                case 3: // up
                    check[3] = true;
                    if(row - 1 >= 0 && visitedCells[row - 1][column] == '.'){
                        visitedCells[row - 1][column] = '+';
                        maze[row * 2][column * 2 + 1] = ' ';
                        stack.push(column);
                        stack.push(row - 1);
                        check[0] = check[1] = check[2] = check[3] = false;
                    }
                    break;
            }
        }
    }

    /**
     * This is the row of the exit
     * @return exit row
     */
    public int getRow(){
        return row;
    }

    /**
     * This is the column of the exit
     * @return exit column
     */
    public int getColumn(){
        return column;
    }

    /**
     * @return size of the maze
     */
    public final int getSize(){
        return size;
    }

    public final char[][] getMaze(){
        return maze;
    }
    public final char getMaze(int row, int column){
        return maze[row][column];
    }

    public final void setMaze(int row, int column, char ch){
        maze[row][column] = ch;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setColumn(int column){
        this.column = column;
    }
}
