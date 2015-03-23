package maze.cli;

import maze.logic.Player;

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

    public static void printInventory(Player player){
        int shield = player.getInventory(0);
        int darts = player.getInventory(1);

        System.out.print("Inventory: ");
        if(shield == 0 && darts == 0){
            System.out.print("Nothing;");
        }
        else {
            if(player.getHero() == 'A')
                System.out.print("Sword; ");

            if(shield == 1)
                System.out.print("Shield; ");

            if(darts != 0)
                System.out.print(darts + " Darts;");
        }
        System.out.println();
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

    public static int getStart(){
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
            s1 = s.findInLine("^(l|r|u|d|(s (l|r|u|d)))");
            //System.out.println(ch);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return s1;
    }
}
