package maze.cli;

public class Output {
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
}
