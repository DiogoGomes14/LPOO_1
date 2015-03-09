package maze;

import maze.logic.Game;
import maze.cli.Input;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome.");
        System.out.println("Type 0 for a predefined maze, 1 to play in a random maze with one dragon or anything else for a random maze with a certain number of dragons: ");
        int dm = 0;
        int n = Input.getNumber();
        Game game;
        if(n == 0){
            game = new Game();
        }
        else{
            int size;
            do {
                System.out.println("Type in the size of the maze(odd number, > 8 and < 100): ");
                size = Input.getNumber();
            } while (!(size > 8 && size < 100 && size % 2 != 0));

            if(n == 1){
                do {
                    System.out.println("Type in 0 for Immobile dragon, 1 for random movement or 2 for dragon movement and sleep mechanism: ");
                    dm = Input.getNumber();
                } while (!(dm >= 0 && dm < 3));
                game = new Game(size,1);
            }
            else {
                int d;
                do {
                    System.out.println("Type in the number of dragons (odd number, >= 1 and < " + n / 2 + ": ");
                    d = Input.getNumber();
                } while (!(d > 0 && d < size / 2));
                do {
                    System.out.println("Type in 0 for Immobile dragon, 1 for random movement or 2 for dragon movement and sleep mechanism: ");
                    dm = Input.getNumber();
                } while (!(dm >= 0 && dm < 3));
                game = new Game(size,d);
            }
        }
        //new Maze(10001);

        game.play(dm);
    }
}
