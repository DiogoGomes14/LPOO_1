package maze.logic;

import maze.cli.Output;

import java.util.Random;
import java.util.Stack;

public class Maze {
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
            int y = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[y][randomNum] = '+';

            stack.push(randomNum);
            stack.push(y);

            maze[y*2 + 1][randomNum*2 + 1] = '+';

            if (2*y == 0){
                maze[0][randomNum*2 + 1] = 'S';
            }
            else {
                maze[2*y + 2][randomNum*2 + 1] = 'S';
            }
        }
        else {
            int x = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[randomNum][x] = '+';

            stack.push(x);
            stack.push(randomNum);

            maze[randomNum*2 + 1][x*2 + 1] = '+';

            if (2*x == 0){
                maze[randomNum*2 + 1][0] = 'S';
            }
            else {
                maze[randomNum*2 + 1][2*x + 2] = 'S';
            }
        }

        int x, y;
        boolean flag = false;
        while (!stack.empty()){
            y = stack.peek();
            stack.pop();
            x = stack.peek();
            stack.push(y);
            switch (rand.nextInt(4)){
                case 0:
                    if(visitedCells[y][x + 1] == '.'){ // bugs here repeatedly
                        visitedCells[y][x + 1] = '+';
                        maze[y * 2 + 1][x * 2 + 2] = ' ';
                        flag = true;
                        stack.push(x + 1);
                        stack.push(y);
                    }
                    break;
                case 1:
                    if(visitedCells[y][x - 1] == '.'){
                        visitedCells[y][x - 1] = '+';
                        maze[y * 2 + 1][x * 2] = ' ';
                        flag = true;
                        stack.push(x - 1);
                        stack.push(y);
                    }
                    break;
                case 2:
                    if(visitedCells[y + 1][x] == '.'){
                        visitedCells[y + 1][x] = '+';
                        maze[y * 2 + 2][x * 2 + 1] = ' ';
                        flag = true;
                        stack.push(x);
                        stack.push(y + 1);
                    }
                    break;
                case 3:
                    if(visitedCells[y - 1][x] == '.'){
                        visitedCells[y - 1][x] = '+';
                        maze[y * 2][x * 2 + 1] = ' ';
                        flag = true;
                        stack.push(x);
                        stack.push(y - 1);
                    }
                    break;
            }

            //possivelmente mudar de lugar
            if (!flag){
                continue;
            }

            // rever
            if (visitedCells[y - 1][x] == '+' && visitedCells[y + 1][x] == '+' && visitedCells[y][x + 1] == '+' && visitedCells[y][x - 1] == '+'){
                stack.pop();
                stack.pop();
            }

            // para continuar
        }

        Output.printMap(maze,size);

        System.out.print("\n");

        Output.printMap(visitedCells,visitedCellsSize);

    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

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
