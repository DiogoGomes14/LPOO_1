package maze;

import maze.cli.Game;
import maze.cli.Input;
import maze.logic.Maze;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome.");
        System.out.println("Type 0 if you want to play in the predefined maze or a different number to play in a random one: ");
        int n = Input.getNumber();
        Game game;
        if(n == 0){
            game = new Game();
        }
        else if(n == 1){
            do {
                System.out.println("Type in the size of the maze(odd number, > 8 and < 100: ");
                n = Input.getNumber();
            } while (!(n > 8 && n < 100 && n % 2 != 0));
            game = new Game(n);
        }
        else {
            System.out.println("Number of dragons: ");
            int d = Input.getNumber();
            game = new Game(n,d);
        }
        //new Maze(10001);

        game.play();
    }
}
