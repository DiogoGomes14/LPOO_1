package maze;

import maze.cli.Game;
import maze.logic.Maze;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome.");
        //new Maze(10001);
        Game game = new Game();
        game.play();
    }
}
