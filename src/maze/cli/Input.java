package maze.cli;

import java.util.Scanner;

public class Input {
    public static char getPlayerMove(){
        char ch = '0';

        System.out.println("Make a move (left,right,up,down):");

        Scanner s = new Scanner(System.in);
        try {
            ch = s.findInLine("[lrud]").charAt(0);
            //System.out.println(ch);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //s.close();
        return ch;
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
}
