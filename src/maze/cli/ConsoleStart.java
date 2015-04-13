package maze.cli;

import maze.logic.Game;

public class ConsoleStart {
    public static void main(String[] args) {

        System.out.println("Welcome.");

        /*
        System.out.println("Type the following separated by spaces:");
        System.out.println("Maze size (0 for predefined or an odd number bigger than 8 for a random one)");
        System.out.println("Number of dragons");
        System.out.println("Type of dragons (0 for Immobile dragon, 1 for random movement or 2 for dragon movement and sleep mechanism)");
        */

        System.out.println("Type 0 for the predefined map, 1 for a game with only one dragon and anything else for a more advanced type of game");
        int dm;
        int n = Interface.getNumber();
        Game game;
        if(n == 0){
            game = new Game();
        }
        else if(n == 1414){
            game = new Game(11,2,2);
        }
        else{
            int size;
            do {
                System.out.println("Type in the size of the maze(odd number, > 8 and < 100): ");
                size = Interface.getNumber();
            } while (!(size > 8 && size < 100 && size % 2 != 0));

            if(n == 1){
                do {
                    System.out.println("Type in 0 for Immobile dragon, 1 for random movement or 2 for dragon movement and sleep mechanism: ");
                    dm = Interface.getNumber();
                } while (!(dm >= 0 && dm < 3));
                game = new Game(size,1,dm);
            }
            else {
                int d;
                do {
                    System.out.println("Type in the number of dragons ( >= 1 and < " + size / 2 + ": ");
                    d = Interface.getNumber();
                } while (!(d > 0 && d < size / 2));
                do {
                    System.out.println("Type in 0 for Immobile dragon, 1 for random movement or 2 for dragon movement and sleep mechanism: ");
                    dm = Interface.getNumber();
                } while (!(dm >= 0 && dm < 3));
                game = new Game(size,d,dm);
            }
        }

        game.play();
    }
}
