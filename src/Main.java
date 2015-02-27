import java.awt.*;
import java.awt.List;
import java.lang.reflect.Array;
import java.util.*;


public class Main {

	public static int heroRow = 1;
	public static int heroColumn = 1;
	public static int dragonRow = 3;
	public static int dragonColumn = 1;
	public static int swordRow = 8;
	public static int swordColumn = 1;
	public static char hero = 'H';
	public static boolean dragon = true;

	public static void main(String[] args) {

		//char[][] map = mapGeneration(10);
		char[][] map = {{'X','X','X','X','X','X','X','X','X','X'},
						{'X',' ',' ',' ',' ',' ',' ',' ',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ',' ',' ',' ',' ','X'},
						{'X','X','X','X','X','X','X','X','X','X'}};
		
		printMap(map,10);

		while(!(heroRow == 5 && heroColumn == 9 && hero == 'A')){
			// ask for the next player move
			newPosition(map);

			// if the player is in the same position as the sword change the 'H' to 'A'
			if(heroRow == swordRow && heroColumn == swordColumn)
				hero = 'A';

			// if the dragon is alive calculate next dragon position
			if(dragon)
				dragonMovement(map);

			// if the hero doesn't have a sword when next to the dragon print the map and end the game (die)
			if(
				((heroRow == dragonRow + 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow - 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow && heroColumn == dragonColumn + 1)
					|| (heroRow == dragonRow && heroColumn == dragonColumn - 1))
				&& hero != 'A')
			{
				printMap(map,10);
				break;
			}

			// if the has a sword next to the dragon, kill it (dragon = false)
			else if(
				((heroRow == dragonRow + 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow - 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow && heroColumn == dragonColumn + 1)
					|| (heroRow == dragonRow && heroColumn == dragonColumn - 1))
				&& hero == 'A')
			{
				dragon = false;
			}

			printMap(map,10);
		}
		System.out.println(dragon ? "You died." : "You have reached the exit");
		System.out.println("The end.");
	}
	
	public static void newPosition(char[][] map){
		System.out.println("Make a move (left,right,up,down):");

		char ch = getMove();
		System.out.println(ch);	
		
		switch (ch){
			case 'l':
				if(map[heroRow][heroColumn - 1] == ' ')
					heroColumn--;
				break;
			case 'r':
				if(map[heroRow][heroColumn + 1] == ' ' || (map[heroRow][heroColumn + 1] == ' ' && hero == 'A' && !dragon))
					heroColumn++;
				break;
			case 'u':
				if(map[heroRow - 1][heroColumn] == ' ')
					heroRow--;
				break;
			case 'd':
				if(map[heroRow + 1][heroColumn] == ' ')
					heroRow++;
				break;
			default:
				System.out.println("Invalid string. Type again");
				break;
		}		
	}

	public static void printMap(char[][] map, int n){
		// insert the hero on the map
		map[heroRow][heroColumn] = hero;

		// insert the sword on the map
		if(hero != 'A')
			map[swordRow][swordColumn] = 'E';

		// insert the dragon on the map
		if(dragon)
			map[dragonRow][dragonColumn] = 'D';

		// check if the dragon is in the same place as the sword and change that location to 'F'
		if(dragonRow == swordRow && dragonColumn == swordColumn)
			map[dragonRow][dragonColumn] = 'F';

		// print the map
		for(int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				System.out.print(map[i][j]);
			}
			System.out.print('\n');
		}

		// reset the board
		map[dragonRow][dragonColumn] = ' ';
		map[swordRow][swordColumn] = ' ';
		map[heroRow][heroColumn] = ' ';
	}
	
	public static char getMove(){
		char ch = '0';
		Scanner s = new Scanner(System.in);
		try {
			ch = s.findInLine("[lrud]").charAt(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ch;
	}
	
	public static void dragonMovement(char[][] map){
		// generate a random number and if equivalent move made it go to the wall don't move it

		Random rand = new Random();
	    int randomNum = rand.nextInt(3);
	    
		switch (randomNum){
			case 0: // Move left
				if(map[dragonRow][dragonColumn - 1] == ' ')
					dragonColumn--;
				break;
			case 1: // Move right
				if(map[dragonRow][dragonColumn + 1] == ' ')
					dragonColumn++;
				break;
			case 2: // Move up
				if(map[dragonRow - 1][dragonColumn] == ' ')
					dragonRow--;
				break;
			case 3: // Move down
				if(map[dragonRow + 1][dragonColumn] == ' ')
					dragonRow++;
				break;
			default:
				break;
		}
	}

	public static char[][] mapGeneration(int n){
        int startX = 1;
        int startY = 1;
        int randomNum;
        int wallX;
        int wallY;
		char map[][] = new char[n][n];	// Initialize the matrix
		for(int i = 0,j = 0; i < n && j < n ; i++,j++ ){	// Start with all walls
			map[i][j] = 'X';
		}

        map[startX][startY] = ' '; // start cell

        int walls[][] = {{startX - 1,startY},{startX,startY - 1},{startX + 1,startY},{startX,startY + 1}};

        Random rand = new Random();


        while(walls.length != 0){
            randomNum = rand.nextInt(walls.length);
            wallX = startX + 2 * walls[randomNum][0];
            wallY = startY + 2 * walls[randomNum][1];
            if(wallX <= 0 || wallY <= 0 || wallX > n || wallY > n){
                Arrays.asList(walls); // para acabar
                continue;
            }

            if (map[wallX][wallY] == ' ' ){

            }
        }
		return map;
	}
}
