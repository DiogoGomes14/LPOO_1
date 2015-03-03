package maze;

import maze.cli.Game;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome.");
        //new Maze(101);
        Game game = new Game();
        game.play();
    }
}
