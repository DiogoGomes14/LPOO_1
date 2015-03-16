package maze.cli;

import java.util.Scanner;

public class Interface {
    public static void printMap(char[][] maze, int size){

        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print(i + ((i > 9) ? "  " : "   "));
        }
        System.out.print('\n');
        for (int i = 0; i < size; i++) {
            System.out.print(i + ((i > 9) ? " " : "  "));
            for (int j = 0; j < size; j++) {
                System.out.print(maze[i][j] + "   ");
            }
            System.out.print("\n\n");
        }
    }

    public static int getNumber(){
        int number = 0;
        Scanner s = new Scanner(System.in);
        try {
            number = s.nextInt();
            //System.out.println(ch);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //s.close();
        return number;
    }

    public static String getPlayerMove(){
        String s1 = "";

        System.out.println("Make a move (left,right,up,down):");

        Scanner s = new Scanner(System.in);
        try {
            s1 = s.findInLine("^(l|r|u|d|(s l|r|u|d))");
            //System.out.println(ch);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return s1;
    }
}
